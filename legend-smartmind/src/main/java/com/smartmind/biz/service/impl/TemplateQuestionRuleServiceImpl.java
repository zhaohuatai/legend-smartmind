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
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleCreateDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleUpdateDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRulePageQueryDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ITemplateQuestionRuleService;
import com.smartmind.biz.dao.TemplateQuestionRuleMapper;
import com.smartmind.biz.bo.model.TemplateQuestionRule;
@Service
public class TemplateQuestionRuleServiceImpl extends BaseServiceImpl<TemplateQuestionRuleMapper, TemplateQuestionRule> implements ITemplateQuestionRuleService{
 	@Autowired
	private TemplateQuestionRuleMapper templateQuestionRuleMapper;
	
	@Override
	public BaseMapper<TemplateQuestionRule> getMapper() {
		return templateQuestionRuleMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createTemplateQuestionRule(TemplateQuestionRuleCreateDto templateQuestionRuleCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(templateQuestionRuleCreateDto);
		TemplateQuestionRule templateQuestionRule = TemplateQuestionRuleConvert.INSTANCE.convert(templateQuestionRuleCreateDto);
		
	      
	   
		return create(templateQuestionRule);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateTemplateQuestionRule(TemplateQuestionRuleUpdateDto templateQuestionRuleCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(templateQuestionRuleCreateDto);
		TemplateQuestionRule templateQuestionRule = TemplateQuestionRuleConvert.INSTANCE.convert(templateQuestionRuleCreateDto);
	
		
		return  updateById(templateQuestionRule);
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