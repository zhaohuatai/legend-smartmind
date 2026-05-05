package com.smartmind.biz.bo.dto.classactivity.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TopicResultVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "话题ID")
	private Long topicId;

	@Schema(description = "话题名称")
	private String topicName;

	@Schema(description = "话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴")
	private Byte topicType;

	@Schema(description = "话题内容/讨论问题")
	private String topicContent;

	@Schema(description = "背景材料/案例描述")
	private String backgroundMaterial;

	@Schema(description = "教师引导提示")
	private String guidanceTips;

	@Schema(description = "预期答案要点")
	private String expectedAnswers;

	@Schema(description = "评价标准")
	private String evaluationCriteria;

	@Schema(description = "话题总分")
	private BigDecimal topicScore;

	@Schema(description = "提交详情ID")
	private Long detailId;

	@Schema(description = "学生答案/讨论内容")
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
