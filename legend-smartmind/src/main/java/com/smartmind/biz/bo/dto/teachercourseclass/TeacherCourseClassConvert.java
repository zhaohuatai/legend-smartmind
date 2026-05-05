package com.smartmind.biz.bo.dto.teachercourseclass;
import com.smartmind.biz.bo.model.TeacherCourseClass;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherCourseClassConvert {

    TeacherCourseClassConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(TeacherCourseClassConvert.class);

    
    TeacherCourseClass convert(TeacherCourseClassCreateDto createDto);

    TeacherCourseClass convert(TeacherCourseClassUpdateDto updateDto);


   
}
