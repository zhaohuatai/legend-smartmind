package com.smartmind.biz.bo.dto.signinactivity;
import com.smartmind.biz.bo.model.SignInActivity;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignInActivityConvert {

    SignInActivityConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(SignInActivityConvert.class);

    
    SignInActivity convert(SignInActivityCreateDto createDto);

    SignInActivity convert(SignInActivityUpdateDto updateDto);


   
}
