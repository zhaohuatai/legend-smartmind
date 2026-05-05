package com.smartmind.biz.bo.dto.question;
import com.smartmind.biz.bo.model.Question;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionConvert {

    QuestionConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(QuestionConvert.class);

    
    Question convert(QuestionCreateDto createDto);

    Question convert(QuestionUpdateDto updateDto);


   
}
