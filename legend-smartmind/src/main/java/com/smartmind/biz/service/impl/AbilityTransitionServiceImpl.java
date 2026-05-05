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
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionCreateDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionUpdateDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionPageQueryDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IAbilityTransitionService;
import com.smartmind.biz.dao.AbilityTransitionMapper;
import com.smartmind.biz.bo.model.AbilityTransition;
@Service
public class AbilityTransitionServiceImpl extends BaseServiceImpl<AbilityTransitionMapper, AbilityTransition> implements IAbilityTransitionService{
 	@Autowired
	private AbilityTransitionMapper abilityTransitionMapper;
	
	@Override
	public BaseMapper<AbilityTransition> getMapper() {
		return abilityTransitionMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createAbilityTransition(AbilityTransitionCreateDto abilityTransitionCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(abilityTransitionCreateDto);
		AbilityTransition abilityTransition = AbilityTransitionConvert.INSTANCE.convert(abilityTransitionCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "fromUnitId","fromUnitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "toUnitId","toUnitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	   
		return create(abilityTransition);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateAbilityTransition(AbilityTransitionUpdateDto abilityTransitionCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(abilityTransitionCreateDto);
		AbilityTransition abilityTransition = AbilityTransitionConvert.INSTANCE.convert(abilityTransitionCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "studentId","studentNo"),Ref.newRef("SmdStudent", "id","studentNo"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "fromUnitId","fromUnitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( abilityTransition, "toUnitId","toUnitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	
		
		return  updateById(abilityTransition);
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