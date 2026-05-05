package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生课程完成度Dto")
public class StudentCourseProgressDto {

	@Schema(description = "课程ID")
	private Long id;

	@Schema(description = "课程名称")
	private String name;

	@Schema(description = "完成进度百分比")
	private Integer progress;
}
