package com.smartmind.biz.bo.dto.activityquestionstatistics;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "题目统计分页查询DTO")
public class ActivityQuestionStatisticsPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "提交记录ID")
	private java.lang.Long submitId;
	
	@Schema(description = "活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "题目ID")
	private java.lang.Long questionId;
	
	@Schema(description = "错误类型")
	private java.lang.String commonMistakeType;
	
	@Schema(description = "统计时间开始")
	private java.util.Date statisticsTimeStart;
	@Schema(description = "统计时间截止")
	private java.util.Date statisticsTimeEnd;
	


}
