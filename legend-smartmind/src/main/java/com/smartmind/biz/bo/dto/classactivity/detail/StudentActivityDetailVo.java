package com.smartmind.biz.bo.dto.classactivity.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class StudentActivityDetailVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "提交记录ID")
	private Long submitId;
	
	@Schema(description = "学生ID")
	private String studentId;

	@Schema(description = "学生姓名")
	private String studentName;

	@Schema(description = "活动ID")
	private Long activityId;

	@Schema(description = "活动名称")
	private String activityName;

	@Schema(description = "活动类型: 1-随堂测试, 2-随堂练习, 3-课堂实验, 4-话题讨论")
	private String activityType;

	@Schema(description = "活动总分")
	private BigDecimal activityScore;

	@Schema(description = "活动开始时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityStartTime;

	@Schema(description = "活动截止时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityEndTime;

	@Schema(description = "活动状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
	private String activityStatus;



	@Schema(description = "当前得分")
	private BigDecimal score;

	@Schema(description = "做题数量")
	private Integer submitCount;

	@Schema(description = "状态: 0-未提交, 1-已提交, 2-已评分")
	private String status;

	@Schema(description = "提交时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date submitTime;

	@Schema(description = "题目结果列表")
	private List<QuestionResultVo> questionResults;

	@Schema(description = "指导书结果列表")
	private List<GuideResultVo> guideResults;

	@Schema(description = "话题结果列表")
	private List<TopicResultVo> topicResults;
}
