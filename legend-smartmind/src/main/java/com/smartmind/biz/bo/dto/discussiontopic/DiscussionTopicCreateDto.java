package com.smartmind.biz.bo.dto.discussiontopic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "讨论活动话题创建Dto")
public class DiscussionTopicCreateDto extends DiscussionTopicBaseDto{

	

}
