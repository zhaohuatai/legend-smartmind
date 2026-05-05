package com.smartmind.biz.bo.dto.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class TopicGenerationBatchResultDto {

    @Schema(description = "话题列表")
    private List<TopicGenerationResultDto> topics;
}
