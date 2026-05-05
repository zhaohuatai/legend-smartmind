package com.smartmind.biz.bo.dto.learningsnapshot;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学习快照分页查询DTO")
public class LearningSnapshotPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "快照时间开始")
	private java.util.Date snapshotTimeStart;
	@Schema(description = "快照时间截止")
	private java.util.Date snapshotTimeEnd;
	


}
