package com.smartmind.biz.bo.dto.classactivitysubmit;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassActivitySubmitBaseDto{

	
	public ClassActivitySubmitBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="提交编码不能为空")
	@jakarta.validation.constraints.NotNull(message="提交编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="提交编码长度不能大于50")
	@Schema(description = "提交编码")
 	private java.lang.String submitCode;
 

	@jakarta.validation.constraints.NotNull(message="课堂活动ID不能为空")

	@Schema(description = "课堂活动ID")
 	private java.lang.Long activityId;
 

	@jakarta.validation.constraints.NotNull(message="课堂ID不能为空")

	@Schema(description = "课堂ID")
 	private java.lang.Long sessionId;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")

	@Schema(description = "班级ID")
 	private java.lang.Long clazzId;
 

	@jakarta.validation.constraints.NotNull(message="学生ID不能为空")

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="总分数据精度错误")
	@Schema(description = "总分")
 	private java.math.BigDecimal totalScore;
 
	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="当前得分数据精度错误")
	@Schema(description = "当前得分")
 	private java.math.BigDecimal score;
 
	
	@Schema(description = "做题数量")
 	private java.lang.Integer submitCount;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-未提交, 1-已提交, 2-已评分不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-未提交, 1-已提交, 2-已评分不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-未提交, 1-已提交, 2-已评分长度不能大于2")
	@Schema(description = "状态: 0-未提交, 1-已提交, 2-已评分")
 	private java.lang.String status;
 

	@Schema(description = "提交时间")
 	private java.util.Date submitTime;
 

	@Schema(description = "评分时间")
 	private java.util.Date gradeTime;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="评分人长度不能大于40")
	@Schema(description = "评分人")
 	private java.lang.String gradeBy;
 

 

 
	

}
