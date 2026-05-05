package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生学习统计概览Dto")
public class StudentStatsOverviewDto {

	@Schema(description = "在学课程数")
	private Integer totalCourses;

	@Schema(description = "参与活动数")
	private Integer totalActivities;

	@Schema(description = "已完成活动数")
	private Integer completedActivities;

	@Schema(description = "平均得分")
	private java.math.BigDecimal avgScore;

	@Schema(description = "完成率")
	private Integer completionRate;

	@Schema(description = "正确率")
	private Integer accuracyRate;

	@Schema(description = "综合得分")
	private Integer overallScore;
}
