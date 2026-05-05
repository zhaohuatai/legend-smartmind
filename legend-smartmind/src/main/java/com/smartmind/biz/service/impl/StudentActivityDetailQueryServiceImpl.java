package com.smartmind.biz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartmind.biz.bo.dto.classactivity.detail.GuideResultVo;
import com.smartmind.biz.bo.dto.classactivity.detail.QuestionResultVo;
import com.smartmind.biz.bo.dto.classactivity.detail.StudentActivityDetailVo;
import com.smartmind.biz.bo.dto.classactivity.detail.TopicResultVo;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.model.ClassActivityResource;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.bo.model.DiscussionTopic;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.service.IClassActivityResourceService;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.service.IClassActivitySubmitDetailService;
import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.service.IDiscussionTopicService;
import com.smartmind.biz.service.IExperimentGuideService;
import com.smartmind.biz.service.IQuestionService;
import com.smartmind.biz.service.IStudentActivityDetailQueryService;
import com.smartmind.biz.service.IStudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentActivityDetailQueryServiceImpl implements IStudentActivityDetailQueryService {

	@Autowired
	private IStudentService studentService;

	@Autowired
	private IClassActivityService classActivityService;

	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	@Autowired
	private IClassActivitySubmitDetailService classActivitySubmitDetailService;

	@Autowired
	private IClassActivityResourceService classActivityResourceService;

	@Autowired
	private IQuestionService questionService;

	@Autowired
	private IExperimentGuideService experimentGuideService;

	@Autowired
	private IDiscussionTopicService discussionTopicService;

	@Override
	public StudentActivityDetailVo queryStudentActivityDetail(Long activityId, String studentId,boolean needAnswer) {
		if (activityId == null || studentId == null) {
			return null;
		}

		StudentActivityDetailVo vo = new StudentActivityDetailVo();

		Optional<Student> studentOpt = studentService.loadById(studentId);
		if (studentOpt.isPresent()) {
			Student student = studentOpt.get();
			vo.setStudentId(student.getId());
			vo.setStudentName(student.getStudentName());
		}

		Optional<ClassActivity> activityOpt = classActivityService.loadById(activityId);
		if (activityOpt.isPresent()) {
			ClassActivity activity = activityOpt.get();
			vo.setActivityId(activity.getId());
			vo.setActivityName(activity.getActivityName());
			vo.setActivityType(activity.getActivityType());
			vo.setActivityScore(activity.getScore());
			vo.setActivityStartTime(activity.getStartTime());
			vo.setActivityEndTime(activity.getEndTime());
		}

		ClassActivitySubmit submit = classActivitySubmitService.selectByActivityAndStudent(activityId, studentId);
		if (submit != null) {
			vo.setSubmitId(submit.getId());
			vo.setScore(submit.getScore());
			vo.setSubmitCount(submit.getSubmitCount());
			vo.setStatus(submit.getStatus());
			vo.setSubmitTime(submit.getSubmitTime());
		}

		List<ClassActivityResource> resources = classActivityResourceService.loadByActivityId(activityId);

		List<QuestionResultVo> questionResults = new ArrayList<>();
		List<GuideResultVo> guideResults = new ArrayList<>();
		List<TopicResultVo> topicResults = new ArrayList<>();

		if (resources != null && !resources.isEmpty()) {
			for (ClassActivityResource resource : resources) {
				
				ClassActivitySubmitDetail detail = classActivitySubmitDetailService
						.selectBySubmitAndResource(submit.getId(), 
											resource.getResourceId(), 
											resource.getResourceType());
//				class_activity_resource_type	1	题库
//				class_activity_resource_type	2	讨论话题
//				class_activity_resource_type	3	实验指导书
				if ("1".equals(resource.getResourceType())) {
					questionResults.addAll(buildQuestionResults(resource, detail, needAnswer));
				} else if ("2".equals(resource.getResourceType())) {
					topicResults.add(buildTopicResult(resource, detail, needAnswer));
				}else if ("3".equals(resource.getResourceType())) {
					guideResults.add(buildGuideResult(resource, detail, needAnswer));
				} 
			}
		}

		vo.setQuestionResults(questionResults);
		vo.setGuideResults(guideResults);
		vo.setTopicResults(topicResults);

		return vo;
	}

	private List<QuestionResultVo> buildQuestionResults(ClassActivityResource resource, ClassActivitySubmitDetail detail,boolean needAnswer) {
		List<QuestionResultVo> results = new ArrayList<>();

		Optional<Question> questionOpt = questionService.loadById(resource.getResourceId());
		if (!questionOpt.isPresent()) {
			return results;
		}
		Question question = questionOpt.get();


		QuestionResultVo vo = new QuestionResultVo();
		vo.setResourceId(resource.getId());
		vo.setQuestionId(question.getId());
		vo.setQuestionType(question.getQuestionType());
		vo.setQuestionContent(question.getQuestionContent());
		vo.setQuestionScore(question.getScore());
		vo.setDifficultyLevel(question.getDifficultyLevel());
		vo.setKnowledgePoints(question.getKnowledgePoints());

		if (detail != null) {
			vo.setDetailId(detail.getId());
			vo.setAnswerContent(detail.getAnswerContent());
			
			vo.setFullScore(detail.getFullScore());
			vo.setStatus(detail.getStatus());
			vo.setSubmitTime(detail.getSubmitTime());
			
			vo.setIsCorrect(needAnswer?detail.getIsCorrect():null);
			vo.setScore(needAnswer?detail.getScore():null);
			vo.setAiEvaluate(needAnswer?detail.getAiEvaluate():null);
			vo.setTeacherEvaluate(needAnswer?detail.getTeacherEvaluate():null);
		}

		vo.setAnswer(needAnswer?question.getAnswer():null);//参考答案
		vo.setAnswerAnalysis(needAnswer?question.getAnswerAnalysis():null);
		vo.setOptions(question.getOptions());

		results.add(vo);
		return results;
	}

	private GuideResultVo buildGuideResult(ClassActivityResource resource, ClassActivitySubmitDetail detail,boolean needAnswer) {
		GuideResultVo vo = new GuideResultVo();

		Optional<ExperimentGuide> guideOpt = experimentGuideService.loadById(resource.getResourceId());
		if (guideOpt.isPresent()) {
			ExperimentGuide guide = guideOpt.get();
			vo.setGuideId(guide.getId());
			vo.setGuideName(guide.getGuideName());
			vo.setExperimentObjectives(guide.getExperimentObjectives());
			vo.setExperimentPrinciple(guide.getExperimentPrinciple());
			vo.setExperimentEquipment(guide.getExperimentEquipment());
			vo.setExperimentSteps(guide.getExperimentSteps());
			vo.setPrecautions(guide.getPrecautions());
			vo.setReportRequirements(guide.getReportRequirements());
			vo.setGuideScore(resource.getScore());
		}

		if (detail != null) {
			vo.setDetailId(detail.getId());
			vo.setAnswerContent(detail.getAnswerContent());
			
			vo.setFullScore(detail.getFullScore());
			vo.setStatus(detail.getStatus());
			vo.setSubmitTime(detail.getSubmitTime());
			
			vo.setScore(needAnswer?detail.getScore():null);
			vo.setAiEvaluate(needAnswer?detail.getAiEvaluate():null);
			vo.setTeacherEvaluate(needAnswer?detail.getTeacherEvaluate():null);
		}

		return vo;
	}

	private TopicResultVo buildTopicResult(ClassActivityResource resource, ClassActivitySubmitDetail detail,boolean needAnswer) {
		TopicResultVo vo = new TopicResultVo();

		Optional<DiscussionTopic> topicOpt = discussionTopicService.loadById(resource.getResourceId());
		if (topicOpt.isPresent()) {
			DiscussionTopic topic = topicOpt.get();
			vo.setTopicId(topic.getId());
			vo.setTopicName(topic.getTopicName());
			vo.setTopicType(topic.getTopicType());
			vo.setTopicContent(topic.getTopicContent());
			vo.setBackgroundMaterial(topic.getBackgroundMaterial());
			vo.setGuidanceTips(topic.getGuidanceTips());
			vo.setExpectedAnswers(topic.getExpectedAnswers());
			vo.setEvaluationCriteria(topic.getEvaluationCriteria());
			vo.setTopicScore(resource.getScore());
		}

		if (detail != null) {
			vo.setDetailId(detail.getId());
			vo.setAnswerContent(detail.getAnswerContent());
			
			vo.setFullScore(detail.getFullScore());
			vo.setStatus(detail.getStatus());
			vo.setSubmitTime(detail.getSubmitTime());
			
			
//			vo.setScore(detail.getScore());
//			vo.setAiEvaluate(detail.getAiEvaluate());
//			vo.setTeacherEvaluate(detail.getTeacherEvaluate());
			
			
			vo.setScore(needAnswer?detail.getScore():null);
			vo.setAiEvaluate(needAnswer?detail.getAiEvaluate():null);
			vo.setTeacherEvaluate(needAnswer?detail.getTeacherEvaluate():null);
			
		}

		return vo;
	}
}
