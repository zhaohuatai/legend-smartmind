package com.smartmind.biz.bo.dto.learningreport;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class LearningReportBaseDto{

	
	public LearningReportBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="报告名称不能为空")
	@jakarta.validation.constraints.NotNull(message="报告名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="报告名称长度不能大于200")
	@Schema(description = "报告名称")
 	private java.lang.String reportName;
 
	@jakarta.validation.constraints.NotBlank(message="报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告不能为空")
	@jakarta.validation.constraints.NotNull(message="报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告长度不能大于10")
	@Schema(description = "报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告")
 	private java.lang.String reportType;
 

	@Schema(description = "活动ID: 单活动/作业报告时关联")
 	private java.lang.Long activityId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="活动名称长度不能大于200")
	@Schema(description = "活动名称")
 	private java.lang.String activityName;
 

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
 

	@jakarta.validation.constraints.NotNull(message="统计开始日期不能为空")

	@Schema(description = "统计开始日期")
 	private java.util.Date startDate;
 

	@jakarta.validation.constraints.NotNull(message="统计结束日期不能为空")

	@Schema(description = "统计结束日期")
 	private java.util.Date endDate;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="参与率(%)数据精度错误")@Schema(description = "参与率(%)")
 	private java.math.BigDecimal participationRate;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="平均分数据精度错误")@Schema(description = "平均分")
 	private java.math.BigDecimal averageScore;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="最高分数据精度错误")@Schema(description = "最高分")
 	private java.math.BigDecimal highestScore;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="最低分数据精度错误")@Schema(description = "最低分")
 	private java.math.BigDecimal lowestScore;
 

	@Schema(description = "排名")
 	private java.lang.Integer rank;
 

	@Schema(description = "活动总数")
 	private java.lang.Integer totalActivities;
 

	@Schema(description = "完成活动数")
 	private java.lang.Integer completedActivities;
 

	@Schema(description = "总题数")
 	private java.lang.Integer totalQuestions;
 

	@Schema(description = "正确题数")
 	private java.lang.Integer correctQuestions;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="总正确率(%)数据精度错误")@Schema(description = "总正确率(%)")
 	private java.math.BigDecimal overallCorrectRate;
 

	@jakarta.validation.constraints.NotNull(message="生成时间不能为空")

	@Schema(description = "生成时间")
 	private java.util.Date generateTime;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-草稿, 1-已生成不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-草稿, 1-已生成不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-草稿, 1-已生成长度不能大于2")
	@Schema(description = "状态: 0-草稿, 1-已生成")
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="优势知识点长度不能大于1,073,741,824")
	@Schema(description = "优势知识点")
 	private java.lang.String strengthPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="薄弱知识点长度不能大于1,073,741,824")
	@Schema(description = "薄弱知识点")
 	private java.lang.String weaknessPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="改进建议长度不能大于65,535")
	@Schema(description = "改进建议")
 	private java.lang.String improvementSuggestions;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="学习趋势数据长度不能大于1,073,741,824")
	@Schema(description = "学习趋势数据")
 	private java.lang.String learningTrend;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="活动详情数据长度不能大于1,073,741,824")
	@Schema(description = "活动详情数据")
 	private java.lang.String activityDetails;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI总结评语长度不能大于65,535")
	@Schema(description = "AI总结评语")
 	private java.lang.String aiSummary;
 
	

}
