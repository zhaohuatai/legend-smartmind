package com.smartmind.biz.router.student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.legend.framework.web.controller.BaseController;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.core.util.ZStrUtil;
import com.smartmind.biz.bo.dto.student.StudentStatsOverviewDto;
import com.smartmind.biz.bo.dto.student.StudentTypeStatsDto;
import com.smartmind.biz.bo.dto.student.StudentDailyStudyTimeDto;
import com.smartmind.biz.bo.dto.student.StudentCourseProgressDto;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.service.IClassActivitySubmitService;

/**
 * 学生端 - 学习统计接口
 * URL前缀: /student/stats
 */
@Controller
@RequestMapping("/student/stats")
public class StudentStatsController implements BaseController {

	@Autowired
	private ICourseService courseService;

	@Autowired
	private IClassActivityService classActivityService;

	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	/**
	 * 加载学习统计概览数据
	 */
	@RequestMapping(value="/loadOverview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadOverview(){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}

		StudentStatsOverviewDto dto = new StudentStatsOverviewDto();
		
		int totalCourses = courseService.countCoursesByStudentId(studentId);
		int totalActivities = classActivityService.countActivitiesByStudentId(studentId);
		int completedActivities = classActivityService.countCompletedActivitiesByStudentId(studentId);
		
		BigDecimal avgScore = classActivitySubmitService.calculateAvgScoreByStudentId(studentId);
		double completionRate = totalActivities > 0 ? (completedActivities * 100.0 / totalActivities) : 0;
		double accuracyRate = classActivitySubmitService.calculateAccuracyRateByStudentId(studentId);

		dto.setTotalCourses(totalCourses);
		dto.setTotalActivities(totalActivities);
		dto.setCompletedActivities(completedActivities);
		dto.setAvgScore(avgScore != null ? avgScore.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
		dto.setCompletionRate((int) Math.round(completionRate));
		dto.setAccuracyRate((int) Math.round(accuracyRate));
		dto.setOverallScore(avgScore != null ? avgScore.setScale(0, RoundingMode.HALF_UP).intValue() : 0);

		return ajaxQueryResult(dto);
	}

	/**
	 * 加载题型表现统计
	 */
	@RequestMapping(value="/loadTypeStats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadTypeStats(){
		String userId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(userId)) {
			return ajaxError("未登录");
		}

		List<StudentTypeStatsDto> typeStats = classActivitySubmitService.calculateTypeStatsByStudentId(userId);
		return ajaxQueryResult(typeStats);
	}

	/**
	 * 加载最近7天学习时长
	 */
	@RequestMapping(value="/loadWeekStudyTime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadWeekStudyTime(){
		String userId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(userId)) {
			return ajaxError("未登录");
		}

		List<StudentDailyStudyTimeDto> weekData = classActivitySubmitService.calculateWeekStudyTimeByStudentId(userId);
		return ajaxQueryResult(weekData);
	}

	/**
	 * 加载课程完成度
	 */
	@RequestMapping(value="/loadCourseProgress", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadCourseProgress(){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}

		List<StudentCourseProgressDto> courseProgress = courseService.loadCourseProgressByStudentId(studentId);
		return ajaxQueryResult(courseProgress);
	}
}
