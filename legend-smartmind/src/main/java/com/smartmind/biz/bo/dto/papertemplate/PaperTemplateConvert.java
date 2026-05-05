package com.smartmind.biz.bo.dto.papertemplate;
import com.smartmind.biz.bo.model.PaperTemplate;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaperTemplateConvert {

    PaperTemplateConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(PaperTemplateConvert.class);

    
    PaperTemplate convert(PaperTemplateCreateDto createDto);

    PaperTemplate convert(PaperTemplateUpdateDto updateDto);


   
}
