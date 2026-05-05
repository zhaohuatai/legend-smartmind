package com.smartmind.biz.bo.dto.knowledgeheatmap;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class KnowledgeHeatmapBaseDto{

	
	public KnowledgeHeatmapBaseDto() {}
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
 
	@jakarta.validation.constraints.NotBlank(message="知识点标签不能为空")
	@jakarta.validation.constraints.NotNull(message="知识点标签不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="知识点标签长度不能大于100")
	@Schema(description = "知识点标签")
 	private java.lang.String knowledgeTag;
 

	@Schema(description = "题目数量")
 	private java.lang.Integer questionCount;
 

	@Schema(description = "正确题数")
 	private java.lang.Integer correctCount;
 

	@Schema(description = "错误题数")
 	private java.lang.Integer wrongCount;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="正确率(%)数据精度错误")@Schema(description = "正确率(%)")
 	private java.math.BigDecimal correctRate;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="掌握程度: 1-薄弱, 2-一般, 3-良好, 4-优秀长度不能大于10")
	@Schema(description = "掌握程度: 1-薄弱, 2-一般, 3-良好, 4-优秀")
 	private java.lang.String masteryLevel;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="热力值(0-100)数据精度错误")@Schema(description = "热力值(0-100)")
 	private java.math.BigDecimal heatScore;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="趋势: up-上升, down-下降, stable-稳定长度不能大于20")
	@Schema(description = "趋势: up-上升, down-下降, stable-稳定")
 	private java.lang.String trend;
 

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
 
	

}
