package com.smartmind.biz.bo.dto.papertemplate;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class PaperTemplateBaseDto{

	
	public PaperTemplateBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="模板编码不能为空")
	@jakarta.validation.constraints.NotNull(message="模板编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="模板编码长度不能大于50")
	@Schema(description = "模板编码")
 	private java.lang.String templateCode;
 
	@jakarta.validation.constraints.NotBlank(message="模板名称不能为空")
	@jakarta.validation.constraints.NotNull(message="模板名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="模板名称长度不能大于200")
	@Schema(description = "模板名称")
 	private java.lang.String templateName;
 

	@jakarta.validation.constraints.NotNull(message="所属课程ID不能为空")

	@Schema(description = "所属课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 
	@jakarta.validation.constraints.NotBlank(message="试卷类型不能为空")
	@jakarta.validation.constraints.NotNull(message="试卷类型不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="试卷类型长度不能大于10")
	@Schema(description = "试卷类型")
 	private java.lang.String paperType;
 

	@jakarta.validation.constraints.NotNull(message="总分值不能为空")

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="总分值数据精度错误")@Schema(description = "总分值")
 	private java.math.BigDecimal totalScore;
 

	@jakarta.validation.constraints.NotNull(message="题目数量不能为空")

	@Schema(description = "题目数量")
 	private java.lang.Integer questionCount;
 

	@Schema(description = "考试时长(分钟)")
 	private java.lang.Integer duration;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="难度偏好长度不能大于10")
	@Schema(description = "难度偏好")
 	private java.lang.String difficultyPreference;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="模板描述长度不能大于65,535")
	@Schema(description = "模板描述")
 	private java.lang.String templateDesc;
 
	

}
