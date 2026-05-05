package com.smartmind.biz.bo.dto.learningunit;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学习单元分页查询DTO")
public class LearningUnitPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "单元编码")
	private java.lang.String unitCode;
	
	@Schema(description = "单元名称")
	private java.lang.String unitName;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "父单元ID(0表示根单元)")
	private java.lang.Long parentId;
	
	@Schema(description = "计划开始日期开始")
	private java.util.Date startDateStart;
	@Schema(description = "计划开始日期截止")
	private java.util.Date startDateEnd;
	
	@Schema(description = "计划结束日期开始")
	private java.util.Date endDateStart;
	@Schema(description = "计划结束日期截止")
	private java.util.Date endDateEnd;
	


}
