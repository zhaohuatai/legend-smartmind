package com.smartmind.biz.bo.dto.activityquestionstatistics;
import com.smartmind.biz.bo.model.ActivityQuestionStatistics;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityQuestionStatisticsConvert {

    ActivityQuestionStatisticsConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ActivityQuestionStatisticsConvert.class);

    
    ActivityQuestionStatistics convert(ActivityQuestionStatisticsCreateDto createDto);

    ActivityQuestionStatistics convert(ActivityQuestionStatisticsUpdateDto updateDto);


   
}
