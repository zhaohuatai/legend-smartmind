package com.smartmind.biz.bo.dto.classactivityresource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "课堂活动资源批量保存DTO")
public class ClassActivityResourceBatchSaveDto {

	@Schema(description = "课堂活动ID")
	private java.lang.Long activityId;

	@Schema(description = "资源类型: question-题库, discussion-讨论话题, experiment-实验指导书")
	private java.lang.String resourceType;

	@Schema(description = "资源ID列表")
	private java.util.List<java.lang.Long> resourceIds;

	@Schema(description = "资源分数(题库为每题分数,其他为总分)")
	private java.math.BigDecimal score;
}
