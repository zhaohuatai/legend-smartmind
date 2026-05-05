package com.smartmind.biz.bo.dto.classactivity;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂活动分页查询DTO")
public class ClassActivityPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "活动编码")
	private java.lang.String activityCode;
	
	@Schema(description = "活动名称")
	private java.lang.String activityName;
	
	@Schema(description = "所属课堂ID")
	private java.lang.Long sessionId;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "上课班级ID")
	private java.lang.Long clazzId;
	
	@Schema(description = "活动开始时间")
	private java.util.Date startTime;
	


}
