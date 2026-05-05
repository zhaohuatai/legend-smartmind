package com.smartmind.biz.bo.dto.exampaperquestion;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "试卷题目关联分页查询DTO")
public class ExamPaperQuestionPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "试卷ID")
	private java.lang.Long paperId;
	
	@Schema(description = "题目ID")
	private java.lang.Long questionId;
	


}
