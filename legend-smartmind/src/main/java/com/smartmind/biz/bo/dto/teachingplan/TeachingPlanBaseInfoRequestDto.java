package com.smartmind.biz.bo.dto.teachingplan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "教案基础信息AI生成请求参数")
public class TeachingPlanBaseInfoRequestDto {

    @Schema(description = "课程ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;

    @Schema(description = "单元编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unitCode;

    @Schema(description = "教案名称（单元名-第n次课）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String planName;

    @Schema(description = "第几次课", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer lessonSession;

    @Schema(description = "教案内容说明（用户提示词）")
    private String contentDescription;
}
