package com.smartmind.biz.bo.dto.teachingplan;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "教案内容更新Dto")
public class TeachingPlanContentUpdateDto {

	@NotNull(message = "教案ID不能为空")
	@Schema(description = "教案ID", requiredMode = RequiredMode.REQUIRED)
	private Long planId;

	@NotNull(message = "教案内容不能为空")
	@Schema(description = "教案完整内容", requiredMode = RequiredMode.REQUIRED)
	private String mainContent;
}
