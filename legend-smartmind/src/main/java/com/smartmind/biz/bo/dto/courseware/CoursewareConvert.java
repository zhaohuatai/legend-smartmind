package com.smartmind.biz.bo.dto.courseware;
import com.smartmind.biz.bo.model.Courseware;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoursewareConvert {

    CoursewareConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(CoursewareConvert.class);

    
    Courseware convert(CoursewareCreateDto createDto);

    Courseware convert(CoursewareUpdateDto updateDto);


   
}
