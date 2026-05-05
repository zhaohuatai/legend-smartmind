package com.smartmind.biz.bo.dto.papertemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "试卷模板创建Dto")
public class PaperTemplateCreateDto extends PaperTemplateBaseDto{

	

}
