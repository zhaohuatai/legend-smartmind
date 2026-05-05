package com.smartmind.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.legend.framework.core.exceptions.ServiceLogicalException;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartmind.biz.bo.dto.classactivitysubmit.BatchSubmitRequestDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmitRequestDto;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.service.IClassActivitySubmitDetailService;
import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.service.IQuestionService;
import com.smartmind.biz.service.IStudentActivitySubmitService;
import com.smartmind.biz.service.IStudentActivitySubmitAsyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentActivitySubmitServiceImpl implements IStudentActivitySubmitService {

	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	@Autowired
	private IClassActivitySubmitDetailService classActivitySubmitDetailService;

	@Autowired
	private IClassActivityService classActivityService;

	@Autowired
	private IQuestionService questionService;

	@Autowired
	private IStudentActivitySubmitAsyncService asyncService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void submitAnswer(StudentSubmitRequestDto studentAnswer) {
		if (studentAnswer == null || studentAnswer.getStudentId() == null) {
			throw new ServiceLogicalException("学生ID不能为空");
		}

		if (studentAnswer.getSubmitId() == null) {
			throw new ServiceLogicalException("提交记录ID不能为空");
		}
		if (studentAnswer.getDetailId() == null) {
			throw new ServiceLogicalException("答题记录ID不能为空");
		}
		ClassActivitySubmit submit = classActivitySubmitService.loadById( studentAnswer.getSubmitId())
										.orElseThrow(()->ZAlert.newSLE("未找到学生提交记录，请先开始活动"));
		
		Optional<ClassActivity> activityOpt = classActivityService.loadById(submit.getActivityId());
		if (activityOpt.isPresent()) {
			ClassActivity activity = activityOpt.get();
			if ("2".equals(activity.getStatus())) {
				throw new ServiceLogicalException("活动已结束，无法提交");
			}
			if ("3".equals(activity.getStatus())) {
				throw new ServiceLogicalException("活动已取消，无法提交");
			}
			if (activity.getEndTime() != null && new Date().after(activity.getEndTime())) {
				throw new ServiceLogicalException("已超过截止时间，无法提交");
			}
		}
		
		
		ClassActivitySubmitDetail detail = classActivitySubmitDetailService.loadById(studentAnswer.getDetailId())
				.orElseThrow(()->ZAlert.newSLE("未查询到Submit记录"));
		
		ZAssert.equals(detail.getSubmitId(), submit.getId(), "数据不对应");
		ZAssert.equals(submit.getStudentId(), studentAnswer.getStudentId(), "请不要提交别人的答案");

		if (submit.getSubmitTime() != null) {
			long interval = new Date().getTime() - submit.getSubmitTime().getTime();
			if (interval < 45000) {
				throw new ServiceLogicalException("提交过于频繁，请等待45秒后再提交");
			}
		}

		
		detail.setAnswerContent(studentAnswer.getAnswerContent());
		detail.setSubmitTime(new Date());
		detail.setStatus("1");

		if ("1".equals(detail.getResourceType())) {
			
			handleQuestionSubmit(detail);
			
		} else if ("3".equals(detail.getResourceType())) {
			
			handleExperimentSubmit(detail);
			
		} else if ("2".equals(detail.getResourceType())) {
			
			handleDiscussionSubmit(detail);
			
		}

		classActivitySubmitDetailService.updateById(detail);

		updateSubmitStats(submit);

		asyncService.triggerGrading(detail.getId(), detail.getResourceType());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSubmitAnswer(BatchSubmitRequestDto batchRequest) {
		if (batchRequest == null || batchRequest.getStudentId() == null) {
			throw new ServiceLogicalException("学生ID不能为空");
		}
		if (batchRequest.getSubmitId() == null) {
			throw new ServiceLogicalException("提交记录ID不能为空");
		}
		if (batchRequest.getAnswers() == null || batchRequest.getAnswers().isEmpty()) {
			throw new ServiceLogicalException("答案列表不能为空");
		}

		ClassActivitySubmit submit = classActivitySubmitService.loadById(batchRequest.getSubmitId())
				.orElseThrow(() -> ZAlert.newSLE("未找到学生提交记录，请先开始活动"));
		ZAssert.equals(submit.getStudentId(), batchRequest.getStudentId(), "请不要提交别人的答案");

		Optional<ClassActivity> activityOpt = classActivityService.loadById(submit.getActivityId());
		if (activityOpt.isPresent()) {
			ClassActivity activity = activityOpt.get();
			if ("2".equals(activity.getStatus())) {
				throw new ServiceLogicalException("活动已结束，无法提交");
			}
			if ("3".equals(activity.getStatus())) {
				throw new ServiceLogicalException("活动已取消，无法提交");
			}
			if (activity.getEndTime() != null && new Date().after(activity.getEndTime())) {
				throw new ServiceLogicalException("已超过截止时间，无法提交");
			}
		}

		for (BatchSubmitRequestDto.AnswerItem item : batchRequest.getAnswers()) {
			if (item.getDetailId() == null) {
				throw new ServiceLogicalException("答题记录ID不能为空");
			}

			StudentSubmitRequestDto singleRequest = new StudentSubmitRequestDto();
			singleRequest.setStudentId(batchRequest.getStudentId());
			singleRequest.setSubmitId(batchRequest.getSubmitId());
			singleRequest.setDetailId(item.getDetailId());
			singleRequest.setAnswerContent(item.getAnswerContent());

			submitAnswer(singleRequest);
		}
	}



	private void handleQuestionSubmit(ClassActivitySubmitDetail detail) {
		
		Long questionId = detail.getResourceId();
		
		Optional<Question> questionOpt = questionService.loadById(questionId);
		if (!questionOpt.isPresent()) {
			return;
		}

		Question question = questionOpt.get();

		String questionType = question.getQuestionType();
		if ("1".equals(questionType) || "2".equals(questionType) || "3".equals(questionType) || "4".equals(questionType)) {
			String studentAnswer = detail.getAnswerContent() != null ? detail.getAnswerContent().trim() : "";
			String correctAnswer = question.getAnswer() != null ? question.getAnswer().trim() : "";

			boolean isCorrect = false;
			if ("1".equals(questionType) || "3".equals(questionType)) {
				isCorrect = studentAnswer.equalsIgnoreCase(correctAnswer);
			} else if ("2".equals(questionType)) {
				String sortedStudent = sortAnswer(studentAnswer);
				String sortedCorrect = sortAnswer(correctAnswer);
				isCorrect = sortedStudent.equals(sortedCorrect);
			} else if ("4".equals(questionType)) {
				isCorrect = studentAnswer.equals(correctAnswer);
			}

			if (isCorrect) {
				detail.setIsCorrect("1");
				detail.setScore(detail.getFullScore());
			} else {
				detail.setIsCorrect("0");
				detail.setScore(BigDecimal.ZERO);
			}
			detail.setStatus("2");
			detail.setGradeTime(new Date());
			detail.setGradeBy("system");
		}
	}

	private String sortAnswer(String answer) {
		char[] chars = answer.replaceAll("[,，;；\\s]", "").toCharArray();
		java.util.Arrays.sort(chars);
		return new String(chars);
	}

	private void handleExperimentSubmit(ClassActivitySubmitDetail detail) {
		detail.setIsCorrect(null);
		detail.setScore(null);
		detail.setStatus("1");
	}

	private void handleDiscussionSubmit(ClassActivitySubmitDetail detail) {
		detail.setIsCorrect(null);
		detail.setScore(null);
		detail.setStatus("1");
	}

	private void updateSubmitStats(ClassActivitySubmit submit) {
		List<ClassActivitySubmitDetail> details = classActivitySubmitDetailService.selectBySubmitId(submit.getId());

		BigDecimal totalScore = BigDecimal.ZERO;
		int submitCount = 0;
		int gradedCount = 0;

		if (details != null && !details.isEmpty()) {
			for (ClassActivitySubmitDetail detail : details) {
				if (detail.getStatus() != null && !"0".equals(detail.getStatus())) {
					submitCount++;
				}
				if ("2".equals(detail.getStatus()) && detail.getScore() != null) {
					totalScore = totalScore.add(detail.getScore());
					gradedCount++;
				}
			}
		}

		submit.setSubmitCount(submitCount);
		submit.setScore(totalScore);
		submit.setSubmitTime(new Date());

		boolean allGraded = details != null && !details.isEmpty() && gradedCount == details.size();
		if (allGraded) {
			submit.setStatus("2");
		} else if (submitCount > 0) {
			submit.setStatus("1");
		}

		classActivitySubmitService.updateById(submit);

//		if (submit.getActivityId() != null) {
//			Optional<ClassActivity> activityOpt = classActivityService.loadById(submit.getActivityId());
//			if (activityOpt.isPresent()) {
//				ClassActivity activity = activityOpt.get();
//				activity.setUpdateTime(new Date());
//				classActivityService.updateById(activity);
//			}
//		}
	}
}
