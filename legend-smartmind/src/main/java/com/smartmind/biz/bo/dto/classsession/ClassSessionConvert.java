package com.smartmind.biz.bo.dto.classsession;
import com.smartmind.biz.bo.model.ClassSession;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassSessionConvert {

    ClassSessionConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassSessionConvert.class);

    
    ClassSession convert(ClassSessionCreateDto createDto);

    ClassSession convert(ClassSessionUpdateDto updateDto);


   
}
