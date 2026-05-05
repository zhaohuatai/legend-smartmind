package com.smartmind.biz.bo.dto.student;
import com.smartmind.biz.bo.model.Student;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentConvert {

    StudentConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(StudentConvert.class);

    
    Student convert(StudentCreateDto createDto);

    Student convert(StudentUpdateDto updateDto);


   
}
