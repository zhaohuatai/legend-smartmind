package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ids.ZUidUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.exceptions.ServiceLogicalException;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityCreateDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityUpdateDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityPageQueryDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.dao.ClassActivityMapper;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.dao.ClassSessionMapper;
import com.smartmind.biz.bo.model.ClassSession;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceUpdateDto;
import com.smartmind.biz.bo.model.ClassActivityResource;
import com.smartmind.biz.dao.ClassActivityResourceMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.dao.ClassActivitySubmitMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.dao.ClassActivitySubmitDetailMapper;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.dao.ClassStudentMapper;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.dao.StudentMapper;
import com.smartmind.biz.service.IClassStudentService;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
@Service
public class ClassActivityServiceImpl extends BaseServiceImpl<ClassActivityMapper, ClassActivity> implements IClassActivityService{
 	@Autowired
	private ClassActivityMapper classActivityMapper;
 	@Autowired
	private ClassSessionMapper classSessionMapper;
 	@Autowired
	private ClassActivityResourceMapper classActivityResourceMapper;
 	@Autowired
	private ClassActivitySubmitMapper classActivitySubmitMapper;
 	@Autowired
	private ClassActivitySubmitDetailMapper classActivitySubmitDetailMapper;
 	@Autowired
	private ClassStudentMapper classStudentMapper;
 	@Autowired
	private IClassStudentService classStudentService;
	
	@Override
	public BaseMapper<ClassActivity> getMapper() {
		return classActivityMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassActivity(ClassActivityCreateDto classActivityCreateDto,SimpleUserBo simpleUser){
		classActivityCreateDto.setActivityCode(ZUidUtil.base36Snowid());
		if (ZStrUtil.isBlank(classActivityCreateDto.getActivityName())) {
			String activityTypeName = DictCache.getDictLabel("class_activity_type", classActivityCreateDto.getActivityType()).orElse("");
			String activityName = activityTypeName;
//			String activityName = getActivityTypeName(classActivityCreateDto.getActivityType());
			classActivityCreateDto.setActivityName(activityName);
		}
		ZBeanUtil.validateBean(classActivityCreateDto);
		ClassActivity classActivity = ClassActivityConvert.INSTANCE.convert(classActivityCreateDto);
		
		classActivity.setSubmitStuCount(0);
		classActivity.setAvgScore(java.math.BigDecimal.ZERO);
		classActivity.setScore(classActivityCreateDto.getScore() != null ? classActivityCreateDto.getScore() : java.math.BigDecimal.valueOf(100));

		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.where(ClassStudent::getClassId, "=", classActivity.getClazzId())
			.where(ClassStudent::getStatus, "=", "1");
		Long stuCount = classStudentMapper.selectCountByQuery(studentQw);
		classActivity.setTotalStuCount(stuCount != null ?stuCount.intValue() : 0);

	   
		if (classActivity.getSessionId() != null) {
			ClassSession session = classSessionMapper.selectByPrimaryKey(classActivity.getSessionId());
			if (session != null) {
				classActivity.setCourseId(session.getCourseId());
				classActivity.setCourseName(session.getCourseName());
				classActivity.setClazzId(session.getClazzId());
				classActivity.setClazzName(session.getClazzName());
			}
		}
		
		int result = create(classActivity);
		
		List<ClassActivityResourceCreateDto> resourceList = classActivityCreateDto.getActivityResourceList();
		ZAssert.notEmpty(resourceList, "请选择对应题目");
		
		if (resourceList != null && !resourceList.isEmpty()) {
			ZAssert.notNull(classActivity.getScore(), "活动总分不能为空");
			java.math.BigDecimal totalScore = java.math.BigDecimal.ZERO;
			for (ClassActivityResourceCreateDto resDto : resourceList) {
				if (resDto.getScore() != null) {
					totalScore = totalScore.add(resDto.getScore());
				}
			}
			if (totalScore.compareTo(classActivity.getScore()) != 0) {
				throw new ServiceLogicalException("各资源分数之和必须等于活动总分");
			}
			for (int i = 0; i < resourceList.size(); i++) {
				ClassActivityResourceCreateDto resDto = resourceList.get(i);
				ClassActivityResource resource = new ClassActivityResource();
				resource.setActivityId(classActivity.getId());
				resource.setResourceType(resDto.getResourceType());
				resource.setResourceId(resDto.getResourceId());
				resource.setResourceName(resDto.getResourceName());
				resource.setScore(resDto.getScore());
				resource.setSortOrder(i + 1);
				resource.setCreateTime(new java.util.Date());
				classActivityResourceMapper.insertSelective(resource);
			}
		}
		
		return result;
	}


	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassActivity(ClassActivityUpdateDto classActivityCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivityCreateDto);
		ClassActivity classActivity = ClassActivityConvert.INSTANCE.convert(classActivityCreateDto);
		return  updateById(classActivity);
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
	public List<ClassActivity> queryBySessionId(Long sessionId) {
		if (sessionId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassActivity> qw = ChainWrapper.create(ClassActivity.class)
			.where(ClassActivity::getSessionId, "=", sessionId)
			.orderBy(ClassActivity::getSortOrder, true)
			.orderBy(ClassActivity::getCreateTime, false);
		return loadByChainWrapper(qw);
	}
	

	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void startActivity(Long activityId, SimpleUserBo simpleUser) {
		if (activityId == null) {
			ZAlert.throwSLE("活动ID不能为空");
		}
		ClassActivity activity = loadById(activityId).orElse(null);
		if (activity == null) {
			ZAlert.throwSLE("活动不存在");
		}
		if (!"0".equals(activity.getStatus())) {
			ZAlert.throwSLE("活动已在进行中");
		}
		
		ChainWrapper<ClassActivityResource> resourceQw = ChainWrapper.create(ClassActivityResource.class)
			.where(ClassActivityResource::getActivityId, "=", activityId)
			.orderBy(ClassActivityResource::getSortOrder, true);
		List<ClassActivityResource> resources = classActivityResourceMapper.selectByQuery(resourceQw);
		if (resources == null || resources.isEmpty()) {
			ZAlert.throwSLE("活动未配置资源");
		}
		
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.where(ClassStudent::getClassId, "=", activity.getClazzId())
			.where(ClassStudent::getStatus, "=", "1");
		List<ClassStudent> students = classStudentMapper.selectByQuery(studentQw);
		if (students == null || students.isEmpty()) {
			ZAlert.throwSLE("班级暂无学生");
		}
		
		for (ClassStudent student : students) {
			ChainWrapper<ClassActivitySubmit> submitQw = ChainWrapper.create(ClassActivitySubmit.class)
				.where(ClassActivitySubmit::getActivityId, "=", activityId)
				.where(ClassActivitySubmit::getStudentId, "=", student.getStudentId());
			Long existCount = classActivitySubmitMapper.selectCountByQuery(submitQw);
			if (existCount != null && existCount > 0) {
				continue;
			}
			
			ClassActivitySubmit submit = new ClassActivitySubmit();
			submit.setSubmitCode(ZUidUtil.base36Snowid());
			submit.setActivityId(activityId);
			submit.setSessionId(activity.getSessionId());
			submit.setCourseId(activity.getCourseId());
			submit.setClazzId(activity.getClazzId());
			submit.setStudentId(student.getStudentId());
			submit.setStudentName(student.getStudentName());
			submit.setTotalScore(java.math.BigDecimal.ZERO);
			submit.setStatus("0");
			submit.setCreateTime(new java.util.Date());
			classActivitySubmitMapper.insertSelective(submit);
			
			for (ClassActivityResource res : resources) {
				ClassActivitySubmitDetail detail = new ClassActivitySubmitDetail();
				detail.setSubmitId(submit.getId());
				detail.setActivityId(activityId);
				detail.setResourceId(res.getResourceId());
				detail.setResourceType(res.getResourceType());
				detail.setResourceName(res.getResourceName());
				detail.setScore(java.math.BigDecimal.ZERO);
				detail.setFullScore(res.getScore());
				detail.setStatus("0");
				detail.setCreateTime(new java.util.Date());
				classActivitySubmitDetailMapper.insertSelective(detail);
			}
		}
		
		activity.setStatus("1");
		activity.setStartTime(new java.util.Date());
	
		activity.setUpdateTime(new java.util.Date());
		updateById(activity);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void deleteActivity(Long activityId) {
		if (activityId == null) {
			ZAlert.throwSLE("活动ID不能为空");
		}
		ClassActivity activity = loadById(activityId).orElse(null);
		if (activity == null) {
			ZAlert.throwSLE("活动不存在");
		}
		
		ChainWrapper<ClassActivityResource> resourceQw = ChainWrapper.create(ClassActivityResource.class)
			.where(ClassActivityResource::getActivityId, "=", activityId);
		classActivityResourceMapper.deleteByQuery(resourceQw);
		
		ChainWrapper<ClassActivitySubmit> submitQw = ChainWrapper.create(ClassActivitySubmit.class)
			.where(ClassActivitySubmit::getActivityId, "=", activityId);
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(submitQw);
		if (submits != null && !submits.isEmpty()) {
			List<Long> submitIds = Lists.newArrayList();
			for (ClassActivitySubmit submit : submits) {
				submitIds.add(submit.getId());
			}
			ChainWrapper<ClassActivitySubmitDetail> detailQw = ChainWrapper.create(ClassActivitySubmitDetail.class)
				.where(ClassActivitySubmitDetail::getSubmitId, "in", submitIds);
			classActivitySubmitDetailMapper.deleteByQuery(detailQw);
		}
		classActivitySubmitMapper.deleteByQuery(submitQw);
		
		deleteById(activityId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<ClassActivity> loadActivitiesBySessionAndStudentId(Long sessionId, String studentId) {
		if (sessionId == null || studentId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.select("cs.class_id AS classId")
			.from("smd_class_student AS cs")
			.where("cs.student_id", "=", studentId)
			.where("cs.status", "=", "1");
		List<ClassStudent> classStudents = classStudentService.loadByQuery(studentQw);
		if (classStudents == null || classStudents.isEmpty()) {
			return Lists.newArrayList();
		}
		List<Long> classIds = classStudents.stream()
			.map(ClassStudent::getClassId)
			.collect(Collectors.toList());
		ChainWrapper<ClassActivity> activityQw = ChainWrapper.create(ClassActivity.class)
			.where("session_id", "=", sessionId)
			.where("clazz_id", "IN", classIds)
			.where("status", "!=", "3")
			.orderBy("sort_order", true)
			.orderBy("create_time", false);
		return loadByChainWrapper(activityQw);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public int countActivitiesByStudentId(String studentId) {
		if (studentId == null) {
			return 0;
		}
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.select("cs.class_id AS classId")
			.from("smd_class_student AS cs")
			.where("cs.student_id", "=", studentId)
			.where("cs.status", "=", "1");
		List<ClassStudent> classStudents = classStudentService.loadByQuery(studentQw);
		if (classStudents == null || classStudents.isEmpty()) {
			return 0;
		}
		List<Long> classIds = classStudents.stream()
			.map(ClassStudent::getClassId)
			.collect(Collectors.toList());
		ChainWrapper<ClassActivity> activityQw = ChainWrapper.create(ClassActivity.class)
			.where("clazz_id", "IN", classIds)
			.where("status", "!=", "3");
		Long count = classActivityMapper.selectCountByQuery(activityQw);
		return count != null ? count.intValue() : 0;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public int countCompletedActivitiesByStudentId(String studentId) {
		if (studentId == null) {
			return 0;
		}
		ChainWrapper<ClassActivitySubmit> submitQw = ChainWrapper.create(ClassActivitySubmit.class)
			.select("DISTINCT cas.activity_id AS activityId")
			.from("smd_class_activity_submit AS cas")
			.where("cas.student_id", "=", studentId)
			.where("cas.status", "=", "1");
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(submitQw);
		return submits != null ? submits.size() : 0;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public ClassActivityUpdateDto loadforUpdateById(Long id) {
		if (id == null) {
			return null;
		}
		ClassActivity activity = loadById(id).orElse(null);
		if (activity == null) {
			return null;
		}
		ClassActivityUpdateDto dto = new ClassActivityUpdateDto();
		dto.setActivityCode(activity.getActivityCode());
		dto.setActivityName(activity.getActivityName());
		dto.setSessionId(activity.getSessionId());
		dto.setCourseId(activity.getCourseId());
		dto.setCourseName(activity.getCourseName());
		dto.setClazzId(activity.getClazzId());
		dto.setClazzName(activity.getClazzName());
		dto.setActivityType(activity.getActivityType());
		dto.setStartTime(activity.getStartTime());
		dto.setEndTime(activity.getEndTime());
		dto.setScore(activity.getScore());
		dto.setDuration(activity.getDuration());
		dto.setStatus(activity.getStatus());
		dto.setSortOrder(activity.getSortOrder());
		dto.setRemark(activity.getRemark());
		dto.setActivityContent(activity.getActivityContent());
		
		ChainWrapper<ClassActivityResource> qw = ChainWrapper.create(ClassActivityResource.class)
			.where(ClassActivityResource::getActivityId, "=", id)
			.orderBy(ClassActivityResource::getSortOrder, true);
		List<ClassActivityResource> resources = classActivityResourceMapper.selectByQuery(qw);

		if (resources != null && !resources.isEmpty()) {
			List<ClassActivityResourceUpdateDto> resourceDtoList = Lists.newArrayList();
			for (ClassActivityResource res : resources) {
				ClassActivityResourceUpdateDto resDto = new ClassActivityResourceUpdateDto();
				resDto.setId(res.getId());
				resDto.setResourceType(res.getResourceType());
				resDto.setResourceId(res.getResourceId());
				resDto.setResourceName(res.getResourceName());
				resDto.setScore(res.getScore());
				resourceDtoList.add(resDto);
			}
			dto.setActivityResourceList(resourceDtoList);
		}
		
		return dto;
	}
}