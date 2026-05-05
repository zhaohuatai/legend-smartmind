package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生题型表现统计Dto")
public class StudentTypeStatsDto {

	@Schema(description = "题型类型")
	private String type;

	@Schema(description = "题型名称")
	private String name;

	@Schema(description = "正确率")
	private Integer accuracy;

	@Schema(description = "颜色")
	private String color;
}
