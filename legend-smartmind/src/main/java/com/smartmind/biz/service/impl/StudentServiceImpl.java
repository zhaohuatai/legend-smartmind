package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;

import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.student.StudentCreateDto;
import com.smartmind.biz.bo.dto.student.StudentUpdateDto;
import com.smartmind.biz.bo.dto.student.StudentPageQueryDto;
import com.smartmind.biz.bo.dto.student.StudentConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IStudentService;
import com.smartmind.biz.service.IClazzService;
import com.smartmind.biz.dao.StudentMapper;
import com.smartmind.biz.dao.ClassStudentMapper;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.bo.model.Clazz;

import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.bss.biz.service.impl.AuthUserServiceImpl;
import org.legend.framework.bss.dto.user.AccountDto;
import org.legend.framework.bss.util.PasswordUtil;
import org.legend.framework.core.util.AppCtxUtil;
@Service
public class StudentServiceImpl extends BaseServiceImpl<StudentMapper, Student> implements IStudentService{
 	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private ClassStudentMapper classStudentMapper;

	@Autowired
	private IClazzService clazzService;
	
	@Override
	public BaseMapper<Student> getMapper() {
		return studentMapper;
	}
	
	/**
	 * 保存学生班级关联
	 */
	private void saveStudentClassRelation(String studentId,String studentName, Long classId) {
		// 删除旧的关联
		ChainWrapper<ClassStudent> deleteWrapper = ChainWrapper.create(ClassStudent.class);
		deleteWrapper.where("student_id", "=", studentId);
		classStudentMapper.deleteByQuery(deleteWrapper);

		
		// 保存新的关联
		if (classId != null) {
			ClassStudent relation = new ClassStudent();
			relation.setStudentId(studentId);
			relation.setClassId(classId);
			relation.setStudentName(studentName);
			relation.setStatus("1");
			clazzService.loadById(classId).ifPresent(c->relation.setClassName(studentName));
			classStudentMapper.insertSelective(relation);
		}
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public  int batchCreate(List<StudentCreateDto> students){
		if (students == null || students.isEmpty()) {
			ZAlert.throwSLE("学生列表不能为空");
		}
		// 批量创建学生
		for (StudentCreateDto dto : students) {
			ZBeanUtil.validateBean(dto);
			createStudent(dto, SecurityUtil.getSimpleUser());
		}
		return students.size();
    }
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createStudent(StudentCreateDto studentCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(studentCreateDto);

		// 先调用createAccount创建账号，默认密码为学号
		String password = ZStrUtil.isNotBlank(studentCreateDto.getPassword()) ? studentCreateDto.getPassword() : studentCreateDto.getStudentNo();
		password = PasswordUtil.getPasswordEncoder().genFrontPassword(password);
		AccountDto accountDto = new AccountDto(studentCreateDto.getStudentNo(),password,"student");
		
		accountDto.setTenantId("1");
		accountDto.setPerName(studentCreateDto.getStudentName());
		accountDto.setPhone(studentCreateDto.getPhone());
		accountDto.setEmail(studentCreateDto.getEmail());
		accountDto.setGender(studentCreateDto.getGender());
		
		AuthUserServiceImpl authUserService = AppCtxUtil.cfg.getBean(AuthUserServiceImpl.class);
		String userId = authUserService.createAccount(simpleUser.getTenantId(), accountDto);

		// 创建学生信息
		Student student = StudentConvert.INSTANCE.convert(studentCreateDto);
		student.setStudentName(studentCreateDto.getStudentName());
		student.setStudentNo(studentCreateDto.getStudentNo());
		student.setGender(student.getGender());
		student.setId(userId);

		// 保存学生
		int result = create(student);

		// 保存班级关联
		if (studentCreateDto.getClassId() != null && student.getId() != null) {
			saveStudentClassRelation(student.getId(), studentCreateDto.getStudentName(),studentCreateDto.getClassId());
			// 更新班级学生数量
			clazzService.updateClassStudentCount(studentCreateDto.getClassId());
		}

		return result;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateStudent(StudentUpdateDto studentUpdateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(studentUpdateDto);
		Student student = StudentConvert.INSTANCE.convert(studentUpdateDto);

		// 获取旧班级ID（用于后续更新学生数）
		Long oldClassId = null;
		if (studentUpdateDto.getId() != null) {
			ChainWrapper<ClassStudent> queryWrapper = ChainWrapper.create(ClassStudent.class);
			queryWrapper.where("student_id", "=", studentUpdateDto.getId());
			queryWrapper.where("status", "=", "1");
			List<ClassStudent> oldRelations = classStudentMapper.selectByQuery(queryWrapper);
			if (!oldRelations.isEmpty()) {
				oldClassId = oldRelations.get(0).getClassId();
			}
		}
		student.setStudentNo(null);//学号不能修改
		// 更新学生
		int result = updateById(student);

		// 更新班级关联
		if (studentUpdateDto.getId() != null) {
			saveStudentClassRelation(studentUpdateDto.getId(),studentUpdateDto.getStudentName(), studentUpdateDto.getClassId());
			// 更新新班级学生数量
			if (studentUpdateDto.getClassId() != null) {
				clazzService.updateClassStudentCount(studentUpdateDto.getClassId());
			}
			// 更新旧班级学生数量（如果班级发生变化）
			if (oldClassId != null && !oldClassId.equals(studentUpdateDto.getClassId())) {
				clazzService.updateClassStudentCount(oldClassId);
			}
		}

		return result;
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
	public void updateStudentInfo(String studentId, String phone, String email, String photoUrl) {
		if (studentId == null) {
			return;
		}
		ChainWrapper<Student> qw = ChainWrapper.create(Student.class).where("id", "=", studentId);
		qw.set("phone", phone).set("email", email).set("photo_url", photoUrl);
		studentMapper.updateByQuery(qw);
	}
	
}
