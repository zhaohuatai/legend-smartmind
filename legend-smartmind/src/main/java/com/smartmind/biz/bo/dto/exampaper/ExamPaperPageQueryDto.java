package com.smartmind.biz.bo.dto.exampaper;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "试卷分页查询DTO")
public class ExamPaperPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "试卷编码")
	private java.lang.String paperCode;
	
	@Schema(description = "试卷名称")
	private java.lang.String paperName;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "所属单元ID")
	private java.lang.Long unitId;
	
	@Schema(description = "关联教案ID")
	private java.lang.Long planId;
	


}
