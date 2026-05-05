package com.smartmind.biz.bo.dto.activitysubmitstatistics;
import com.smartmind.biz.bo.model.ActivitySubmitStatistics;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivitySubmitStatisticsConvert {

    ActivitySubmitStatisticsConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ActivitySubmitStatisticsConvert.class);

    
    ActivitySubmitStatistics convert(ActivitySubmitStatisticsCreateDto createDto);

    ActivitySubmitStatistics convert(ActivitySubmitStatisticsUpdateDto updateDto);


   
}
