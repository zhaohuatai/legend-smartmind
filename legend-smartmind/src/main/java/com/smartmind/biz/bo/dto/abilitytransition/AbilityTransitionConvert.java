package com.smartmind.biz.bo.dto.abilitytransition;
import com.smartmind.biz.bo.model.AbilityTransition;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbilityTransitionConvert {

    AbilityTransitionConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(AbilityTransitionConvert.class);

    
    AbilityTransition convert(AbilityTransitionCreateDto createDto);

    AbilityTransition convert(AbilityTransitionUpdateDto updateDto);


   
}
