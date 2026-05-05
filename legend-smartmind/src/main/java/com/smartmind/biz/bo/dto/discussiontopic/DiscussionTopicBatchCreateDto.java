package com.smartmind.biz.bo.dto.discussiontopic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
public class DiscussionTopicBatchCreateDto {

    @Schema(description = "话题列表")
    private List<DiscussionTopicCreateDto> topics;
}
