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
import com.smartmind.biz.bo.dto.teacher.TeacherCreateDto;
import com.smartmind.biz.bo.dto.teacher.TeacherUpdateDto;
import com.smartmind.biz.bo.dto.teacher.TeacherPageQueryDto;
import com.smartmind.biz.bo.dto.teacher.TeacherConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ITeacherService;
import com.smartmind.biz.dao.TeacherMapper;
import com.smartmind.biz.bo.model.Teacher;
import org.legend.framework.bss.biz.service.impl.AuthUserServiceImpl;
import org.legend.framework.bss.dto.user.AccountDto;
import org.legend.framework.bss.util.PasswordUtil;
import org.legend.framework.core.util.AppCtxUtil;
@Service
public class TeacherServiceImpl extends BaseServiceImpl<TeacherMapper, Teacher> implements ITeacherService{
 	@Autowired
	private TeacherMapper teacherMapper;
	
	@Override
	public BaseMapper<Teacher> getMapper() {
		return teacherMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createTeacher(TeacherCreateDto teacherCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teacherCreateDto);
		
		// 先调用createAccount创建账号，默认密码为工号
		String password = ZStrUtil.isNotBlank(teacherCreateDto.getPassword()) ? teacherCreateDto.getPassword() : teacherCreateDto.getTeacherNo();
		password = PasswordUtil.getPasswordEncoder().genFrontPassword(password);
		AccountDto accountDto = new AccountDto(teacherCreateDto.getTeacherNo(),password,"teacher");
		accountDto.setTenantId("1");
		accountDto.setPerName(teacherCreateDto.getTeacherName());
		accountDto.setPhone(teacherCreateDto.getPhone());
		accountDto.setEmail(teacherCreateDto.getEmail());
		accountDto.setGender(teacherCreateDto.getGender());
		
		AuthUserServiceImpl authUserService = AppCtxUtil.cfg.getBean(AuthUserServiceImpl.class);
		String userId = authUserService.createAccount(simpleUser.getTenantId(), accountDto);
		
		// 创建教师信息
		Teacher teacher = TeacherConvert.INSTANCE.convert(teacherCreateDto);
		teacher.setId(userId);
	   
		return create(teacher);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateTeacher(TeacherUpdateDto teacherCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teacherCreateDto);
		Teacher teacher = TeacherConvert.INSTANCE.convert(teacherCreateDto);
	
		
		return  updateById(teacher);
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