package com.smartmind.biz.bo.dto.activityquestionstatistics;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ActivityQuestionStatisticsBaseDto{

	
	public ActivityQuestionStatisticsBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="提交记录ID不能为空")

	@Schema(description = "提交记录ID")
 	private java.lang.Long submitId;
 

	@jakarta.validation.constraints.NotNull(message="活动ID不能为空")

	@Schema(description = "活动ID")
 	private java.lang.Long activityId;
 

	@jakarta.validation.constraints.NotNull(message="题目ID不能为空")

	@Schema(description = "题目ID")
 	private java.lang.Long questionId;
 

	@Schema(description = "题号")
 	private java.lang.Integer questionSeq;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="是否正确: 0-否, 1-是长度不能大于10")
	@Schema(description = "是否正确: 0-否, 1-是")
 	private java.lang.String isCorrect;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="得分数据精度错误")@Schema(description = "得分")
 	private java.math.BigDecimal score;
 

	@Schema(description = "用时(秒)")
 	private java.lang.Integer timeSpent;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="是否修改过答案: 0-否, 1-是长度不能大于10")
	@Schema(description = "是否修改过答案: 0-否, 1-是")
 	private java.lang.String optionChanged;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="置信度等级: 1-高, 2-中, 3-低长度不能大于10")
	@Schema(description = "置信度等级: 1-高, 2-中, 3-低")
 	private java.lang.String confidenceLevel;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="错误类型长度不能大于50")
	@Schema(description = "错误类型")
 	private java.lang.String commonMistakeType;
 

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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="选择答案长度不能大于1,073,741,824")
	@Schema(description = "选择答案")
 	private java.lang.String answerOptions;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI题目分析长度不能大于65,535")
	@Schema(description = "AI题目分析")
 	private java.lang.String aiAnalysis;
 
	

}
