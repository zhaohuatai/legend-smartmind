package com.smartmind.biz.bo.dto.standardachievement;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class StandardAchievementBaseDto{

	
	public StandardAchievementBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@Schema(description = "单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 

	@jakarta.validation.constraints.NotNull(message="课标条目ID不能为空")

	@Schema(description = "课标条目ID")
 	private java.lang.Long standardId;
 
	@jakarta.validation.constraints.NotBlank(message="知识点不能为空")
	@jakarta.validation.constraints.NotNull(message="知识点不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="知识点长度不能大于200")
	@Schema(description = "知识点")
 	private java.lang.String knowledgePoint;
 
	@jakarta.validation.constraints.NotBlank(message="要求层级: 1-了解, 2-理解, 3-掌握, 4-应用不能为空")
	@jakarta.validation.constraints.NotNull(message="要求层级: 1-了解, 2-理解, 3-掌握, 4-应用不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="要求层级: 1-了解, 2-理解, 3-掌握, 4-应用长度不能大于10")
	@Schema(description = "要求层级: 1-了解, 2-理解, 3-掌握, 4-应用")
 	private java.lang.String requirementLevel;
 

	@Schema(description = "被测次数")
 	private java.lang.Integer testedCount;
 

	@Schema(description = "正确次数")
 	private java.lang.Integer correctCount;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="达成率(%)数据精度错误")@Schema(description = "达成率(%)")
 	private java.math.BigDecimal achievementRate;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="是否达成: 0-否, 1-是长度不能大于10")
	@Schema(description = "是否达成: 0-否, 1-是")
 	private java.lang.String achieved;
 

	@jakarta.validation.constraints.NotNull(message="统计时间不能为空")

	@Schema(description = "统计时间")
 	private java.util.Date statisticsTime;
 
	@jakarta.validation.constraints.NotBlank(message="计算类型: 1-班级整体, 2-个人不能为空")
	@jakarta.validation.constraints.NotNull(message="计算类型: 1-班级整体, 2-个人不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="计算类型: 1-班级整体, 2-个人长度不能大于10")
	@Schema(description = "计算类型: 1-班级整体, 2-个人")
 	private java.lang.String calculationType;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="差距分析长度不能大于65,535")
	@Schema(description = "差距分析")
 	private java.lang.String gapAnalysis;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="提升计划长度不能大于65,535")
	@Schema(description = "提升计划")
 	private java.lang.String improvementPlan;
 
	

}
