package com.smartmind.biz.bo.dto.templatequestionrule;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class TemplateQuestionRuleBaseDto{

	
	public TemplateQuestionRuleBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="模板ID不能为空")

	@Schema(description = "模板ID")
 	private java.lang.Long templateId;
 
	@jakarta.validation.constraints.NotBlank(message="规则名称不能为空")
	@jakarta.validation.constraints.NotNull(message="规则名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="规则名称长度不能大于100")
	@Schema(description = "规则名称")
 	private java.lang.String ruleName;
 
	@jakarta.validation.constraints.NotBlank(message="题目类型不能为空")
	@jakarta.validation.constraints.NotNull(message="题目类型不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="题目类型长度不能大于10")
	@Schema(description = "题目类型")
 	private java.lang.String questionType;
 

	@jakarta.validation.constraints.NotNull(message="题目数量不能为空")

	@Schema(description = "题目数量")
 	private java.lang.Integer questionCount;
 

	@jakarta.validation.constraints.NotNull(message="每题分值不能为空")

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="每题分值数据精度错误")@Schema(description = "每题分值")
 	private java.math.BigDecimal scorePerQuestion;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="难度范围(如: 1-3)长度不能大于50")
	@Schema(description = "难度范围(如: 1-3)")
 	private java.lang.String difficultyRange;
 

	@Schema(description = "排序序号")
 	private java.lang.Integer sortOrder;
 
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态长度不能大于2")
	@Schema(description = "状态")
 	private java.lang.String status;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识点标签限制长度不能大于1,073,741,824")
	@Schema(description = "知识点标签限制")
 	private java.lang.String knowledgeTags;
 
	

}
