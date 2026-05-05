package com.smartmind.biz.bo.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QuestionGenerationRequestDto {

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "单元编码")
    private String unitCode;

    @Schema(description = "题目类型列表")
    private List<String> questionTypes;

    @Schema(description = "出题总数")
    private Integer totalCount;

    @Schema(description = "难度等级（单一指定）")
    private String difficultyLevel;

    @Schema(description = "难度等级列表（多项指定范围）")
    private List<String> difficultyLevels;

    @Schema(description = "认知层次列表（指定范围）")
    private List<String> cognitiveLevels;

    @Schema(description = "难度分布（key:难度等级,value:题数）")
    private Map<String, Integer> difficultyDistribution;

    @Schema(description = "认知层次分布（key:认知层次,value:题数）")
    private Map<String, Integer> cognitiveDistribution;

    @Schema(description = "内容说明/出题要求")
    private String contentDescription;
}
