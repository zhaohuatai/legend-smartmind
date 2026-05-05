package com.smartmind.biz.bo.dto.teachingplan;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教案分页查询DTO")
public class TeachingPlanPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "教案名称")
	private java.lang.String planName;
	
	@Schema(description = "课时主题")
	private java.lang.String lessonTitle;
	
	@Schema(description = "计划授课日期开始")
	private java.util.Date teachingDateStart;
	@Schema(description = "计划授课日期截止")
	private java.util.Date teachingDateEnd;
	
	@Schema(description = "状态")
	private java.lang.String status;
	


}
