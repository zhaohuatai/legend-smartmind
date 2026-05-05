package com.smartmind.biz.bo.dto.learningreport;
import com.smartmind.biz.bo.model.LearningReport;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningReportConvert {

    LearningReportConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(LearningReportConvert.class);

    
    LearningReport convert(LearningReportCreateDto createDto);

    LearningReport convert(LearningReportUpdateDto updateDto);


   
}
