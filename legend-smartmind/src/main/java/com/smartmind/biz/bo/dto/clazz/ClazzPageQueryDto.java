package com.smartmind.biz.bo.dto.clazz;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "班级信息分页查询DTO")
public class ClazzPageQueryDto extends PageParam {
	private static final long serialVersionUID = 1L;

	@Schema(description = "班级名称")
	private java.lang.String className;

	@Schema(description = "年级")
	private java.lang.String gradeLevel;

	@Schema(description = "状态: 0-失效, 1-有效")
	private java.lang.String status;
}
