package com.smartmind.biz.bo.dto.activitysubmitstatistics;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ActivitySubmitStatisticsBaseDto{

	
	public ActivitySubmitStatisticsBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="提交记录ID不能为空")

	@Schema(description = "提交记录ID")
 	private java.lang.Long submitId;
 

	@jakarta.validation.constraints.NotNull(message="活动ID不能为空")

	@Schema(description = "活动ID")
 	private java.lang.Long activityId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="活动名称长度不能大于200")
	@Schema(description = "活动名称")
 	private java.lang.String activityName;
 

	@jakarta.validation.constraints.NotNull(message="学生ID不能为空")

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@Schema(description = "提交次数")
 	private java.lang.Integer submitCount;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="总得分数据精度错误")@Schema(description = "总得分")
 	private java.math.BigDecimal totalScore;
 

	@Schema(description = "正确题数")
 	private java.lang.Integer correctCount;
 

	@Schema(description = "错误题数")
 	private java.lang.Integer wrongCount;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="正确率(%)数据精度错误")@Schema(description = "正确率(%)")
 	private java.math.BigDecimal correctRate;
 

	@Schema(description = "平均每题用时(秒)")
 	private java.lang.Integer averageTimePerQuestion;
 

	@Schema(description = "班级排名")
 	private java.lang.Integer rankInClass;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="排名百分比数据精度错误")@Schema(description = "排名百分比")
 	private java.math.BigDecimal rankPercentile;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="较上次提交进步(%)数据精度错误")@Schema(description = "较上次提交进步(%)")
 	private java.math.BigDecimal comparedToLast;
 

	@jakarta.validation.constraints.NotNull(message="统计时间不能为空")

	@Schema(description = "统计时间")
 	private java.util.Date statisticsTime;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="各题得分分布长度不能大于1,073,741,824")
	@Schema(description = "各题得分分布")
 	private java.lang.String scoreDistribution;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识点掌握情况长度不能大于1,073,741,824")
	@Schema(description = "知识点掌握情况")
 	private java.lang.String knowledgeMastery;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="薄弱点分析长度不能大于1,073,741,824")
	@Schema(description = "薄弱点分析")
 	private java.lang.String weakPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="提升建议长度不能大于65,535")
	@Schema(description = "提升建议")
 	private java.lang.String improvementSuggestions;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI综合分析长度不能大于65,535")
	@Schema(description = "AI综合分析")
 	private java.lang.String aiAnalysis;
 
	

}
