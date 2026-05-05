package com.smartmind.biz.bo.dto.question;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class QuestionBaseDto{

	
	public QuestionBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="题目标识码不能为空")
	@jakarta.validation.constraints.NotNull(message="题目标识码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="题目标识码长度不能大于50")
	@Schema(description = "题目标识码")
 	private java.lang.String questionCode;
 
	@jakarta.validation.constraints.NotBlank(message="题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题不能为空")
	@jakarta.validation.constraints.NotNull(message="题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题长度不能大于10")
	@Schema(description = "题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题")
 	private java.lang.String questionType;
 

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
 	
 	@jakarta.validation.constraints.Size(min=0,max=64,message="单元编码长度不能大于64")
 	@Schema(description = "单元编码")
 	private java.lang.String unitCode;
 
	@jakarta.validation.constraints.NotBlank(message="难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难不能为空")
	@jakarta.validation.constraints.NotNull(message="难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难长度不能大于10")
	@Schema(description = "难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难")
 	private java.lang.String difficultyLevel;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="认知层次: 1-识记, 2-理解, 3-应用, 4-分析, 5-综合, 6-评价长度不能大于10")
	@Schema(description = "认知层次: 1-识记, 2-理解, 3-应用, 4-分析, 5-综合, 6-评价")
 	private java.lang.String cognitiveLevel;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="题目分值数据精度错误")@Schema(description = "题目分值")
 	private java.math.BigDecimal score;
 

	@Schema(description = "使用次数")
 	private java.lang.Integer usageCount;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="历史正确率(%)数据精度错误")@Schema(description = "历史正确率(%)")
 	private java.math.BigDecimal correctRate;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="来源类型: 1-系统题库, 2-教师自建, 3-AI生成, 4-导入长度不能大于10")
	@Schema(description = "来源类型: 1-系统题库, 2-教师自建, 3-AI生成, 4-导入")
 	private java.lang.String sourceType;
 
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
 
	@jakarta.validation.constraints.NotBlank(message="题目内容不能为空")
	@jakarta.validation.constraints.NotNull(message="题目内容不能为空")
	@jakarta.validation.constraints.Size(min=0,max=65535,message="题目内容长度不能大于65,535")
	@Schema(description = "题目内容")
 	private java.lang.String questionContent;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识点标签列表长度不能大于1,073,741,824")
	@Schema(description = "知识点标签列表")
 	private java.lang.String knowledgePoints;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="参考答案长度不能大于65,535")
	@Schema(description = "参考答案")
 	private java.lang.String answer;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="答案解析长度不能大于65,535")
	@Schema(description = "答案解析")
 	private java.lang.String answerAnalysis;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="AI分析标签(易错点/考察重点等)长度不能大于1,073,741,824")
	@Schema(description = "AI分析标签(易错点/考察重点等)")
 	private java.lang.String aiAnalysisTags;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="AI推荐相似题长度不能大于1,073,741,824")
	@Schema(description = "AI推荐相似题")
 	private java.lang.String aiSimilarQuestions;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="选项列表JSON结构长度不能大于1,073,741,824")
	@Schema(description = "选项列表JSON结构")
 	private java.lang.String options;
 
	

}
