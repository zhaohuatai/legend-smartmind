package com.smartmind.biz.bo.dto.course;
import com.smartmind.biz.bo.model.Course;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseConvert {

    CourseConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(CourseConvert.class);

    
    Course convert(CourseCreateDto createDto);

    Course convert(CourseUpdateDto updateDto);


   
}
