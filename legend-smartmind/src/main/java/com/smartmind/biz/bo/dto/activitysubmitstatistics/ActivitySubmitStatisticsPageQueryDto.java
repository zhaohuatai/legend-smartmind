package com.smartmind.biz.bo.dto.activitysubmitstatistics;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "提交统计分页查询DTO")
public class ActivitySubmitStatisticsPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "提交记录ID")
	private java.lang.Long submitId;
	
	@Schema(description = "活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "统计时间开始")
	private java.util.Date statisticsTimeStart;
	@Schema(description = "统计时间截止")
	private java.util.Date statisticsTimeEnd;
	


}
