package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.TemplateQuestionRule;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleCreateDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleUpdateDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRulePageQueryDto;
public interface ITemplateQuestionRuleService extends IBaseService<TemplateQuestionRule>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createTemplateQuestionRule(TemplateQuestionRuleCreateDto templateQuestionRuleCreateVo,SimpleUserBo simpleUser);

	int updateTemplateQuestionRule(TemplateQuestionRuleUpdateDto templateQuestionRuleUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}