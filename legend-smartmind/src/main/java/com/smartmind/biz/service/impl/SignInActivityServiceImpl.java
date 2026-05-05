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
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityCreateDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityUpdateDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityPageQueryDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ISignInActivityService;
import com.smartmind.biz.dao.SignInActivityMapper;
import com.smartmind.biz.bo.model.SignInActivity;
@Service
public class SignInActivityServiceImpl extends BaseServiceImpl<SignInActivityMapper, SignInActivity> implements ISignInActivityService{
 	@Autowired
	private SignInActivityMapper signInActivityMapper;
	
	@Override
	public BaseMapper<SignInActivity> getMapper() {
		return signInActivityMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createSignInActivity(SignInActivityCreateDto signInActivityCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(signInActivityCreateDto);
		SignInActivity signInActivity = SignInActivityConvert.INSTANCE.convert(signInActivityCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "teacherId","teacherName"),Ref.newRef("SmdTeacher", "id","teacherName"));
	   
		return create(signInActivity);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateSignInActivity(SignInActivityUpdateDto signInActivityCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(signInActivityCreateDto);
		SignInActivity signInActivity = SignInActivityConvert.INSTANCE.convert(signInActivityCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInActivity, "teacherId","teacherName"),Ref.newRef("SmdTeacher", "id","teacherName"));
	
		
		return  updateById(signInActivity);
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