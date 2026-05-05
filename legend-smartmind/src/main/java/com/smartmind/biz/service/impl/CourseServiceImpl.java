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
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.course.CourseCreateDto;
import com.smartmind.biz.bo.dto.course.CourseUpdateDto;
import com.smartmind.biz.bo.dto.course.CoursePageQueryDto;
import com.smartmind.biz.bo.dto.course.CourseConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;

import com.smartmind.biz.service.IClassCourseService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.IClassStudentService;
import com.smartmind.biz.dao.CourseMapper;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.bo.model.TeacherCourseClass;
import com.smartmind.biz.bo.model.ClassSession;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.model.ClassCourse;
import com.smartmind.biz.dao.ClassSessionMapper;
import com.smartmind.biz.dao.ClassActivityMapper;
import com.smartmind.biz.dao.TeacherCourseClassMapper;
import com.smartmind.biz.bo.dto.student.StudentCourseProgressDto;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
@Service
public class CourseServiceImpl extends BaseServiceImpl<CourseMapper, Course> implements ICourseService{
 	@Autowired
	private CourseMapper courseMapper;
 	
 	@Autowired IClassCourseService classCourseService;
 	
 	@Autowired
	private IClassStudentService classStudentService;
 	@Autowired
	private TeacherCourseClassMapper teacherCourseClassMapper;
 	@Autowired
	private ClassSessionMapper classSessionMapper;
 	@Autowired
	private ClassActivityMapper classActivityMapper;
 	
	@Override
	public BaseMapper<Course> getMapper() {
		return courseMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public Long createCourse(CourseCreateDto courseCreateDto, SimpleUserBo simpleUser) {
		// 生成课程编码
		courseCreateDto.setCourseCode(ZUidUtil.base36Snowid());
		ZBeanUtil.validateBean(courseCreateDto);
		Course course = CourseConvert.INSTANCE.convert(courseCreateDto);
		
		create(course);
		
		classCourseService.saveCourseClassRelation(course.getId(), courseCreateDto.getClassIds());
		
		return course.getId(); 
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	Long updateCourse(CourseUpdateDto courseCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(courseCreateDto);
		Course course = CourseConvert.INSTANCE.convert(courseCreateDto);
		
		
		updateById(course);
		
		classCourseService.saveCourseClassRelation(course.getId(), courseCreateDto.getClassIds());
		
		return course.getId();
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
	@Transactional(rollbackFor=Exception.class)
	public void updateKnowledgeFramework(Long id, String knowledgeFramework) {
		ZAssert.notNull(id, "课程ID不能为空");
		Course course = new Course();
		course.setId(id);
		course.setKnowledgeFramework(knowledgeFramework);
		updateById(course);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<Course> loadCoursesByStudentId(String studentId) {
		if (studentId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.select("class_id AS classId")
			.from("smd_class_student AS cs")
			.where("student_id", "=", studentId)
			.and("status", "=", "1");
		List<ClassStudent> classStudents = classStudentService.loadByQuery(studentQw);
		if (classStudents == null || classStudents.isEmpty()) {
			return Lists.newArrayList();
		}
		List<Long> classIds = classStudents.stream().map(ClassStudent::getClassId).collect(Collectors.toList());
	
		
		List<ClassCourse> classCourses = classCourseService.loadByChainQuery(ChainWrapper.create(ClassCourse.class)
				.where("class_id", "IN", classIds)
				.and("status", "=", "1"));
		if (classCourses == null || classCourses.isEmpty()) {
			return Lists.newArrayList();
		}
		List<Long> courseIds = classCourses.stream().map(ClassCourse::getCourseId).collect(Collectors.toList());
		if (courseIds == null || courseIds.isEmpty()) {
			return Lists.newArrayList();
		}	
		ChainWrapper<Course> courseQw = ChainWrapper.create(Course.class)	
			.where("id", "IN", courseIds)
//			.where("status", "=", "1")
			.orderBy("create_time", false);
		return loadByChainWrapper(courseQw);
		
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public int countCoursesByStudentId(String studentId) {
		if (studentId == null) {
			return 0;
		}
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.select("class_id AS classId")
			.from("smd_class_student AS cs")
			.where("student_id", "=", studentId)
			.where("status", "=", "1");
		List<ClassStudent> classStudents = classStudentService.loadByQuery(studentQw);
		if (classStudents == null || classStudents.isEmpty()) {
			return 0;
		}
		List<Long> classIds = classStudents.stream()
			.map(ClassStudent::getClassId)
			.collect(Collectors.toList());
		
		List<ClassCourse> tccList = classCourseService.loadByChainQuery(ChainWrapper.create(ClassCourse.class)
				.select("course_id AS courseId")
				.where("class_id", "IN", classIds)
				.and("status", "=", "1"));
		return tccList != null ? tccList.size() : 0;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<StudentCourseProgressDto> loadCourseProgressByStudentId(String studentId) {
		if (studentId == null) {
			return Lists.newArrayList();
		}
		List<Course> courses = loadCoursesByStudentId(studentId);
		if (courses == null || courses.isEmpty()) {
			return Lists.newArrayList();
		}
		List<StudentCourseProgressDto> result = Lists.newArrayList();
		for (Course course : courses) {
			StudentCourseProgressDto dto = new StudentCourseProgressDto();
			dto.setId(course.getId());
			dto.setName(course.getCourseName());
			
			ChainWrapper<ClassSession> sessionQw = ChainWrapper.create(ClassSession.class)
				.where("course_id", "=", course.getId())
				.where("status", "=", "1");
			List<ClassSession> sessions = classSessionMapper.selectByQuery(sessionQw);
			
			int totalActivities = 0;
			int completedActivities = 0;
			if (sessions != null && !sessions.isEmpty()) {
				for (ClassSession session : sessions) {
					ChainWrapper<ClassActivity> activityQw = ChainWrapper.create(ClassActivity.class)
						.where("session_id", "=", session.getId())
						.where("status", "=", "1");
					List<ClassActivity> activities = classActivityMapper.selectByQuery(activityQw);
					if (activities != null) {
						totalActivities += activities.size();
						for (ClassActivity activity : activities) {
							if ("2".equals(activity.getStatus())) {
								completedActivities++;
							}
						}
					}
				}
			}
			
			int progress = totalActivities > 0 ? (completedActivities * 100 / totalActivities) : 0;
			dto.setProgress(progress);
			result.add(dto);
		}
		return result;
	}
}