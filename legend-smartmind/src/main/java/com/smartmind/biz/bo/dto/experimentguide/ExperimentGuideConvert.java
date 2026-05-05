package com.smartmind.biz.bo.dto.experimentguide;
import com.smartmind.biz.bo.model.ExperimentGuide;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExperimentGuideConvert {

    ExperimentGuideConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ExperimentGuideConvert.class);

    
    ExperimentGuide convert(ExperimentGuideCreateDto createDto);

    ExperimentGuide convert(ExperimentGuideUpdateDto updateDto);


   
}
