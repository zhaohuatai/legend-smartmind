package com.smartmind.biz.bo.dto.teachingfile;
import com.smartmind.biz.bo.model.TeachingFile;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeachingFileConvert {

    TeachingFileConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(TeachingFileConvert.class);

    
    TeachingFile convert(TeachingFileCreateDto createDto);

    TeachingFile convert(TeachingFileUpdateDto updateDto);


   
}
