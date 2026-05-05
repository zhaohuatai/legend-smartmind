package com.smartmind.biz.bo.dto.classactivity;
import com.smartmind.biz.bo.model.ClassActivity;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassActivityConvert {

    ClassActivityConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassActivityConvert.class);

    
    ClassActivity convert(ClassActivityCreateDto createDto);

    ClassActivity convert(ClassActivityUpdateDto updateDto);


   
}
