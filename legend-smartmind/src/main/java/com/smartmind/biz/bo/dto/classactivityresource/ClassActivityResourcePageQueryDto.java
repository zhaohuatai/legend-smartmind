package com.smartmind.biz.bo.dto.classactivityresource;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂活动资源关联分页查询DTO")
public class ClassActivityResourcePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "课堂活动ID")
	private java.lang.Long activityId;
	
	@Schema(description = "资源名称")
	private java.lang.String resourceName;
	


}
