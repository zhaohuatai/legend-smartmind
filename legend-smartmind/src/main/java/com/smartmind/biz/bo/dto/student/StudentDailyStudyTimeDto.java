package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生每日学习时长Dto")
public class StudentDailyStudyTimeDto {

	@Schema(description = "日期")
	private String date;

	@Schema(description = "星期标签")
	private String label;

	@Schema(description = "学习时长(分钟)")
	private Integer minutes;

	@Schema(description = "柱状图高度")
	private Integer height;
}
