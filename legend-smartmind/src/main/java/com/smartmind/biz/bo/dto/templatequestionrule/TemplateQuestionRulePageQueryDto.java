package com.smartmind.biz.bo.dto.templatequestionrule;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "模板选题规则分页查询DTO")
public class TemplateQuestionRulePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "模板ID")
	private java.lang.Long templateId;
	


}
