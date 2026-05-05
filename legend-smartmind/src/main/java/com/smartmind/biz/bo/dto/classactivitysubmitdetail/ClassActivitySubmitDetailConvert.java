package com.smartmind.biz.bo.dto.classactivitysubmitdetail;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassActivitySubmitDetailConvert {

    ClassActivitySubmitDetailConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ClassActivitySubmitDetailConvert.class);

    
    ClassActivitySubmitDetail convert(ClassActivitySubmitDetailCreateDto createDto);

    ClassActivitySubmitDetail convert(ClassActivitySubmitDetailUpdateDto updateDto);


   
}
