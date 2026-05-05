package com.smartmind.biz.bo.dto.knowledgeheatmap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "知识点热力图创建Dto")
public class KnowledgeHeatmapCreateDto extends KnowledgeHeatmapBaseDto{

	

}
