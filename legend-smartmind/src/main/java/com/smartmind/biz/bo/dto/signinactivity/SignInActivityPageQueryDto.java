package com.smartmind.biz.bo.dto.signinactivity;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到活动分页查询DTO")
public class SignInActivityPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "签到编码")
	private java.lang.String signCode;
	
	@Schema(description = "签到名称")
	private java.lang.String signName;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "发布教师ID")
	private java.lang.String teacherId;
	
	@Schema(description = "教师姓名")
	private java.lang.String teacherName;
	
	@Schema(description = "开始时间开始")
	private java.util.Date startTimeStart;
	@Schema(description = "开始时间截止")
	private java.util.Date startTimeEnd;
	
	@Schema(description = "结束时间开始")
	private java.util.Date endTimeStart;
	@Schema(description = "结束时间截止")
	private java.util.Date endTimeEnd;
	


}
