package com.smartmind.biz.bo.dto.experimentguide;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "实验指导书基础信息AI生成请求参数")
public class ExperimentGuideBaseInfoRequestDto {

    @Schema(description = "课程ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;

    @Schema(description = "单元编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unitCode;

    @Schema(description = "指导书名称（单元名-第n次实验）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String guideName;

    @Schema(description = "第几次实验", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer experimentSession;

    @Schema(description = "实验指导书内容说明（用户提示词）")
    private String contentDescription;
}
