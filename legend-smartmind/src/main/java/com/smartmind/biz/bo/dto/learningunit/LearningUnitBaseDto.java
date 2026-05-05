package com.smartmind.biz.bo.dto.learningunit;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class LearningUnitBaseDto{

	
	public LearningUnitBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="单元编码不能为空")
	@jakarta.validation.constraints.NotNull(message="单元编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="单元编码长度不能大于50")
	@Schema(description = "单元编码")
 	private java.lang.String unitCode;
 
	@jakarta.validation.constraints.NotBlank(message="单元名称不能为空")
	@jakarta.validation.constraints.NotNull(message="单元名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 

	@jakarta.validation.constraints.NotNull(message="所属课程ID不能为空")

	@Schema(description = "所属课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@Schema(description = "父单元ID(0表示根单元)")
 	private java.lang.Long parentId;
 
	@jakarta.validation.constraints.NotBlank(message="单元层级: 1-大单元, 2-子单元, 3-课时不能为空")
	@jakarta.validation.constraints.NotNull(message="单元层级: 1-大单元, 2-子单元, 3-课时不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="单元层级: 1-大单元, 2-子单元, 3-课时长度不能大于10")
	@Schema(description = "单元层级: 1-大单元, 2-子单元, 3-课时")
 	private java.lang.String unitLevel;
 

	@Schema(description = "排序序号")
 	private java.lang.Integer sortOrder;
 

	@Schema(description = "预计课时数")
 	private java.lang.Integer estimatedHours;
 

	@Schema(description = "计划开始日期")
 	private java.util.Date startDate;
 

	@Schema(description = "计划结束日期")
 	private java.util.Date endDate;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-失效, 1-有效长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效")
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="单元描述长度不能大于65,535")
	@Schema(description = "单元描述")
 	private java.lang.String unitDesc;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="单元目标(知识、能力、素养)长度不能大于65,535")
	@Schema(description = "单元目标(知识、能力、素养)")
 	private java.lang.String unitObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="重难点分析长度不能大于65,535")
	@Schema(description = "重难点分析")
 	private java.lang.String keyPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识点标签列表长度不能大于1,073,741,824")
	@Schema(description = "知识点标签列表")
 	private java.lang.String knowledgeTags;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="课标对应关系长度不能大于1,073,741,824")
	@Schema(description = "课标对应关系")
 	private java.lang.String standardMapping;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="前置单元ID列表长度不能大于1,073,741,824")
	@Schema(description = "前置单元ID列表")
 	private java.lang.String prerequisiteUnits;
 
	

}
