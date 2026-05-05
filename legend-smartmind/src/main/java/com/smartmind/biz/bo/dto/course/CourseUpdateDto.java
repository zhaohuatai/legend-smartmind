package com.smartmind.biz.bo.dto.course;

import java.util.List;

import com.smartmind.biz.bo.dto.classcourse.CourseClassRelationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课程信息更新Dto")
public class CourseUpdateDto extends CourseBaseDto{
	
	@NotNull(message="ID不能为空")
	@Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
	private Long id;

	
	private List<Long>  classIds;
}
