package com.smartmind.biz.bo.dto.classactivitysubmit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class BatchSubmitRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "学生ID")
	private String studentId;
	
	@Schema(description = "提交记录ID")
	private Long submitId;
	
	@Schema(description = "答案列表")
	private List<AnswerItem> answers;

	@Data
	public static class AnswerItem implements Serializable {
		
		private static final long serialVersionUID = 1L;

		@Schema(description = "答题记录ID")
		private Long detailId;

		@Schema(description = "学生答案")
		private String answerContent;
	}
}
