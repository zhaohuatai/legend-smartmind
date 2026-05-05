package com.smartmind.biz.bo.dto.abilitytransition;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class AbilityTransitionBaseDto{

	
	public AbilityTransitionBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="学生ID不能为空")

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学号长度不能大于50")
	@Schema(description = "学号")
 	private java.lang.String studentNo;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="起始单元ID不能为空")

	@Schema(description = "起始单元ID")
 	private java.lang.Long fromUnitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="起始单元名称长度不能大于100")
	@Schema(description = "起始单元名称")
 	private java.lang.String fromUnitName;
 

	@jakarta.validation.constraints.NotNull(message="目标单元ID不能为空")

	@Schema(description = "目标单元ID")
 	private java.lang.Long toUnitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="目标单元名称长度不能大于100")
	@Schema(description = "目标单元名称")
 	private java.lang.String toUnitName;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="起始分数数据精度错误")@Schema(description = "起始分数")
 	private java.math.BigDecimal fromScore;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="目标分数数据精度错误")@Schema(description = "目标分数")
 	private java.math.BigDecimal toScore;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="分数变化数据精度错误")@Schema(description = "分数变化")
 	private java.math.BigDecimal scoreChange;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="变化率(%)数据精度错误")@Schema(description = "变化率(%)")
 	private java.math.BigDecimal changeRate;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="跃迁类型: 1-进步, 2-退步, 3-稳定长度不能大于10")
	@Schema(description = "跃迁类型: 1-进步, 2-退步, 3-稳定")
 	private java.lang.String transitionType;
 

	@jakarta.validation.constraints.NotNull(message="分析时间不能为空")

	@Schema(description = "分析时间")
 	private java.util.Date analysisTime;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="能力维度变化长度不能大于1,073,741,824")
	@Schema(description = "能力维度变化")
 	private java.lang.String abilityDimensions;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="可视化数据长度不能大于1,073,741,824")
	@Schema(description = "可视化数据")
 	private java.lang.String visualizationData;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI分析评语长度不能大于65,535")
	@Schema(description = "AI分析评语")
 	private java.lang.String aiAnalysis;
 
	

}
