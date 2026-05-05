package com.smartmind.biz.bo.dto.discussiontopic;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class DiscussionTopicBaseDto{

	
	public DiscussionTopicBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="话题编码不能为空")
	@jakarta.validation.constraints.NotNull(message="话题编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="话题编码长度不能大于50")
	@Schema(description = "话题编码")
 	private java.lang.String topicCode;
 
	@jakarta.validation.constraints.NotBlank(message="话题名称不能为空")
	@jakarta.validation.constraints.NotNull(message="话题名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="话题名称长度不能大于200")
	@Schema(description = "话题名称")
 	private java.lang.String topicName;
 

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
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="单元编码长度不能大于50")
	@Schema(description = "单元编码")
 	private java.lang.String unitCode;
 

	@jakarta.validation.constraints.NotNull(message="话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴不能为空")

	@Schema(description = "话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴")
 	private java.lang.Byte topicType;
 

	@Schema(description = "预计讨论时长(分钟)")
 	private java.lang.Integer estimatedDuration;
 

	@Schema(description = "建议小组人数")
 	private java.lang.Integer groupSize;
 

	@Schema(description = "使用次数")
 	private java.lang.Integer usageCount;
 
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
 
	@jakarta.validation.constraints.NotBlank(message="话题内容/讨论问题不能为空")
	@jakarta.validation.constraints.NotNull(message="话题内容/讨论问题不能为空")
	@jakarta.validation.constraints.Size(min=0,max=65535,message="话题内容/讨论问题长度不能大于65,535")
	@Schema(description = "话题内容/讨论问题")
 	private java.lang.String topicContent;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="知识点标签列表长度不能大于2,147,483,647")
	@Schema(description = "知识点标签列表")
 	private java.lang.String knowledgePoints;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="背景材料/案例描述长度不能大于65,535")
	@Schema(description = "背景材料/案例描述")
 	private java.lang.String backgroundMaterial;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教师引导提示长度不能大于65,535")
	@Schema(description = "教师引导提示")
 	private java.lang.String guidanceTips;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="预期答案要点长度不能大于65,535")
	@Schema(description = "预期答案要点")
 	private java.lang.String expectedAnswers;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="评价标准长度不能大于65,535")
	@Schema(description = "评价标准")
 	private java.lang.String evaluationCriteria;
 
	

}
