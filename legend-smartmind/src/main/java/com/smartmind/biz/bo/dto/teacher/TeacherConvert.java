package com.smartmind.biz.bo.dto.teacher;
import com.smartmind.biz.bo.model.Teacher;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherConvert {

    TeacherConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(TeacherConvert.class);

    
    Teacher convert(TeacherCreateDto createDto);

    Teacher convert(TeacherUpdateDto updateDto);


   
}
