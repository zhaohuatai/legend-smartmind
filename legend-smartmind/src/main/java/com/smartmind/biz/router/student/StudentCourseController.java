package com.smartmind.biz.router.student;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.legend.framework.web.controller.BaseController;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.core.util.ZStrUtil;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.bo.model.ClassSession;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.dto.classactivity.detail.StudentActivityDetailVo;
import com.smartmind.biz.bo.dto.classactivitysubmit.BatchSubmitRequestDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmitRequestDto;
import com.smartmind.biz.service.IClassActivityService;
import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.service.IClassSessionService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.IStudentActivityDetailQueryService;
import com.smartmind.biz.service.IStudentActivitySubmitService;

/**
 * 学生端 - 课程相关接口
 *
 */
@Controller
@RequestMapping("/student/course")
public class StudentCourseController implements BaseController {

	@Autowired
	private IClassActivityService classActivityService;

	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	@Autowired
	private IStudentActivityDetailQueryService studentActivityDetailQueryService;
 
	@Autowired
	private  IStudentActivitySubmitService studentActivitySubmitService;
	@Autowired
	private ICourseService courseService;

	@Autowired
	private IClassSessionService classSessionService;
	
	/**
	 * 获取我的课程列表
	 */
	@RequestMapping(value="/loadStudentCourses", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadStudentCourses(){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		List<Course> courses = courseService.loadCoursesByStudentId(studentId);
		return ajaxQueryResult(courses);
	}

	/**
	 * 获取课程详情
	 */
	@RequestMapping(value="/loadCourseDetail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadCourseDetail(@PathVariable Long id){
		return ajaxQueryResult(courseService.loadById(id));
	}

	/**
	 * 获取课程的课堂列表
	 */
	@RequestMapping(value="/loadSessions/{courseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadSessionList(@PathVariable Long courseId){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		List<ClassSession> sessions = classSessionService.loadSessionsByCourseAndStudentId(courseId, studentId);
		return ajaxQueryResult(sessions);
	}
	
	
	/**
	 * 获取课堂的活动列表
	 */
	@RequestMapping(value="/loadActivityList/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadActivityList(@PathVariable Long sessionId){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		List<ClassActivity> activities = classActivityService.loadActivitiesBySessionAndStudentId(sessionId, studentId);
		return ajaxQueryResult(activities);
	}

	
	/**
	 * 提交答案（单题）
	 */
	@RequestMapping(value="/submitAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object submitAnswer(@RequestBody StudentSubmitRequestDto request){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		request.setStudentId(studentId);
		studentActivitySubmitService.submitAnswer(request);
		return ajaxDoneSuccess("提交成功");
	}

	/**
	 * 批量提交答案
	 */
	@RequestMapping(value="/batchSubmitAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object batchSubmitAnswer(@RequestBody BatchSubmitRequestDto request){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		request.setStudentId(studentId);
		studentActivitySubmitService.batchSubmitAnswer(request);
		return ajaxDoneSuccess("提交成功");
	}
	

	/**
	 * 获取活动详情(包含资源列表+学生提交情况)
	 * 复用教师端 loadStudentActivityDetail 的逻辑
	 */
	@RequestMapping(value="/loadStudentActivityDetail/{activityId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadStudentActivityDetail(@PathVariable Long activityId){
		if (activityId == null) {
			return ajaxError("活动ID不能为空");
		}
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		ClassActivitySubmit submit =  classActivitySubmitService.selectByActivityAndStudent(activityId, studentId);
		if(submit==null) {
			return ajaxError("未初始化课堂活动");
		}
		
		Optional<ClassActivity> activityOpt = classActivityService.loadById(activityId);
		if (!activityOpt.isPresent()) {
			return ajaxError("活动不存在");
		}
		ClassActivity activity = activityOpt.get();
		
		Boolean needAnswer = false;
		if ("2".equals(submit.getStatus())) {
			needAnswer = true;
		}
		if ("2".equals(activity.getStatus())) {
			needAnswer = true;
		}
		if (activity.getEndTime() != null && new Date().after(activity.getEndTime())) {
			needAnswer = true;
		}
		
		StudentActivityDetailVo detail = studentActivityDetailQueryService.queryStudentActivityDetail(activityId, studentId, needAnswer);
		detail.setActivityStatus(activity.getStatus());
		return ajaxQueryResult(detail);
	}
}
