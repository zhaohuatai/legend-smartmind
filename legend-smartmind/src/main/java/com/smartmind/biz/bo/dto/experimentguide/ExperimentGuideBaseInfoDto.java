package com.smartmind.biz.bo.dto.experimentguide;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "实验指导书基础信息AI生成结果")
public class ExperimentGuideBaseInfoDto {

    @Schema(description = "实验主题")
    private String experimentTitle;

    @Schema(description = "预计课时数（学时）")
    private Integer estimatedHours;

    @Schema(description = "实验目的")
    private String experimentObjectives;

    @Schema(description = "实验原理")
    private String experimentPrinciple;

    @Schema(description = "实验器材/设备")
    private String experimentEquipment;

    @Schema(description = "实验步骤")
    private String experimentSteps;

    @Schema(description = "注意事项")
    private String precautions;

    @Schema(description = "实验报告要求")
    private String reportRequirements;
}
