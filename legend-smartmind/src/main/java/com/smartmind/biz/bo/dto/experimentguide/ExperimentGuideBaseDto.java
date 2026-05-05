package com.smartmind.biz.bo.dto.experimentguide;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ExperimentGuideBaseDto{

	
	public ExperimentGuideBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="指导书编码不能为空")
	@jakarta.validation.constraints.NotNull(message="指导书编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="指导书编码长度不能大于50")
	@Schema(description = "指导书编码")
 	private java.lang.String guideCode;
 
	@jakarta.validation.constraints.NotBlank(message="指导书名称不能为空")
	@jakarta.validation.constraints.NotNull(message="指导书名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="指导书名称长度不能大于200")
	@Schema(description = "指导书名称")
 	private java.lang.String guideName;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="单元ID不能为空")

	@Schema(description = "单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="单元编码长度不能大于50")
	@Schema(description = "单元编码")
 	private java.lang.String unitCode;
 

	@jakarta.validation.constraints.NotNull(message="第几次实验不能为空")

	@Schema(description = "第几次实验")
 	private java.lang.Integer experimentSession;
 

	@Schema(description = "预计课时数（学时）")
 	private java.lang.Integer estimatedHours;
 

	@Schema(description = "AI生成模式")
 	private java.lang.Byte aiGenerateMode;
 
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=1,message="状态长度不能大于1")
	@Schema(description = "状态")
 	private java.lang.String status;
 

	@Schema(description = "版本号")
 	private java.lang.Integer version;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=64,message="创建人长度不能大于64")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=64,message="更新人长度不能大于64")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="实验目的长度不能大于65,535")
	@Schema(description = "实验目的")
 	private java.lang.String experimentObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="实验原理长度不能大于65,535")
	@Schema(description = "实验原理")
 	private java.lang.String experimentPrinciple;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="实验器材/设备长度不能大于65,535")
	@Schema(description = "实验器材/设备")
 	private java.lang.String experimentEquipment;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="实验步骤长度不能大于65,535")
	@Schema(description = "实验步骤")
 	private java.lang.String experimentSteps;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="注意事项长度不能大于65,535")
	@Schema(description = "注意事项")
 	private java.lang.String precautions;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="实验报告要求长度不能大于65,535")
	@Schema(description = "实验报告要求")
 	private java.lang.String reportRequirements;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="实验指导书完整内容长度不能大于2,147,483,647")
	@Schema(description = "实验指导书完整内容")
 	private java.lang.String markdownContent;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI生成提示词长度不能大于65,535")
	@Schema(description = "AI生成提示词")
 	private java.lang.String aiPrompt;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI优化建议长度不能大于65,535")
	@Schema(description = "AI优化建议")
 	private java.lang.String aiSuggestions;
 
	

}
