package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;
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
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionCreateDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionUpdateDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionPageQueryDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.service.IClassSessionService;
import com.smartmind.biz.service.IClassStudentService;
import com.smartmind.biz.dao.ClassSessionMapper;
import com.smartmind.biz.bo.model.ClassSession;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.dao.CourseMapper;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.dao.ClazzMapper;
import com.smartmind.biz.bo.model.Clazz;
import com.smartmind.biz.dao.TeacherMapper;
import com.smartmind.biz.bo.model.Teacher;
import com.smartmind.biz.dao.LearningUnitMapper;
import com.smartmind.biz.bo.model.LearningUnit;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
@Service
public class ClassSessionServiceImpl extends BaseServiceImpl<ClassSessionMapper, ClassSession> implements IClassSessionService{
 	@Autowired
	private ClassSessionMapper classSessionMapper;
 	@Autowired
	private CourseMapper courseMapper;
 	@Autowired
	private ClazzMapper clazzMapper;
 	@Autowired
	private TeacherMapper teacherMapper;
 	@Autowired
	private LearningUnitMapper learningUnitMapper;
 	@Autowired
	private IClassStudentService classStudentService;
	
	@Override
	public BaseMapper<ClassSession> getMapper() {
		return classSessionMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassSession(ClassSessionCreateDto classSessionCreateDto,SimpleUserBo simpleUser){
		classSessionCreateDto.setTeacherId(simpleUser.getUserId());
		classSessionCreateDto.setSessionCode(ZUidUtil.base36Snowid());
		ZBeanUtil.validateBean(classSessionCreateDto);
		ClassSession classSession = ClassSessionConvert.INSTANCE.convert(classSessionCreateDto);
		
		buildUnitsInfo(classSession, classSessionCreateDto.getUnitIds());
		fillRedundantFields(classSession);
	   
		return create(classSession);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassSession(ClassSessionUpdateDto classSessionUpdateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classSessionUpdateDto);
		ClassSession classSession = ClassSessionConvert.INSTANCE.convert(classSessionUpdateDto);
	
		buildUnitsInfo(classSession, classSessionUpdateDto.getUnitIds());
		fillRedundantFields(classSession);
		
		return  updateById(classSession);
	}
	
	private void fillRedundantFields(ClassSession classSession) {
		if (classSession.getCourseId() != null) {
			Course course = courseMapper.selectByPrimaryKey(classSession.getCourseId());
			if (course != null) {
				classSession.setCourseName(course.getCourseName());
			}
		}
		if (classSession.getClazzId() != null) {
			Clazz clazz = clazzMapper.selectById(classSession.getClazzId());
			if (clazz != null) {
				classSession.setClazzName(clazz.getClassName());
			}
		}
		if (classSession.getTeacherId() != null) {
			Teacher teacher = teacherMapper.selectByPrimaryKey(classSession.getTeacherId());
			if (teacher != null) {
				classSession.setTeacherName(teacher.getTeacherName());
			}
		}
	}

	private void buildUnitsInfo(ClassSession classSession, String unitIdsStr) {
		if (ZStrUtil.isBlank(unitIdsStr)) {
			classSession.setUnitsInfo("[]");
			return;
		}
		List<Long> unitIds = Arrays.stream(unitIdsStr.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.map(Long::valueOf)
				.collect(Collectors.toList());
		if (unitIds.isEmpty()) {
			classSession.setUnitsInfo("[]");
			return;
		}
		List<LearningUnit> units = learningUnitMapper.selectByIds(unitIds);
		JSONArray jsonArray = new JSONArray();
		for (LearningUnit unit : units) {
			JSONObject obj = new JSONObject();
			obj.put("unitCode", unit.getUnitCode());
			obj.put("unitName", unit.getUnitName());
			jsonArray.add(obj);
		}
		classSession.setUnitsInfo(jsonArray.toJSONString());
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
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<ClassSession> loadSessionsByCourseAndStudentId(Long courseId, String studentId) {
		if (courseId == null || studentId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassStudent> studentQw = ChainWrapper.create(ClassStudent.class)
			.select("class_id AS classId")
			.from("smd_class_student AS cs")
			.where("student_id", "=", studentId)
			.where("status", "=", "1");
		List<ClassStudent> classStudents = classStudentService.loadByQuery(studentQw);
		if (classStudents == null || classStudents.isEmpty()) {
			return Lists.newArrayList();
		}
		List<Long> classIds = classStudents.stream()
			.map(ClassStudent::getClassId)
			.collect(Collectors.toList());
		ChainWrapper<ClassSession> sessionQw = ChainWrapper.create(ClassSession.class)
			.where("course_id", "=", courseId)
			.where("clazz_id", "IN", classIds)
//			.where("status", "!=", "3")
			.orderBy("start_time", false);
		return loadByChainWrapper(sessionQw);
	}
}