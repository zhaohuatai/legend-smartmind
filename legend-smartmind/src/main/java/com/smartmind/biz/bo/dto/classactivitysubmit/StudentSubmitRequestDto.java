package com.smartmind.biz.bo.dto.classactivitysubmit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StudentSubmitRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	// @Schema(description = "活动ID")
	// private Long activityId;
//
	@Schema(description = "学生ID")
	private String studentId;
	
	@Schema(description = "提交记录ID")
	private Long submitId;
	
	@Schema(description = "提交记录ID")
	private Long detailId;
//	
//	@Schema(description = "题目ID（仅question类型需要）")
//	private Long questionId;

//	@Schema(description = "资源ID")
//	private Long resourceId;
//	class_activity_resource_type	1	题库
//	class_activity_resource_type	2	讨论话题
//	class_activity_resource_type	3	实验指导书
//	@Schema(description = "资源类型: 1-题库, 2-讨论话题, 3-实验指导书")
//	private String resourceType;

	@Schema(description = "学生答案")
	private String answerContent;

	
}
