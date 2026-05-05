package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassItemDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassUpdateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassPageQueryDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import org.legend.framework.core.util.AppCtxUtil;
import com.smartmind.biz.service.ITeacherCourseClassService;
import com.smartmind.biz.service.ITeacherService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.IClazzService;
import com.smartmind.biz.dao.TeacherCourseClassMapper;
import com.smartmind.biz.dao.ClassCourseMapper;
import com.smartmind.biz.bo.model.TeacherCourseClass;
import com.smartmind.biz.bo.model.Teacher;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.Clazz;
import com.smartmind.biz.bo.model.ClassCourse;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
@Service
public class TeacherCourseClassServiceImpl extends BaseServiceImpl<TeacherCourseClassMapper, TeacherCourseClass> implements ITeacherCourseClassService{
 	@Autowired
	private TeacherCourseClassMapper teacherCourseClassMapper;

	@Autowired
	private ClassCourseMapper classCourseMapper;

	@Override
	public BaseMapper<TeacherCourseClass> getMapper() {
		return teacherCourseClassMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createTeacherCourseClass(TeacherCourseClassCreateDto teacherCourseClassCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teacherCourseClassCreateDto);
		TeacherCourseClass teacherCourseClass = TeacherCourseClassConvert.INSTANCE.convert(teacherCourseClassCreateDto);
		
		// 查询并填充教师名称
		if (teacherCourseClass.getTeacherId() != null) {
			Teacher teacher = AppCtxUtil.cfg.getBean(ITeacherService.class)
					.loadById(teacherCourseClass.getTeacherId())
					.orElseThrow(() -> new RuntimeException("未查询到教师数据"));
			teacherCourseClass.setTeacherName(teacher.getTeacherName());
		}
		
		// 查询并填充课程名称
		if (teacherCourseClass.getCourseId() != null) {
			Course course = AppCtxUtil.cfg.getBean(ICourseService.class)
					.loadById(teacherCourseClass.getCourseId())
					.orElseThrow(() -> new RuntimeException("未查询到课程数据"));
			teacherCourseClass.setCourseName(course.getCourseName());
		}
		
		// 查询并填充班级名称
		if (teacherCourseClass.getClassId() != null) {
			Clazz clazz = AppCtxUtil.cfg.getBean(IClazzService.class)
					.loadById(teacherCourseClass.getClassId())
					.orElseThrow(() -> new RuntimeException("未查询到班级数据"));
			teacherCourseClass.setClassName(clazz.getClassName());
		}
	   
		return create(teacherCourseClass);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateTeacherCourseClass(TeacherCourseClassUpdateDto teacherCourseClassCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teacherCourseClassCreateDto);
		TeacherCourseClass teacherCourseClass = TeacherCourseClassConvert.INSTANCE.convert(teacherCourseClassCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( teacherCourseClass, "teacherId","teacherName"),Ref.newRef("SmdTeacher", "id","teacherName"));
		RedundantHandler.fillRedunFields(Self.newSelf( teacherCourseClass, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( teacherCourseClass, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
	
		
		return  updateById(teacherCourseClass);
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

	/**
	 * 根据课程创建关联（自动关联班级）
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void createByCourse(TeacherCourseClassBatchCreateDto dto, SimpleUserBo simpleUser) {
		ZBeanUtil.validateBean(dto);

		String teacherId = dto.getTeacherId();
		List<Long> courseIds = dto.getCourseIds();

		if (teacherId == null) {
			ZAlert.throwSLE("教师ID不能为空");
		}
		if (courseIds == null || courseIds.isEmpty()) {
			ZAlert.throwSLE("课程ID列表不能为空");
		}

		// 查询教师信息
		Teacher teacher = AppCtxUtil.cfg.getBean(ITeacherService.class)
				.loadById(teacherId)
				.orElseThrow(() -> ZAlert.newSLE("未查询到教师数据"));

		// 批量查询课程信息
		ICourseService courseService = AppCtxUtil.cfg.getBean(ICourseService.class);
		List<Course> courseList = courseService.loadByIds(courseIds);
		if (courseList == null || courseList.isEmpty()) {
			ZAlert.throwSLE("未查询到课程数据");
		}

		// 检查课程学期是否为空（调试用，可删除）
		for (Course c : courseList) {
			if (c.getSemester() == null || c.getSemester().isEmpty()) {
				System.out.println("[DEBUG] 课程 " + c.getCourseName() + " (id=" + c.getId() + ") 的学期为空");
			}
		}

		// 查询课程关联的班级
		ChainWrapper<ClassCourse> classCourseWrapper = ChainWrapper.create(ClassCourse.class)
				.and(ClassCourse::getCourseId, "in", courseIds);
		List<ClassCourse> classCourseList = classCourseMapper.selectByQuery(classCourseWrapper);

		if (classCourseList == null || classCourseList.isEmpty()) {
			ZAlert.throwSLE("这些课程未关联班级，请先配置课程班级关联");
		}

		// 按课程ID分组
		Map<Long, List<ClassCourse>> courseClassMap = classCourseList.stream()
				.collect(Collectors.groupingBy(ClassCourse::getCourseId));

		// 收集所有班级ID
		List<Long> classIds = classCourseList.stream()
				.map(ClassCourse::getClassId)
				.distinct()
				.collect(Collectors.toList());

		// 批量查询班级信息
		IClazzService clazzService = AppCtxUtil.cfg.getBean(IClazzService.class);
		List<Clazz> clazzList = clazzService.loadByIds(classIds);
		Map<Long, Clazz> clazzMap = clazzList.stream()
				.collect(Collectors.toMap(Clazz::getId, c -> c));

		// 循环课程创建关联
		for (Course course : courseList) {
			String semester = course.getSemester();
			List<ClassCourse> courseClasses = courseClassMap.get(course.getId());
			if (courseClasses == null || courseClasses.isEmpty()) {
				continue;
			}

			for (ClassCourse classCourse : courseClasses) {
				// 获取班级信息
				Clazz clazz = clazzMap.get(classCourse.getClassId());
				if (clazz == null) {
					continue;
				}

				// 检查是否已存在关联
				ChainWrapper<TeacherCourseClass> existWrapper = ChainWrapper.create(TeacherCourseClass.class)
						.where(TeacherCourseClass::getTeacherId, "=", teacherId)
						.and(TeacherCourseClass::getCourseId, "=", course.getId())
						.and(TeacherCourseClass::getClassId, "=", classCourse.getClassId())
						.and(TeacherCourseClass::getSemester, "=", semester);
				Long count = teacherCourseClassMapper.selectCountByQuery(existWrapper);
				if (count != null && count > 0) {
					continue;
				}

				// 创建关联
				TeacherCourseClass relation = new TeacherCourseClass();
				relation.setTeacherId(teacherId);
				relation.setTeacherName(teacher.getTeacherName());
				relation.setCourseId(course.getId());
				relation.setCourseName(course.getCourseName());
				relation.setClassId(classCourse.getClassId());
				relation.setClassName(clazz.getClassName());
				relation.setSemester(semester);
				relation.setStatus("1");

				create(relation);
			}
		}
	}

	/**
	 * 批量创建关联
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void createBatch(TeacherCourseClassBatchDto batchDto, SimpleUserBo simpleUser) {
		ZBeanUtil.validateBean(batchDto);

		List<TeacherCourseClassItemDto> items = batchDto.getItems();
		if (items == null || items.isEmpty()) {
			ZAlert.throwSLE("关联列表不能为空");
		}

		// 收集所有ID
		List<String> teacherIds = items.stream().map(TeacherCourseClassItemDto::getTeacherId).distinct().collect(Collectors.toList());
		List<Long> courseIds = items.stream().map(TeacherCourseClassItemDto::getCourseId).distinct().collect(Collectors.toList());
		List<Long> classIds = items.stream().map(TeacherCourseClassItemDto::getClassId).distinct().collect(Collectors.toList());

		// 批量查询教师
		ITeacherService teacherService = AppCtxUtil.cfg.getBean(ITeacherService.class);
		List<Teacher> teacherList = teacherService.loadByIds(teacherIds);
		Map<String, Teacher> teacherMap = teacherList.stream().collect(Collectors.toMap(Teacher::getId, t -> t));

		// 批量查询课程
		ICourseService courseService = AppCtxUtil.cfg.getBean(ICourseService.class);
		List<Course> courseList = courseService.loadByIds(courseIds);
		Map<Long, Course> courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, c -> c));

		// 批量查询班级
		IClazzService clazzService = AppCtxUtil.cfg.getBean(IClazzService.class);
		List<Clazz> clazzList = clazzService.loadByIds(classIds);
		Map<Long, Clazz> clazzMap = clazzList.stream().collect(Collectors.toMap(Clazz::getId, c -> c));

		// 循环创建关联
		for (TeacherCourseClassItemDto item : items) {
			Teacher teacher = teacherMap.get(item.getTeacherId());
			Course course = courseMap.get(item.getCourseId());
			Clazz clazz = clazzMap.get(item.getClassId());

			if (teacher == null || course == null || clazz == null) {
				continue;
			}

			String semester = course.getSemester();

			// 检查是否已存在关联
			ChainWrapper<TeacherCourseClass> existWrapper = ChainWrapper.create(TeacherCourseClass.class)
					.where(TeacherCourseClass::getTeacherId, "=", item.getTeacherId())
					.and(TeacherCourseClass::getCourseId, "=", item.getCourseId())
					.and(TeacherCourseClass::getClassId, "=", item.getClassId())
					.and(TeacherCourseClass::getSemester, "=", semester);
			Long count = teacherCourseClassMapper.selectCountByQuery(existWrapper);
			if (count != null && count > 0) {
				continue;
			}

			// 创建关联
			TeacherCourseClass relation = new TeacherCourseClass();
			relation.setTeacherId(item.getTeacherId());
			relation.setTeacherName(teacher.getTeacherName());
			relation.setCourseId(item.getCourseId());
			relation.setCourseName(course.getCourseName());
			relation.setClassId(item.getClassId());
			relation.setClassName(clazz.getClassName());
			relation.setSemester(semester);
			relation.setStatus("1");

			create(relation);
		}
	}

}