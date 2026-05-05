package com.smartmind.biz.bo.dto.classactivity.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GuideResultVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "指导书ID")
	private Long guideId;

	@Schema(description = "指导书名称")
	private String guideName;

	@Schema(description = "实验目的")
	private String experimentObjectives;

	@Schema(description = "实验原理")
	private String experimentPrinciple;

	@Schema(description = "实验器材/设备")
	private String experimentEquipment;

	@Schema(description = "实验步骤")
	private String experimentSteps;

	@Schema(description = "注意事项")
	private String precautions;

	@Schema(description = "实验报告要求")
	private String reportRequirements;

	@Schema(description = "指导书总分")
	private BigDecimal guideScore;

	@Schema(description = "提交详情ID")
	private Long detailId;

	@Schema(description = "学生答案/实验报告")
	private String answerContent;

	@Schema(description = "得分")
	private BigDecimal score;

	@Schema(description = "满分")
	private BigDecimal fullScore;

	@Schema(description = "状态: 0-未作答, 1-已作答, 2-已评分")
	private String status;

	@Schema(description = "作答时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date submitTime;

	@Schema(description = "智能体评级")
	private String aiEvaluate;

	@Schema(description = "教师评价")
	private String teacherEvaluate;
}
