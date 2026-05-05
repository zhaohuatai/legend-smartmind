package com.smartmind.biz.bo.dto.knowledgeheatmap;
import com.smartmind.biz.bo.model.KnowledgeHeatmap;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeHeatmapConvert {

    KnowledgeHeatmapConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(KnowledgeHeatmapConvert.class);

    
    KnowledgeHeatmap convert(KnowledgeHeatmapCreateDto createDto);

    KnowledgeHeatmap convert(KnowledgeHeatmapUpdateDto updateDto);


   
}
