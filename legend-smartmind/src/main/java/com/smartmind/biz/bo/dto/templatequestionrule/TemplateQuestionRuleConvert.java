package com.smartmind.biz.bo.dto.templatequestionrule;
import com.smartmind.biz.bo.model.TemplateQuestionRule;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateQuestionRuleConvert {

    TemplateQuestionRuleConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(TemplateQuestionRuleConvert.class);

    
    TemplateQuestionRule convert(TemplateQuestionRuleCreateDto createDto);

    TemplateQuestionRule convert(TemplateQuestionRuleUpdateDto updateDto);


   
}
