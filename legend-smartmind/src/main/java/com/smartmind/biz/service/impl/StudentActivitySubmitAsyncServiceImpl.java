package com.smartmind.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.bo.model.DiscussionTopic;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.service.IClassActivitySubmitDetailService;
import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.service.IDiscussionTopicService;
import com.smartmind.biz.service.IExperimentGuideService;
import com.smartmind.biz.service.IQuestionService;
import com.smartmind.biz.service.IStudentActivitySubmitAsyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentActivitySubmitAsyncServiceImpl implements IStudentActivitySubmitAsyncService {

	private final ReactAgent agent;
	private final ObjectMapper objectMapper;

	@Autowired
	private IClassActivitySubmitDetailService classActivitySubmitDetailService;

	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	@Autowired
	private IClassActivityService classActivityService;

	@Autowired
	private IQuestionService questionService;

	@Autowired
	private IExperimentGuideService experimentGuideService;

	@Autowired
	private IDiscussionTopicService discussionTopicService;

	public StudentActivitySubmitAsyncServiceImpl(ChatModel chatModel, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;

		if (chatModel != null) {
			this.agent = ReactAgent.builder()
					.name("StudentActivityGradingAgent")
					.model(chatModel)
					.saver(new MemorySaver())
					.build();
		} else {
			this.agent = null;
		}
	}

	@Async
	@Override
	public void triggerGrading(Long detailId, String resourceType) {
		try {
			Optional<ClassActivitySubmitDetail> detailOpt = classActivitySubmitDetailService.loadById(detailId);
			if (!detailOpt.isPresent()) {
				return;
			}

			ClassActivitySubmitDetail detail = detailOpt.get();

			if ("1".equals(resourceType)) {
				Optional<Question> questionOpt = questionService.loadById(detail.getResourceId());
				if (questionOpt.isPresent()) {
					String questionType = questionOpt.get().getQuestionType();
					if ("5".equals(questionType) || "6".equals(questionType) || "7".equals(questionType) || "8".equals(questionType)) {
						gradeSubjectiveQuestion(detail, questionOpt.get());
					}
				}
			} else if ("3".equals(resourceType)) {
				gradeExperimentReport(detail);
			} else if ("2".equals(resourceType)) {
				gradeDiscussionTopic(detail);
			}
		} catch (Exception e) {
			log.error("异步评分触发失败: resourceType={}, detailId={}", resourceType, detailId, e);
		}
	}

	private void gradeSubjectiveQuestion(ClassActivitySubmitDetail detail, Question question) {
		if (agent == null) {
			log.warn("Grading agent not available");
			return;
		}

		String prompt = buildSubjectiveQuestionPrompt(question, detail.getAnswerContent());
		log.info("AI grading subjective question prompt: {}", prompt);

		try {
			AssistantMessage result = agent.call(prompt);
			log.info("AI grading subjective question response: {}", result.getText());

			JsonNode json = objectMapper.readTree(result.getText());
			BigDecimal score = json.has("score") ? json.get("score").decimalValue() : BigDecimal.ZERO;
			String evaluate = json.has("evaluate") ? json.get("evaluate").asText() : "";

			ClassActivitySubmitDetail updateDetail = new ClassActivitySubmitDetail();
			updateDetail.setId(detail.getId());
			updateDetail.setScore(score);
			updateDetail.setFullScore(question.getScore());
			updateDetail.setAiEvaluate(evaluate);
			updateDetail.setStatus("2");
			updateDetail.setGradeTime(new Date());
			updateDetail.setGradeBy("ai");
			classActivitySubmitDetailService.updateById(updateDetail);

			updateSubmitAndActivity(detail.getSubmitId());
		} catch (Exception e) {
			log.error("Failed to grade subjective question: detailId={}", detail.getId(), e);
		}
	}

	private void gradeExperimentReport(ClassActivitySubmitDetail detail) {
		if (agent == null) {
			log.warn("Grading agent not available");
			return;
		}

		Optional<ExperimentGuide> guideOpt = experimentGuideService.loadById(detail.getResourceId());
		if (!guideOpt.isPresent()) {
			return;
		}

		ExperimentGuide guide = guideOpt.get();
		String prompt = buildExperimentReportPrompt(guide, detail.getAnswerContent());
		log.info("AI grading experiment report prompt: {}", prompt);

		try {
			AssistantMessage result = agent.call(prompt);
			log.info("AI grading experiment report response: {}", result.getText());

			JsonNode json = objectMapper.readTree(result.getText());
			BigDecimal score = json.has("score") ? json.get("score").decimalValue() : BigDecimal.ZERO;
			String evaluate = json.has("evaluate") ? json.get("evaluate").asText() : "";

			ClassActivitySubmitDetail updateDetail = new ClassActivitySubmitDetail();
			updateDetail.setId(detail.getId());
			updateDetail.setScore(score);
			updateDetail.setAiEvaluate(evaluate);
			updateDetail.setStatus("2");
			updateDetail.setGradeTime(new Date());
			updateDetail.setGradeBy("ai");
			classActivitySubmitDetailService.updateById(updateDetail);

			updateSubmitAndActivity(detail.getSubmitId());
		} catch (Exception e) {
			log.error("Failed to grade experiment report: detailId={}", detail.getId(), e);
		}
	}

	private void gradeDiscussionTopic(ClassActivitySubmitDetail detail) {
		if (agent == null) {
			log.warn("Grading agent not available");
			return;
		}

		Optional<DiscussionTopic> topicOpt = discussionTopicService.loadById(detail.getResourceId());
		if (!topicOpt.isPresent()) {
			return;
		}

		DiscussionTopic topic = topicOpt.get();
		String prompt = buildDiscussionTopicPrompt(topic, detail.getAnswerContent());
		log.info("AI grading discussion topic prompt: {}", prompt);

		try {
			AssistantMessage result = agent.call(prompt);
			log.info("AI grading discussion topic response: {}", result.getText());

			JsonNode json = objectMapper.readTree(result.getText());
			BigDecimal score = json.has("score") ? json.get("score").decimalValue() : BigDecimal.ZERO;
			String evaluate = json.has("evaluate") ? json.get("evaluate").asText() : "";

			ClassActivitySubmitDetail updateDetail = new ClassActivitySubmitDetail();
			updateDetail.setId(detail.getId());
			updateDetail.setScore(score);
			updateDetail.setAiEvaluate(evaluate);
			updateDetail.setStatus("2");
			updateDetail.setGradeTime(new Date());
			updateDetail.setGradeBy("ai");
			classActivitySubmitDetailService.updateById(updateDetail);

			updateSubmitAndActivity(detail.getSubmitId());
		} catch (Exception e) {
			log.error("Failed to grade discussion topic: detailId={}", detail.getId(), e);
		}
	}

	private String buildSubjectiveQuestionPrompt(Question question, String studentAnswer) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("你是一位经验丰富的教育专家，请根据以下题目和学生答案进行客观评分。\n\n");
		prompt.append("## 题目信息\n");
		prompt.append("- 题目类型: ").append(question.getQuestionType()).append("\n");
		prompt.append("- 题目内容: ").append(question.getQuestionContent()).append("\n");
		prompt.append("- 参考答案: ").append(question.getAnswer()).append("\n");
		prompt.append("- 答案解析: ").append(question.getAnswerAnalysis()).append("\n");
		prompt.append("- 题目分值: ").append(question.getScore()).append("分\n\n");
		prompt.append("## 学生答案\n");
		prompt.append(studentAnswer).append("\n\n");
		prompt.append("## 评分要求\n");
		prompt.append("1. 根据参考答案和答案解析，客观评价学生答案的质量\n");
		prompt.append("2. 给出合理的分数（0-").append(question.getScore()).append("分之间）\n");
		prompt.append("3. 给出简短的评价说明，指出优点和不足\n\n");
		prompt.append("## 输出格式\n");
		prompt.append("请严格按照以下JSON格式输出：\n");
		prompt.append("{\n");
		prompt.append("  \"score\": 分数（数字）,\n");
		prompt.append("  \"evaluate\": \"评价说明（字符串）\"\n");
		prompt.append("}\n");
		return prompt.toString();
	}

	private String buildExperimentReportPrompt(ExperimentGuide guide, String studentAnswer) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("你是一位实验教学专家，请根据以下实验指导书要求和学生的实验报告进行客观评分。\n\n");
		prompt.append("## 实验指导书信息\n");
		prompt.append("- 实验名称: ").append(guide.getGuideName()).append("\n");
		prompt.append("- 实验目的: ").append(guide.getExperimentObjectives()).append("\n");
		prompt.append("- 实验原理: ").append(guide.getExperimentPrinciple()).append("\n");
		prompt.append("- 实验步骤: ").append(guide.getExperimentSteps()).append("\n");
		prompt.append("- 报告要求: ").append(guide.getReportRequirements()).append("\n\n");
		prompt.append("## 学生实验报告\n");
		prompt.append(studentAnswer).append("\n\n");
		prompt.append("## 评分要求\n");
		prompt.append("1. 根据实验目的、原理、步骤和报告要求，客观评价学生实验报告的质量\n");
		prompt.append("2. 给出合理的分数（0-100分之间）\n");
		prompt.append("3. 给出简短的评价说明，指出优点和不足\n\n");
		prompt.append("## 输出格式\n");
		prompt.append("请严格按照以下JSON格式输出：\n");
		prompt.append("{\n");
		prompt.append("  \"score\": 分数（数字）,\n");
		prompt.append("  \"evaluate\": \"评价说明（字符串）\"\n");
		prompt.append("}\n");
		return prompt.toString();
	}

	private String buildDiscussionTopicPrompt(DiscussionTopic topic, String studentAnswer) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("你是一位课堂讨论评价专家，请根据以下话题要求和学生的讨论内容进行客观评分。\n\n");
		prompt.append("## 话题信息\n");
		prompt.append("- 话题名称: ").append(topic.getTopicName()).append("\n");
		prompt.append("- 话题内容: ").append(topic.getTopicContent()).append("\n");
		prompt.append("- 背景材料: ").append(topic.getBackgroundMaterial()).append("\n");
		prompt.append("- 预期答案要点: ").append(topic.getExpectedAnswers()).append("\n");
		prompt.append("- 评价标准: ").append(topic.getEvaluationCriteria()).append("\n\n");
		prompt.append("## 学生讨论内容\n");
		prompt.append(studentAnswer).append("\n\n");
		prompt.append("## 评分要求\n");
		prompt.append("1. 根据话题的预期答案要点和评价标准，客观评价学生讨论内容的质量\n");
		prompt.append("2. 给出合理的分数（0-100分之间）\n");
		prompt.append("3. 给出简短的评价说明，指出优点和不足\n\n");
		prompt.append("## 输出格式\n");
		prompt.append("请严格按照以下JSON格式输出：\n");
		prompt.append("{\n");
		prompt.append("  \"score\": 分数（数字）,\n");
		prompt.append("  \"evaluate\": \"评价说明（字符串）\"\n");
		prompt.append("}\n");
		return prompt.toString();
	}

	private void updateSubmitAndActivity(Long submitId) {
		if (submitId == null) {
			return;
		}

		Optional<ClassActivitySubmit> submitOpt = classActivitySubmitService.loadById(submitId);
		if (!submitOpt.isPresent()) {
			return;
		}
		ClassActivitySubmit submit = submitOpt.get();

		List<ClassActivitySubmitDetail> details = classActivitySubmitDetailService.selectBySubmitId(submitId);

		BigDecimal totalScore = BigDecimal.ZERO;
		int gradedCount = 0;
		int totalCount = details != null ? details.size() : 0;

		if (details != null && !details.isEmpty()) {
			for (ClassActivitySubmitDetail detail : details) {
				if (detail.getScore() != null) {
					totalScore = totalScore.add(detail.getScore());
					gradedCount++;
				}
			}
		}

		submit.setScore(totalScore);
		if (gradedCount == totalCount && totalCount > 0) {
			submit.setStatus("2");
		}
		classActivitySubmitService.updateById(submit);

		if (submit.getActivityId() != null) {
			Optional<ClassActivity> activityOpt = classActivityService.loadById(submit.getActivityId());
			if (activityOpt.isPresent()) {
				ClassActivity activity = activityOpt.get();
				activity.setUpdateTime(new Date());
				classActivityService.updateById(activity);
			}
		}
	}
}
