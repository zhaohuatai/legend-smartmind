package com.smartmind.biz.service.impl;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanCreateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanUpdateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanPageQueryDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanContentUpdateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.service.ITeachingPlanService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import com.smartmind.biz.dao.TeachingPlanMapper;
import com.smartmind.biz.bo.model.TeachingPlan;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
@Service
public class TeachingPlanServiceImpl extends BaseServiceImpl<TeachingPlanMapper, TeachingPlan> implements ITeachingPlanService{
 	@Autowired
	private TeachingPlanMapper teachingPlanMapper;
	
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private ILearningUnitService learningUnitService;
	
	@Override
	public BaseMapper<TeachingPlan> getMapper() {
		return teachingPlanMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	Long createTeachingPlan(TeachingPlanCreateDto teachingPlanCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teachingPlanCreateDto);
		Object id1=teachingPlanMapper.selectIdByUniqueCol("plan_code", teachingPlanCreateDto.getPlanCode());
		ZAssert.isNull(id1, "教案编码已经存在");
		TeachingPlan teachingPlan = TeachingPlanConvert.INSTANCE.convert(teachingPlanCreateDto);
		
		fillRedundantFields(teachingPlan);
		
		teachingPlan.setTeacherId(simpleUser.getUserId());
		teachingPlan.setCreateBy(simpleUser.getUserId());
		teachingPlan.setUpdateBy(simpleUser.getUserId());
	   
		 create(teachingPlan);
		 return teachingPlan.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateTeachingPlan(TeachingPlanUpdateDto teachingPlanCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teachingPlanCreateDto);
		Serializable id1=(Long) teachingPlanMapper.selectIdByUniqueCol("plan_code", teachingPlanCreateDto.getPlanCode());
		if(id1!=null&&!id1.equals(teachingPlanCreateDto.getId())){
			ZAlert.Error("教案编码已经存在");
		}
		TeachingPlan teachingPlan = TeachingPlanConvert.INSTANCE.convert(teachingPlanCreateDto);
		
		fillRedundantFields(teachingPlan);
		
		return  updateById(teachingPlan);
	}
	
	private void fillRedundantFields(TeachingPlan teachingPlan) {
		if (teachingPlan.getCourseId() != null) {
			Optional<Course> courseOpt = courseService.loadById(teachingPlan.getCourseId());
			if (courseOpt.isPresent()) {
				teachingPlan.setCourseName(courseOpt.get().getCourseName());
			}
		}
		if (teachingPlan.getUnitId() != null) {
			Optional<LearningUnit> unitOpt = learningUnitService.loadById(teachingPlan.getUnitId());
			if (unitOpt.isPresent()) {
				teachingPlan.setUnitName(unitOpt.get().getUnitName());
				teachingPlan.setUnitCode(unitOpt.get().getUnitCode());
			}
		}
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	@Override
	public Optional<TeachingPlan> loadByPlanCode(java.lang.String planCode){
		if(planCode!=null){
			return Optional.ofNullable(teachingPlanMapper.selectByUniqueCol("planCode",planCode));
		}
		return Optional.empty();
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusListDto statusListDto) {
		ZBeanUtil.validateBean(statusListDto);
		setStatusByIds(statusListDto.getIds(),  statusListDto.getStatus()) ;
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusDto status) {
		ZBeanUtil.validateBean(status);
		setStatusById(status.getId(),  status.getStatus()) ;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public List<TeachingPlan> loadByCourseIdAndUnitCode(Long courseId, String unitCode) {
		if (courseId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<TeachingPlan> qw = ChainWrapper.create(TeachingPlan.class)
			.where(TeachingPlan::getCourseId, "=", courseId);
		if (ZStrUtil.hasText(unitCode)) {
			qw.and(TeachingPlan::getUnitCode, "LIKE", unitCode + "%");
		}
		qw.orderBy(TeachingPlan::getLessonHour, true).orderBy(TeachingPlan::getLessonSession, true);
		return loadByChainWrapper(qw);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateMarkdownContent(TeachingPlanContentUpdateDto dto) {
		ZAssert.notNull(dto.getPlanId(), "教案ID不能为空");
		ZAssert.notNull(dto.getMainContent(), "教案内容不能为空");
		updateById(new TeachingPlan().setId(dto.getPlanId()).setMainContent(dto.getMainContent()));
	}
	
}