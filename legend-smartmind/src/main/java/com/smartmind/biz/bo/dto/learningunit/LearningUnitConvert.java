package com.smartmind.biz.bo.dto.learningunit;
import com.smartmind.biz.bo.model.LearningUnit;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningUnitConvert {

    LearningUnitConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(LearningUnitConvert.class);

    
    LearningUnit convert(LearningUnitCreateDto createDto);

    LearningUnit convert(LearningUnitUpdateDto updateDto);


   
}
