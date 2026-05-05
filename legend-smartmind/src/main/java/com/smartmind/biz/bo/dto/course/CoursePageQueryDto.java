package com.smartmind.biz.bo.dto.course;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课程信息分页查询DTO")
public class CoursePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "课程编码")
	private java.lang.String courseCode;
	
	@Schema(description = "课程名称")
	private java.lang.String courseName;
	


}
