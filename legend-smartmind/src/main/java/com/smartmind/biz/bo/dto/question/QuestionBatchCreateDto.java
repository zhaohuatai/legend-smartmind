package com.smartmind.biz.bo.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class QuestionBatchCreateDto {

    @Schema(description = "题目列表")
    private List<QuestionCreateDto> questions;
}
