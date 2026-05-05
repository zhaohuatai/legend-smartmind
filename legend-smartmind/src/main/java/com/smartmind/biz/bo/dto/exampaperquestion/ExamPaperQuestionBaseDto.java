package com.smartmind.biz.bo.dto.exampaperquestion;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ExamPaperQuestionBaseDto{

	
	public ExamPaperQuestionBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="试卷ID不能为空")

	@Schema(description = "试卷ID")
 	private java.lang.Long paperId;
 

	@jakarta.validation.constraints.NotNull(message="题目ID不能为空")

	@Schema(description = "题目ID")
 	private java.lang.Long questionId;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="题目类型长度不能大于10")
	@Schema(description = "题目类型")
 	private java.lang.String questionType;
 

	@jakarta.validation.constraints.NotNull(message="本题分值不能为空")

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="本题分值数据精度错误")@Schema(description = "本题分值")
 	private java.math.BigDecimal score;
 

	@jakarta.validation.constraints.NotNull(message="题目序号不能为空")

	@Schema(description = "题目序号")
 	private java.lang.Integer sortOrder;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="所属板块长度不能大于50")
	@Schema(description = "所属板块")
 	private java.lang.String sectionName;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="题目内容(冗余存储)长度不能大于65,535")
	@Schema(description = "题目内容(冗余存储)")
 	private java.lang.String questionContent;
 
	

}
