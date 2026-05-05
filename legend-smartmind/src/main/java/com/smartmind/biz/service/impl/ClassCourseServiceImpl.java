package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
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
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseCreateDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseUpdateDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCoursePageQueryDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassCourseService;
import com.smartmind.biz.dao.ClassCourseMapper;
import com.smartmind.biz.bo.model.ClassCourse;
import com.smartmind.biz.dao.CourseMapper;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.dao.ClazzMapper;
import com.smartmind.biz.bo.model.Clazz;
@Service
public class ClassCourseServiceImpl extends BaseServiceImpl<ClassCourseMapper, ClassCourse> implements IClassCourseService{
 	@Autowired
	private ClassCourseMapper classCourseMapper;
 	@Autowired
	private CourseMapper courseMapper;
 	@Autowired
	private ClazzMapper clazzMapper;
	
	@Override
	public BaseMapper<ClassCourse> getMapper() {
		return classCourseMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassCourse(ClassCourseCreateDto classCourseCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classCourseCreateDto);
		ClassCourse classCourse = ClassCourseConvert.INSTANCE.convert(classCourseCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( classCourse, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( classCourse, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
	   
		return create(classCourse);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassCourse(ClassCourseUpdateDto classCourseCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classCourseCreateDto);
		ClassCourse classCourse = ClassCourseConvert.INSTANCE.convert(classCourseCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( classCourse, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( classCourse, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
	
		
		return  updateById(classCourse);
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusListDto statusListDto) {
		ZBeanUtil.validateBean(statusListDto);
		setStatusByIds(statusListDto.getIds(),  statusListDto.getStatus()) ;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setStatus(StatusDto status) {
		ZBeanUtil.validateBean(status);
		setStatusById(status.getId(),  status.getStatus()) ;
	}

	@Override
	public List<Long> queryClassIdsByCourseId(Long courseId) {
		ChainWrapper<ClassCourse> wrapper = ChainWrapper.create(ClassCourse.class);
		wrapper.where("course_id", "=", courseId);
		List<ClassCourse> list = classCourseMapper.selectByQuery(wrapper);
		return list.stream().map(ClassCourse::getClassId).collect(Collectors.toList());
	}
	
	@Override
	public List<ClassCourse> queryCourseClassByCourseId(Long courseId) {
		ChainWrapper<ClassCourse> wrapper = ChainWrapper.create(ClassCourse.class);
		wrapper.where("course_id", "=", courseId);
		List<ClassCourse> list = classCourseMapper.selectByQuery(wrapper);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveCourseClassRelation(Long courseId, List<Long> classIds) {
		// 删除旧关联
		ChainWrapper<ClassCourse> deleteWrapper = ChainWrapper.create(ClassCourse.class);
		deleteWrapper.where("course_id", "=", courseId);
		classCourseMapper.deleteByQuery(deleteWrapper);

		Course course = courseMapper.selectByPrimaryKey(courseId);
		
		// 保存新关联（只填充关键字段）
		if (classIds != null && !classIds.isEmpty()) {
			for (Long classId : classIds) {
				Clazz clazz= clazzMapper.selectById(classId);
				ClassCourse relation = new ClassCourse();
				relation.setCourseId(courseId);
				relation.setClassId(classId);
				relation.setClassName(clazz.getClassName());
				relation.setStatus("1");
				relation.setCourseName(course.getCourseName());
				relation.setSchoolYear(course.getSchoolYear());
				relation.setSemester(course.getSemester());
				classCourseMapper.insertSelective(relation);
			}
		}

		// 更新课程的 className 字段
		//updateCourseClassName(courseId);
	}

	/**
	 * 更新课程的关联班级名称
	 */
	private void updateCourseClassName(Long courseId) {
		// 查询该课程关联的所有班级
		ChainWrapper<ClassCourse> wrapper = ChainWrapper.create(ClassCourse.class);
		wrapper.where("course_id", "=", courseId);
		List<ClassCourse> relations = classCourseMapper.selectByQuery(wrapper);

		String classNames = "";
		if (relations != null && !relations.isEmpty()) {
			// 获取班级名称列表
			List<String> names = relations.stream()
				.map(r -> {
					Clazz clazz = clazzMapper.selectById(r.getClassId());
					return clazz != null ? clazz.getClassName() : null;
				})
				.filter(name -> name != null)
				.collect(Collectors.toList());
			classNames = String.join(", ", names);
		}

		// 更新课程表
		Course course = new Course();
		course.setId(courseId);
		course.setClassName(classNames);
		courseMapper.updateById(course);
	}

}