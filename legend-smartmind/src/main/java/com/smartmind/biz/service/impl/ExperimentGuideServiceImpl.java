package com.smartmind.biz.service.impl;
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
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideCreateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideUpdateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuidePageQueryDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideContentUpdateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.service.IExperimentGuideService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import com.smartmind.biz.dao.ExperimentGuideMapper;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.model.TeachingPlan;
@Service
public class ExperimentGuideServiceImpl extends BaseServiceImpl<ExperimentGuideMapper, ExperimentGuide> implements IExperimentGuideService{
 	@Autowired
	private ExperimentGuideMapper experimentGuideMapper;
	
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private ILearningUnitService learningUnitService;
	
	@Override
	public BaseMapper<ExperimentGuide> getMapper() {
		return experimentGuideMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	Long createExperimentGuide(ExperimentGuideCreateDto experimentGuideCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(experimentGuideCreateDto);
		Object id1=experimentGuideMapper.selectIdByUniqueCol("guide_code", experimentGuideCreateDto.getGuideCode());
		ZAssert.isNull(id1, "指导书编码已经存在");
		ExperimentGuide experimentGuide = ExperimentGuideConvert.INSTANCE.convert(experimentGuideCreateDto);
		
		fillRedundantFields(experimentGuide);
		experimentGuide.setCreateBy(simpleUser.getUserId());
		experimentGuide.setUpdateBy(simpleUser.getUserId());
	      
		 create(experimentGuide);
		 
		 return experimentGuide.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateExperimentGuide(ExperimentGuideUpdateDto experimentGuideCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(experimentGuideCreateDto);
		Object id1=experimentGuideMapper.selectIdByUniqueCol("guide_code", experimentGuideCreateDto.getGuideCode());
		if(id1!=null&&!id1.equals(experimentGuideCreateDto.getId())){
			ZAlert.Error("指导书编码已经存在");
		}
		ExperimentGuide experimentGuide = ExperimentGuideConvert.INSTANCE.convert(experimentGuideCreateDto);
		
		fillRedundantFields(experimentGuide);
	
		return  updateById(experimentGuide);
	}
	
	private void fillRedundantFields(ExperimentGuide experimentGuide) {
		if (experimentGuide.getCourseId() != null) {
			Optional<Course> courseOpt = courseService.loadById(experimentGuide.getCourseId());
			if (courseOpt.isPresent()) {
				experimentGuide.setCourseName(courseOpt.get().getCourseName());
			}
		}
		if (experimentGuide.getUnitId() != null) {
			Optional<LearningUnit> unitOpt = learningUnitService.loadById(experimentGuide.getUnitId());
			if (unitOpt.isPresent()) {
				experimentGuide.setUnitName(unitOpt.get().getUnitName());
				experimentGuide.setUnitCode(unitOpt.get().getUnitCode());
			}
		}
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	@Override
	public Optional<ExperimentGuide> loadByGuideCode(java.lang.String guideCode){
		if(guideCode!=null){
			return Optional.ofNullable(experimentGuideMapper.selectByUniqueCol("guideCode",guideCode));
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
	public List<ExperimentGuide> loadByCourseIdAndUnitCode(Long courseId, String unitCode) {
		if (courseId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ExperimentGuide> qw = ChainWrapper.create(ExperimentGuide.class)
			.where(ExperimentGuide::getCourseId, "=", courseId);
		if (ZStrUtil.hasText(unitCode)) {
			qw.and(ExperimentGuide::getUnitCode, "LIKE", unitCode + "%");
		}
		qw.orderBy(ExperimentGuide::getExperimentSession, true);
		return loadByChainWrapper(qw);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public List<ExperimentGuide> loadByCourseIdAndUnitCodes(Long courseId, List<String> unitCodes) {
		if (courseId == null || unitCodes == null || unitCodes.isEmpty()) {
			return Lists.newArrayList();
		}
		ChainWrapper<ExperimentGuide> qw = ChainWrapper.create(ExperimentGuide.class)
			.where(ExperimentGuide::getCourseId, "=", courseId);
		qw.nestedAnd(wrapper -> {
			for (int i = 0; i < unitCodes.size(); i++) {
				String unitCode = unitCodes.get(i);
				if (i == 0) {
					wrapper.where(ExperimentGuide::getUnitCode, "LIKE", unitCode + "%");
				} else {
					wrapper.or(ExperimentGuide::getUnitCode, "LIKE", unitCode + "%");
				}
			}
		});
		qw.orderBy(ExperimentGuide::getExperimentSession, true);
		return loadByChainWrapper(qw);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateMainContent(ExperimentGuideContentUpdateDto dto) {
		ZAssert.notNull(dto.getGuideId(), "ID不能为空");
		ZAssert.notNull(dto.getMainContent(), "指导书内容不能为空");
		updateById(new ExperimentGuide().setId(dto.getGuideId()).setMainContent(dto.getMainContent()));
	}
	
}