package com.smartmind.biz.bo.dto.classactivityresource;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassActivityResourceBaseDto{

	
	public ClassActivityResourceBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="课堂活动ID不能为空")

	@Schema(description = "课堂活动ID")
 	private java.lang.Long activityId;
 

	@jakarta.validation.constraints.NotBlank(message="资源类型不能为空")
	@jakarta.validation.constraints.Size(min=1,max=20,message="资源类型长度不能大于20")
	@Schema(description = "资源类型: question-题库, discussion-讨论话题, experiment-实验指导书")
 	private java.lang.String resourceType;
 

	@jakarta.validation.constraints.NotNull(message="资源ID不能为空")

	@Schema(description = "资源ID")
 	private java.lang.Long resourceId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="资源名称长度不能大于200")
	@Schema(description = "资源名称")
 	private java.lang.String resourceName;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="资源分数(题库为每题分数,其他为总分)数据精度错误")@Schema(description = "资源分数(题库为每题分数,其他为总分)")
 	private java.math.BigDecimal score;
 

	@Schema(description = "排序")
 	private java.lang.Integer sortOrder;
 

 
	

}
