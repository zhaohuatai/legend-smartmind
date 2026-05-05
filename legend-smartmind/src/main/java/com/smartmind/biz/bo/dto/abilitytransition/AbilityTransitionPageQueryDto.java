package com.smartmind.biz.bo.dto.abilitytransition;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "能力跃迁分页查询DTO")
public class AbilityTransitionPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "起始单元ID")
	private java.lang.Long fromUnitId;
	
	@Schema(description = "目标单元ID")
	private java.lang.Long toUnitId;
	
	@Schema(description = "分析时间开始")
	private java.util.Date analysisTimeStart;
	@Schema(description = "分析时间截止")
	private java.util.Date analysisTimeEnd;
	


}
