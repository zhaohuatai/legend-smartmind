package com.smartmind.biz.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitUpdateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitPageQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitConvert;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmissionVo;
import com.smartmind.biz.bo.dto.classactivitysubmit.ActivityStatsVo;
import com.smartmind.biz.bo.dto.student.StudentTypeStatsDto;
import com.smartmind.biz.bo.dto.student.StudentDailyStudyTimeDto;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.dao.ClassActivitySubmitMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.dao.ClassActivitySubmitDetailMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.dao.ClassActivityMapper;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.dao.ClassStudentMapper;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.dao.ClassActivityResourceMapper;
import com.smartmind.biz.bo.model.ClassActivityResource;
@Service
public class ClassActivitySubmitServiceImpl extends BaseServiceImpl<ClassActivitySubmitMapper, ClassActivitySubmit> implements IClassActivitySubmitService{
 	@Autowired
	private ClassActivitySubmitMapper classActivitySubmitMapper;
	
	@Autowired
	private ClassActivitySubmitDetailMapper classActivitySubmitDetailMapper;
	
	@Autowired
	private ClassActivityMapper classActivityMapper;
	
	@Autowired
	private ClassStudentMapper classStudentMapper;
	
	@Autowired
	private ClassActivityResourceMapper classActivityResourceMapper;
	
	@Override
	public BaseMapper<ClassActivitySubmit> getMapper() {
		return classActivitySubmitMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassActivitySubmit(ClassActivitySubmitCreateDto classActivitySubmitCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivitySubmitCreateDto);
		ClassActivitySubmit classActivitySubmit = ClassActivitySubmitConvert.INSTANCE.convert(classActivitySubmitCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( classActivitySubmit, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	   
		return create(classActivitySubmit);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassActivitySubmit(ClassActivitySubmitUpdateDto classActivitySubmitCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivitySubmitCreateDto);
		ClassActivitySubmit classActivitySubmit = ClassActivitySubmitConvert.INSTANCE.convert(classActivitySubmitCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( classActivitySubmit, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	
		int result = updateById(classActivitySubmit);
		if (classActivitySubmit.getActivityId() != null) {
			updateActivityStats(classActivitySubmit.getActivityId());
		}
		return result;
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusListDto statusListDto) {
		ZBeanUtil.validateBean(statusListDto);
		setStatusByIds(statusListDto.getIds(),  statusListDto.getStatus()) ;
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusDto status) {
		ZBeanUtil.validateBean(status);
		setStatusById(status.getId(),  status.getStatus()) ;
	}
	
	@Override
	public List<StudentSubmissionVo> getStudentSubmissions(Long activityId) {
		ClassActivity activity = classActivityMapper.selectById(activityId);
		if (activity == null) {
			return new ArrayList<>();
		}
		
		List<ClassStudent> students = classStudentMapper.selectByClazzId(activity.getClazzId());
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByActivityId(activityId);
		if(submits==null||submits.isEmpty()) {
			return List.of();
		}
		Map<String, ClassActivitySubmit> submitMap = new HashMap<>();
		for (ClassActivitySubmit submit : submits) {
			submitMap.put(submit.getStudentId(), submit);
		}
		
		List<StudentSubmissionVo> result = new ArrayList<>();
		for (ClassStudent classtudent : students) {
			StudentSubmissionVo vo = new StudentSubmissionVo();
			vo.setStudentId(classtudent.getStudentId());
			vo.setStudentName(classtudent.getStudentName());
			vo.setStudentNo(classtudent.getStudentNo());
			
			ClassActivitySubmit submit = submitMap.get(classtudent.getStudentId());
			if (submit != null && submit.getStatus() != null && !"0".equals(submit.getStatus())) {
				vo.setSubmitted(true);
				vo.setSubmitId(submit.getId());
				vo.setScore(submit.getScore());
				vo.setStatus(submit.getStatus());
				vo.setSubmitTime(submit.getSubmitTime());
				vo.setSubmitCount(submit.getSubmitCount());
			} else {
				vo.setSubmitted(false);
			}
			result.add(vo);
		}
		return result;
	}
	
	@Override
	public ActivityStatsVo getActivityStats(Long activityId) {
		ClassActivity activity = classActivityMapper.selectById(activityId);
		if (activity == null) {
			return new ActivityStatsVo();
		}
		
		List<ClassStudent> students = classStudentMapper.selectByClazzId(activity.getClazzId());
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByActivityId(activityId);
		if(submits==null||submits.isEmpty()){
			ActivityStatsVo stats = new ActivityStatsVo();
			stats.setTotalStudents(0);
			stats.setSubmittedCount(0);
			stats.setUnsubmittedCount(0);
			stats.setSubmitRate(0);
			stats.setAvgScore(new BigDecimal(0));
			return stats;
		}
		int totalStudents = students.size();
		int submittedCount = 0;
		BigDecimal totalScoreSum = BigDecimal.ZERO;
		int scoredCount = 0;
		
		for (ClassActivitySubmit submit : submits) {
			if (submit.getStatus() != null && !"0".equals(submit.getStatus())) {
				submittedCount++;
			}
			if (submit.getScore() != null) {
				totalScoreSum = totalScoreSum.add(submit.getScore());
				scoredCount++;
			}
		}
		
		int unsubmittedCount = totalStudents - submittedCount;
		int submitRate = totalStudents > 0 ? (int) Math.round((double) submittedCount / totalStudents * 100) : 0;
		BigDecimal avgScore = scoredCount > 0 ? totalScoreSum.divide(BigDecimal.valueOf(scoredCount), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
		
		ActivityStatsVo stats = new ActivityStatsVo();
		stats.setTotalStudents(totalStudents);
		stats.setSubmittedCount(submittedCount);
		stats.setUnsubmittedCount(unsubmittedCount);
		stats.setSubmitRate(submitRate);
		stats.setAvgScore(avgScore);
		
		return stats;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateActivityStats(Long activityId) {
		if (activityId == null) {
			return;
		}
		ClassActivity activity = classActivityMapper.selectById(activityId);
		if (activity == null) {
			return;
		}
		
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByActivityId(activityId);
		
		int submittedCount = 0;
		BigDecimal totalScoreSum = BigDecimal.ZERO;
		int scoredCount = 0;
		
		for (ClassActivitySubmit submit : submits) {
			if (submit.getStatus() != null && !"0".equals(submit.getStatus())) {
				submittedCount++;
			}
			if (submit.getScore() != null) {
				totalScoreSum = totalScoreSum.add(submit.getScore());
				scoredCount++;
			}
		}
		
		BigDecimal avgScore = scoredCount > 0 ? totalScoreSum.divide(BigDecimal.valueOf(scoredCount), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
		
		activity.setSubmitStuCount(submittedCount);
		activity.setAvgScore(avgScore);
		activity.setUpdateTime(new java.util.Date());
		classActivityMapper.updateById(activity);
	}

	@Override
	public ClassActivitySubmit selectByActivityAndStudent(Long activityId, String studentId) {
		if (activityId == null || studentId == null) {
			return null;
		}
		ChainWrapper<ClassActivitySubmit> qw = ChainWrapper.create(ClassActivitySubmit.class)
			.where(ClassActivitySubmit::getActivityId, "=", activityId)
			.where(ClassActivitySubmit::getStudentId, "=", studentId);
		List<ClassActivitySubmit> list = classActivitySubmitMapper.selectByQuery(qw);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public BigDecimal calculateAvgScoreByStudentId(String studentId) {
		if (studentId == null) {
			return BigDecimal.ZERO;
		}
		ChainWrapper<ClassActivitySubmit> qw = ChainWrapper.create(ClassActivitySubmit.class)
			.where(ClassActivitySubmit::getStudentId, "=", studentId)
			.where(ClassActivitySubmit::getStatus, "=", "1");
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(qw);
		if (submits == null || submits.isEmpty()) {
			return BigDecimal.ZERO;
		}
		BigDecimal totalScore = BigDecimal.ZERO;
		int count = 0;
		for (ClassActivitySubmit submit : submits) {
			if (submit.getTotalScore() != null) {
				totalScore = totalScore.add(submit.getTotalScore());
				count++;
			}
		}
		return count > 0 ? totalScore.divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public double calculateAccuracyRateByStudentId(String studentId) {
		if (studentId == null) {
			return 0;
		}
		ChainWrapper<ClassActivitySubmit> submitQw = ChainWrapper.create(ClassActivitySubmit.class)
			.where(ClassActivitySubmit::getStudentId, "=", studentId)
			.where(ClassActivitySubmit::getStatus, "=", "1");
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(submitQw);
		if (submits == null || submits.isEmpty()) {
			return 0;
		}
		List<Long> submitIds = new ArrayList<>();
		for (ClassActivitySubmit submit : submits) {
			submitIds.add(submit.getId());
		}
		ChainWrapper<ClassActivitySubmitDetail> detailQw = ChainWrapper.create(ClassActivitySubmitDetail.class)
			.where(ClassActivitySubmitDetail::getSubmitId, "IN", submitIds);
		List<ClassActivitySubmitDetail> details = classActivitySubmitDetailMapper.selectByQuery(detailQw);
		if (details == null || details.isEmpty()) {
			return 0;
		}
		int correctCount = 0;
		int totalCount = details.size();
		for (ClassActivitySubmitDetail detail : details) {
			if (detail.getFullScore() != null && detail.getFullScore().compareTo(BigDecimal.ZERO) > 0) {
				if (detail.getScore() != null && detail.getScore().compareTo(detail.getFullScore()) >= 0) {
					correctCount++;
				}
			}
		}
		return totalCount > 0 ? (correctCount * 100.0 / totalCount) : 0;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<StudentTypeStatsDto> calculateTypeStatsByStudentId(String studentId) {
		if (studentId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassActivitySubmit> submitQw = ChainWrapper.create(ClassActivitySubmit.class)
			.where(ClassActivitySubmit::getStudentId, "=", studentId)
			.where(ClassActivitySubmit::getStatus, "=", "1");
		List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(submitQw);
		if (submits == null || submits.isEmpty()) {
			return Lists.newArrayList();
		}
		Map<String, List<ClassActivitySubmit>> submitByActivity = new HashMap<>();
		for (ClassActivitySubmit submit : submits) {
			submitByActivity.computeIfAbsent(submit.getActivityId().toString(), k -> new ArrayList<>()).add(submit);
		}
		Map<String, StudentTypeStatsDto> typeStatsMap = new HashMap<>();
		String[] typeNames = {"单选题", "多选题", "判断题", "填空题", "简答题", "计算题", "应用题", "综合题"};
		String[] typeColors = {"#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399", "#409EFF", "#67C23A", "#E6A23C"};
		for (int i = 1; i <= 8; i++) {
			String type = String.valueOf(i);
			StudentTypeStatsDto dto = new StudentTypeStatsDto();
			dto.setType(type);
			dto.setName(typeNames[i - 1]);
			dto.setColor(typeColors[i - 1]);
			dto.setAccuracy(0);
			typeStatsMap.put(type, dto);
		}
		for (ClassActivitySubmit submit : submits) {
			ChainWrapper<ClassActivitySubmitDetail> detailQw = ChainWrapper.create(ClassActivitySubmitDetail.class)
				.where(ClassActivitySubmitDetail::getSubmitId, "=", submit.getId());
			List<ClassActivitySubmitDetail> details = classActivitySubmitDetailMapper.selectByQuery(detailQw);
			if (details != null && !details.isEmpty()) {
				for (ClassActivitySubmitDetail detail : details) {
					String resourceType = detail.getResourceType();
					if (resourceType != null && typeStatsMap.containsKey(resourceType)) {
						StudentTypeStatsDto dto = typeStatsMap.get(resourceType);
						if (detail.getFullScore() != null && detail.getFullScore().compareTo(BigDecimal.ZERO) > 0) {
							if (detail.getScore() != null && detail.getScore().compareTo(detail.getFullScore()) >= 0) {
								dto.setAccuracy(dto.getAccuracy() + 1);
							}
						}
					}
				}
			}
		}
		List<StudentTypeStatsDto> result = new ArrayList<>();
		for (StudentTypeStatsDto dto : typeStatsMap.values()) {
			result.add(dto);
		}
		return result;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, readOnly=true)
	public List<StudentDailyStudyTimeDto> calculateWeekStudyTimeByStudentId(String studentId) {
		if (studentId == null) {
			return Lists.newArrayList();
		}
		List<StudentDailyStudyTimeDto> result = Lists.newArrayList();
		String[] weekLabels = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
		LocalDate today = LocalDate.now();
		int maxMinutes = 1;
		for (int i = 6; i >= 0; i--) {
			LocalDate date = today.minusDays(i);
			java.util.Date startDate = java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			java.util.Date endDate = java.util.Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			ChainWrapper<ClassActivitySubmit> qw = ChainWrapper.create(ClassActivitySubmit.class)
				.where(ClassActivitySubmit::getStudentId, "=", studentId)
				.where(ClassActivitySubmit::getStatus, "=", "1")
				.where("submit_time", ">=", startDate)
				.where("submit_time", "<", endDate);
			List<ClassActivitySubmit> submits = classActivitySubmitMapper.selectByQuery(qw);
			int minutes = 0;
			if (submits != null && !submits.isEmpty()) {
				for (ClassActivitySubmit submit : submits) {
					if (submit.getSubmitTime() != null) {
						minutes += 10;
					}
				}
			}
			if (minutes > maxMinutes) {
				maxMinutes = minutes;
			}
			StudentDailyStudyTimeDto dto = new StudentDailyStudyTimeDto();
			dto.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			dto.setLabel(weekLabels[date.getDayOfWeek().getValue() - 1]);
			dto.setMinutes(minutes);
			dto.setHeight((int) (minutes * 100.0 / maxMinutes));
			result.add(dto);
		}
		return result;
	}
}