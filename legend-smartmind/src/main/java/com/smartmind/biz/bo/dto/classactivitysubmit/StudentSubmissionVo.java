package com.smartmind.biz.bo.dto.classactivitysubmit;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentSubmissionVo {
	@Schema(description = "学生ID")
	private String studentId;
	
	@Schema(description = "学生姓名")
	private String studentName;
	
	@Schema(description = "学号")
	private String studentNo;
	
	@Schema(description = "是否已提交")
	private Boolean submitted;
	
	@Schema(description = "提交记录ID")
	private Long submitId;
	
	@Schema(description = "得分")
	private BigDecimal score;
	
	@Schema(description = "状态")
	private String status;
	
	@Schema(description = "提交时间")
	private Date submitTime;
	
	@Schema(description = "做题数量")
	private Integer submitCount;
}
