package com.smartmind.biz.bo.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class QuestionGenerationResultDto {

    @Schema(description = "题目类型: 1=单选题, 2=多选题, 3=判断题, 4=填空题, 5=简答题, 6=计算题, 7=应用题, 8=综合题")
    private String questionType;

    @Schema(description = "难度等级: 1=容易, 2=较易, 3=中等, 4=较难, 5=困难")
    private String difficultyLevel;

    @Schema(description = "认知层次: 1=识记, 2=理解, 3=应用, 4=分析, 5=综合, 6=评价")
    private String cognitiveLevel;

    @Schema(description = "题目内容")
    private String questionContent;

    @Schema(description = "选项列表（单选/多选/判断题）")
    private List<String> options;

    @Schema(description = "参考答案")
    private String answer;

    @Schema(description = "答案解析")
    private String answerAnalysis;

    @Schema(description = "题目分值")
    private Integer score;

    @Schema(description = "知识点标签")
    private String knowledgePoints;
}
