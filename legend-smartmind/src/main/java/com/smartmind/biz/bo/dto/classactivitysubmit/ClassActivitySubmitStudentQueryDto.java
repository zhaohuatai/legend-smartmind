package com.smartmind.biz.bo.dto.classactivitysubmit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Schema(description = "学生课堂活动提交查询DTO")
public class ClassActivitySubmitStudentQueryDto{

	
	@Schema(description = "课堂活动ID")
	private java.lang.Long activityId;
	
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	


}
