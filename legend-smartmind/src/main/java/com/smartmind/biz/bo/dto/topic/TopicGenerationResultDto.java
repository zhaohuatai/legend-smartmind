package com.smartmind.biz.bo.dto.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TopicGenerationResultDto {

    @Schema(description = "话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴")
    private String topicType;

    @Schema(description = "话题名称")
    private String topicName;

    @Schema(description = "话题内容/讨论问题")
    private String topicContent;

    @Schema(description = "预计讨论时长(分钟)")
    private Integer estimatedDuration;

    @Schema(description = "建议小组人数")
    private Integer groupSize;

    @Schema(description = "知识点标签列表")
    private String knowledgePoints;

    @Schema(description = "背景材料/案例描述")
    private String backgroundMaterial;

    @Schema(description = "教师引导提示")
    private String guidanceTips;

    @Schema(description = "预期答案要点")
    private String expectedAnswers;

    @Schema(description = "评价标准")
    private String evaluationCriteria;
}
