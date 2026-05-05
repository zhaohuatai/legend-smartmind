package com.smartmind.biz.bo.dto.classsession;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂教学分页查询DTO")
public class ClassSessionPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "课堂编码")
	private java.lang.String sessionCode;
	
	@Schema(description = "课堂名称")
	private java.lang.String sessionName;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "上课班级ID")
	private java.lang.Long clazzId;
	
	@Schema(description = "授课教师ID")
	private java.lang.String teacherId;
	
	@Schema(description = "上课时间")
	private java.util.Date startTime;
	


}
