package com.smartmind.biz.bo.dto.courseware;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课件管理分页查询DTO")
public class CoursewarePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "课件编码")
	private java.lang.String coursewareCode;
	
	@Schema(description = "课件名称")
	private java.lang.String coursewareName;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "单元ID")
	private java.lang.Long unitId;
	


}
