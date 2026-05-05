package com.smartmind.biz.bo.dto.teachingfile;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教学文件分页查询DTO")
public class TeachingFilePageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "文件名称")
	private java.lang.String fileName;
	
	@Schema(description = "关联课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "关联单元ID")
	private java.lang.Long unitId;
	


}
