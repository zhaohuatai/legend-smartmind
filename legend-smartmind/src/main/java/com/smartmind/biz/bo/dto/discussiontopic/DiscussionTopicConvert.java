package com.smartmind.biz.bo.dto.discussiontopic;
import com.smartmind.biz.bo.model.DiscussionTopic;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiscussionTopicConvert {

    DiscussionTopicConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(DiscussionTopicConvert.class);

    
    DiscussionTopic convert(DiscussionTopicCreateDto createDto);

    DiscussionTopic convert(DiscussionTopicUpdateDto updateDto);


   
}
