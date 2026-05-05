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
import com.smartmind.biz.bo.dto.signinrecord.SignInRecordCreateDto;
import com.smartmind.biz.bo.dto.signinrecord.SignInRecordUpdateDto;
import com.smartmind.biz.bo.dto.signinrecord.SignInRecordPageQueryDto;
import com.smartmind.biz.bo.dto.signinrecord.SignInRecordConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ISignInRecordService;
import com.smartmind.biz.dao.SignInRecordMapper;
import com.smartmind.biz.bo.model.SignInRecord;
@Service
public class SignInRecordServiceImpl extends BaseServiceImpl<SignInRecordMapper, SignInRecord> implements ISignInRecordService{
 	@Autowired
	private SignInRecordMapper signInRecordMapper;
	
	@Override
	public BaseMapper<SignInRecord> getMapper() {
		return signInRecordMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createSignInRecord(SignInRecordCreateDto signInRecordCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(signInRecordCreateDto);
		SignInRecord signInRecord = SignInRecordConvert.INSTANCE.convert(signInRecordCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "signId","signName"),Ref.newRef("SmdSignInActivity", "id","signName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
	   
		return create(signInRecord);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateSignInRecord(SignInRecordUpdateDto signInRecordCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(signInRecordCreateDto);
		SignInRecord signInRecord = SignInRecordConvert.INSTANCE.convert(signInRecordCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "signId","signName"),Ref.newRef("SmdSignInActivity", "id","signName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( signInRecord, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
	
		
		return  updateById(signInRecord);
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