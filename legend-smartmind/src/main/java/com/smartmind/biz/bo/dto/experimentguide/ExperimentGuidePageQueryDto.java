package com.smartmind.biz.bo.dto.experimentguide;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "实验指导书分页查询DTO")
public class ExperimentGuidePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "指导书名称")
	private java.lang.String guideName;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "单元编码")
	private java.lang.String unitCode;
	
	@Schema(description = "单元编码列表")
	private java.util.List<java.lang.String> unitCodes;
	
	@Schema(description = "状态")
	private java.lang.String status;
	


}
