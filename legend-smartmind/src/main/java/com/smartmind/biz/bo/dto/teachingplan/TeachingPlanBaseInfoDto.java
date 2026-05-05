package com.smartmind.biz.bo.dto.teachingplan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "教案基础信息AI生成结果")
public class TeachingPlanBaseInfoDto {

    @Schema(description = "课时主题")
    private String lessonTitle;

    @Schema(description = "授课内容简介")
    private String lessonSummary;
    
    @Schema(description = "预计课时数（学时）")
    private Integer estimatedHours;

    @Schema(description = "知识目标")
    private String knowledgeObjectives;

    @Schema(description = "能力目标")
    private String abilityObjectives;

    @Schema(description = "素养目标")
    private String literacyObjectives;

    @Schema(description = "教学重点")
    private String keyPoints;

    @Schema(description = "教学难点")
    private String difficultPoints;

    @Schema(description = "教学方法列表")
    private List<String> teachingMethods;

    @Schema(description = "教学工具/媒体")
    private List<String> teachingTools;

    @Schema(description = "教学主要内容")
    private String mainContent;

    @Schema(description = "教学反思")
    private String teachingReflection;
}
