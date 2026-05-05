package com.smartmind.biz.bo.dto.classcourse;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "班级课程关联分页查询DTO")
public class ClassCoursePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "授课教室")
	private java.lang.String classroom;
	


}
