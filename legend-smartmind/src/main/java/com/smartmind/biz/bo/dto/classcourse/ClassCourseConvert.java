package com.smartmind.biz.bo.dto.classcourse;
import com.smartmind.biz.bo.model.ClassCourse;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassCourseConvert {

    ClassCourseConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassCourseConvert.class);

    
    ClassCourse convert(ClassCourseCreateDto createDto);

    ClassCourse convert(ClassCourseUpdateDto updateDto);


   
}
