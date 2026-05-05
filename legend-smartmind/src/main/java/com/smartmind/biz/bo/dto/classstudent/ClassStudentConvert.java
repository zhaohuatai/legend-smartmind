package com.smartmind.biz.bo.dto.classstudent;
import com.smartmind.biz.bo.model.ClassStudent;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassStudentConvert {

    ClassStudentConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassStudentConvert.class);

    
    ClassStudent convert(ClassStudentCreateDto createDto);

    ClassStudent convert(ClassStudentUpdateDto updateDto);


   
}
