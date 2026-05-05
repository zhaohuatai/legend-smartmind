package com.smartmind.biz.bo.dto.teachercourseclass;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教师课程班级关联分页查询DTO")
public class TeacherCourseClassPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "教师ID")
	private java.lang.String teacherId;
	
	@Schema(description = "教师姓名")
	private java.lang.String teacherName;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "授课教室")
	private java.lang.String classroom;
	


}
