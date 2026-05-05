package com.smartmind.biz.bo.dto.exampaper;
import com.smartmind.biz.bo.model.ExamPaper;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExamPaperConvert {

    ExamPaperConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ExamPaperConvert.class);

    
    ExamPaper convert(ExamPaperCreateDto createDto);

    ExamPaper convert(ExamPaperUpdateDto updateDto);


   
}
