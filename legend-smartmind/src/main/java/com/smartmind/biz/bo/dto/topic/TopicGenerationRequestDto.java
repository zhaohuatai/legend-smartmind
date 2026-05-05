package com.smartmind.biz.bo.dto.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TopicGenerationRequestDto {

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "单元编码")
    private String unitCode;

    @Schema(description = "话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴")
    private String topicType;

    @Schema(description = "内容说明/主题")
    private String contentDescription;

    @Schema(description = "预计讨论时长(分钟)")
    private Integer estimatedDuration;

    @Schema(description = "建议小组人数")
    private Integer groupSize;
}
