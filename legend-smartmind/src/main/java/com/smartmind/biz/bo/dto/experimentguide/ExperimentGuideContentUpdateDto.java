package com.smartmind.biz.bo.dto.experimentguide;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "实验指导书内容更新Dto")
public class ExperimentGuideContentUpdateDto {

	@NotNull(message = "指导书ID不能为空")
	@Schema(description = "指导书ID", requiredMode = RequiredMode.REQUIRED)
	private Long guideId;

	@NotNull(message = "指导书内容不能为空")
	@Schema(description = "实验指导书完整内容", requiredMode = RequiredMode.REQUIRED)
	private String mainContent;
}
