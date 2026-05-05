package com.smartmind.biz.bo.dto.classactivitysubmitdetail;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassActivitySubmitDetailBaseDto{

	
	public ClassActivitySubmitDetailBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="提交记录ID不能为空")

	@Schema(description = "提交记录ID")
 	private java.lang.Long submitId;
 

	@jakarta.validation.constraints.NotNull(message="课堂活动ID不能为空")

	@Schema(description = "课堂活动ID")
 	private java.lang.Long activityId;
 

	@jakarta.validation.constraints.NotNull(message="资源ID不能为空")

	@Schema(description = "资源ID")
 	private java.lang.Long resourceId;
 

	@jakarta.validation.constraints.NotBlank(message="资源类型不能为空")
	@jakarta.validation.constraints.Size(min=1,max=20,message="资源类型长度不能大于20")
	@Schema(description = "资源类型: question-题库, discussion-讨论话题, experiment-实验指导书")
 	private java.lang.String resourceType;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="资源名称长度不能大于200")
	@Schema(description = "资源名称")
 	private java.lang.String resourceName;
 
	@jakarta.validation.constraints.Size(min=0,max=2,message="是否正确: 0-错误, 1-正确, NULL-不适用长度不能大于2")
	@Schema(description = "是否正确: 0-错误, 1-正确, NULL-不适用")
 	private java.lang.String isCorrect;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="得分数据精度错误")@Schema(description = "得分")
 	private java.math.BigDecimal score;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="满分数据精度错误")@Schema(description = "满分")
 	private java.math.BigDecimal fullScore;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-未作答, 1-已作答, 2-已评分不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-未作答, 1-已作答, 2-已评分不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-未作答, 1-已作答, 2-已评分长度不能大于2")
	@Schema(description = "状态: 0-未作答, 1-已作答, 2-已评分")
 	private java.lang.String status;
 

	@Schema(description = "作答时间")
 	private java.util.Date submitTime;
 

	@Schema(description = "评分时间")
 	private java.util.Date gradeTime;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="评分人长度不能大于40")
	@Schema(description = "评分人")
 	private java.lang.String gradeBy;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="答案内容长度不能大于65,535")
	@Schema(description = "答案内容")
 	private java.lang.String answerContent;
 
	

}
