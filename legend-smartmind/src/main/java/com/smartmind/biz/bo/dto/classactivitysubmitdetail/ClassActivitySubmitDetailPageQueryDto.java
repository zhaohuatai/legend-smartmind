package com.smartmind.biz.bo.dto.classactivitysubmitdetail;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生课堂活动提交明细分页查询DTO")
public class ClassActivitySubmitDetailPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "提交记录ID")
	private java.lang.Long submitId;
	
	@Schema(description = "课堂活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "资源ID")
	private java.lang.Long resourceId;
	
	@Schema(description = "资源名称")
	private java.lang.String resourceName;
	
	@Schema(description = "作答时间")
	private java.util.Date submitTime;
	
	@Schema(description = "评分时间")
	private java.util.Date gradeTime;
	


}
