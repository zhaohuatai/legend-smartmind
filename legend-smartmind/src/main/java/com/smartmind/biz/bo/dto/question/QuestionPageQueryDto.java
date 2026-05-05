package com.smartmind.biz.bo.dto.question;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "题目分页查询DTO")
public class QuestionPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "题目标识码")
	private java.lang.String questionCode;
	
	@Schema(description = "所属课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "所属单元ID")
	private java.lang.Long unitId;
	
	@Schema(description = "单元编码")
	private java.lang.String unitCode;
	
	@Schema(description = "单元编码列表")
	private java.util.List<java.lang.String> unitCodes;
	
	@Schema(description = "题目内容关键词")
	private java.lang.String keyword;
	


}
