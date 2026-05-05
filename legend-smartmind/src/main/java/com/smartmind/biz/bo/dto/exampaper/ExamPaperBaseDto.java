package com.smartmind.biz.bo.dto.exampaper;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ExamPaperBaseDto{

	
	public ExamPaperBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="试卷编码不能为空")
	@jakarta.validation.constraints.NotNull(message="试卷编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="试卷编码长度不能大于50")
	@Schema(description = "试卷编码")
 	private java.lang.String paperCode;
 
	@jakarta.validation.constraints.NotBlank(message="试卷名称不能为空")
	@jakarta.validation.constraints.NotNull(message="试卷名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="试卷名称长度不能大于200")
	@Schema(description = "试卷名称")
 	private java.lang.String paperName;
 
	@jakarta.validation.constraints.NotBlank(message="试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他不能为空")
	@jakarta.validation.constraints.NotNull(message="试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他长度不能大于10")
	@Schema(description = "试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他")
 	private java.lang.String paperType;
 

	@jakarta.validation.constraints.NotNull(message="所属课程ID不能为空")

	@Schema(description = "所属课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@Schema(description = "所属单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 

	@Schema(description = "关联教案ID")
 	private java.lang.Long planId;
 

	@jakarta.validation.constraints.NotNull(message="总分值不能为空")

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="总分值数据精度错误")@Schema(description = "总分值")
 	private java.math.BigDecimal totalScore;
 

	@jakarta.validation.constraints.NotNull(message="题目数量不能为空")

	@Schema(description = "题目数量")
 	private java.lang.Integer questionCount;
 

	@Schema(description = "考试时长(分钟)")
 	private java.lang.Integer duration;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="AI生成模式长度不能大于10")
	@Schema(description = "AI生成模式")
 	private java.lang.String aiGenerateMode;
 

	@Schema(description = "使用次数")
 	private java.lang.Integer usageCount;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="历史平均分数据精度错误")@Schema(description = "历史平均分")
 	private java.math.BigDecimal averageScore;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="来源类型长度不能大于10")
	@Schema(description = "来源类型")
 	private java.lang.String sourceType;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-草稿, 1-已发布, 2-已归档不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-草稿, 1-已发布, 2-已归档不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-草稿, 1-已发布, 2-已归档长度不能大于2")
	@Schema(description = "状态: 0-草稿, 1-已发布, 2-已归档")
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="难度分布设置长度不能大于1,073,741,824")
	@Schema(description = "难度分布设置")
 	private java.lang.String difficultyDistribution;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识点覆盖范围长度不能大于1,073,741,824")
	@Schema(description = "知识点覆盖范围")
 	private java.lang.String knowledgeCoverage;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI组卷提示词长度不能大于65,535")
	@Schema(description = "AI组卷提示词")
 	private java.lang.String aiPrompt;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI试卷评估长度不能大于65,535")
	@Schema(description = "AI试卷评估")
 	private java.lang.String aiEvaluation;
 
	

}
