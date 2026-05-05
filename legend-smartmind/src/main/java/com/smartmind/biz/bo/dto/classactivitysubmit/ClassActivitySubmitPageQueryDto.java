package com.smartmind.biz.bo.dto.classactivitysubmit;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生课堂活动提交分页查询DTO")
public class ClassActivitySubmitPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "提交编码")
	private java.lang.String submitCode;
	
	@Schema(description = "课堂活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "课堂ID")
	private java.lang.Long sessionId;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "班级ID")
	private java.lang.Long clazzId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "提交时间")
	private java.util.Date submitTime;
	
	@Schema(description = "评分时间")
	private java.util.Date gradeTime;
	


}
