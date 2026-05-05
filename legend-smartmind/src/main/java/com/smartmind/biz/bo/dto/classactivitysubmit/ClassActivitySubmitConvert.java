package com.smartmind.biz.bo.dto.classactivitysubmit;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassActivitySubmitConvert {

    ClassActivitySubmitConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassActivitySubmitConvert.class);

    
    ClassActivitySubmit convert(ClassActivitySubmitCreateDto createDto);

    ClassActivitySubmit convert(ClassActivitySubmitUpdateDto updateDto);


   
}
