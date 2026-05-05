package com.smartmind.biz.bo.dto.classactivityresource;
import com.smartmind.biz.bo.model.ClassActivityResource;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassActivityResourceConvert {

    ClassActivityResourceConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassActivityResourceConvert.class);

    
    ClassActivityResource convert(ClassActivityResourceCreateDto createDto);

    ClassActivityResource convert(ClassActivityResourceUpdateDto updateDto);


   
}
