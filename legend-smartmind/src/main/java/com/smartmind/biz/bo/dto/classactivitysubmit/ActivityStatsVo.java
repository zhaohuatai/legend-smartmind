package com.smartmind.biz.bo.dto.classactivitysubmit;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ActivityStatsVo {
	@Schema(description = "班级总人数")
	private Integer totalStudents;
	
	@Schema(description = "已提交人数")
	private Integer submittedCount;
	
	@Schema(description = "未提交人数")
	private Integer unsubmittedCount;
	
	@Schema(description = "提交率(%)")
	private Integer submitRate;
	
	@Schema(description = "平均分")
	private BigDecimal avgScore;
}
