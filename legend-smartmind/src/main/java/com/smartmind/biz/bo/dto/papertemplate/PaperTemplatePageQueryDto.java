package com.smartmind.biz.bo.dto.papertemplate;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "试卷模板分页查询DTO")
public class PaperTemplatePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "模板编码")
	private java.lang.String templateCode;
	
	@Schema(description = "模板名称")
	private java.lang.String templateName;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	


}
