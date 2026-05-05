package com.smartmind.biz.bo.dto.exampaperquestion;
import com.smartmind.biz.bo.model.ExamPaperQuestion;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExamPaperQuestionConvert {

    ExamPaperQuestionConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ExamPaperQuestionConvert.class);

    
    ExamPaperQuestion convert(ExamPaperQuestionCreateDto createDto);

    ExamPaperQuestion convert(ExamPaperQuestionUpdateDto updateDto);


   
}
