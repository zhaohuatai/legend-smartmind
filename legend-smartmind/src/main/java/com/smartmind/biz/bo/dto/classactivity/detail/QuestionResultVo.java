package com.smartmind.biz.bo.dto.classactivity.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class QuestionResultVo implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Schema(description = "提交详情ID")
	private Long detailId;
	
	@Schema(description = "资源ID")
	private Long resourceId;
	
	@Schema(description = "题目ID")
	private Long questionId;


	
	@Schema(description = "题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题")
	private String questionType;

	@Schema(description = "题目内容")
	private String questionContent;

	@Schema(description = "题目分值")
	private BigDecimal questionScore;

	@Schema(description = "难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难")
	private String difficultyLevel;

	@Schema(description = "知识点标签")
	private String knowledgePoints;



	@Schema(description = "学生答案")
	private String answerContent;

	@Schema(description = "参考答案")
	private String answer;

	@Schema(description = "答案解析")
	private String answerAnalysis;

	@Schema(description = "选项列表JSON结构")
	private String options;

	@Schema(description = "是否正确: 0-错误, 1-正确, NULL-不适用")
	private String isCorrect;

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
