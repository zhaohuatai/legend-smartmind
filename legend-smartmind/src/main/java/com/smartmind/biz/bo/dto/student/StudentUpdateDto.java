package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生信息更新Dto")
public class StudentUpdateDto extends StudentBaseDto{
	
	@NotNull(message="ID不能为空")
	@Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
	private String id;

}
