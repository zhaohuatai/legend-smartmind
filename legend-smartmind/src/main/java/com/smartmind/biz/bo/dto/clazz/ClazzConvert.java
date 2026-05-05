package com.smartmind.biz.bo.dto.clazz;
import com.smartmind.biz.bo.model.Clazz;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClazzConvert {

    ClazzConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClazzConvert.class);

    
    Clazz convert(ClazzCreateDto createDto);

    Clazz convert(ClazzUpdateDto updateDto);


   
}
