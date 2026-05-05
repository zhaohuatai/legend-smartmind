package com.smartmind.biz.bo.dto.signinrecord;
import com.smartmind.biz.bo.model.SignInRecord;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignInRecordConvert {

    SignInRecordConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(SignInRecordConvert.class);

    
    SignInRecord convert(SignInRecordCreateDto createDto);

    SignInRecord convert(SignInRecordUpdateDto updateDto);


   
}
