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
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentCreateDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentUpdateDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentPageQueryDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassStudentService;
import com.smartmind.biz.dao.ClassStudentMapper;
import com.smartmind.biz.bo.model.ClassStudent;
@Service
public class ClassStudentServiceImpl extends BaseServiceImpl<ClassStudentMapper, ClassStudent> implements IClassStudentService{
 	@Autowired
	private ClassStudentMapper classStudentMapper;
	
	@Override
	public BaseMapper<ClassStudent> getMapper() {
		return classStudentMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassStudent(ClassStudentCreateDto classStudentCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classStudentCreateDto);
		ClassStudent classStudent = ClassStudentConvert.INSTANCE.convert(classStudentCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	   
		return create(classStudent);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassStudent(ClassStudentUpdateDto classStudentCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classStudentCreateDto);
		ClassStudent classStudent = ClassStudentConvert.INSTANCE.convert(classStudentCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( classStudent, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	
		
		return  updateById(classStudent);
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
	
}