package com.smartmind.biz.router.student;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.legend.framework.web.controller.BaseController;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.core.util.ZStrUtil;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.bo.dto.student.StudentProfileUpdateDto;
import com.smartmind.biz.service.IStudentService;

/**
 * 学生端 - 个人信息接口
 * URL前缀: /student/profile
 */
@Controller
@RequestMapping("/student/profile")
public class StudentProfileController implements BaseController {

	@Autowired
	private IStudentService studentService;

	/**
	 * 获取我的信息
	 */
	@RequestMapping(value="/loadMyInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object loadMyInfo(){
		String studentId = SecurityUtil.getUserId();
		if (ZStrUtil.isBlank(studentId)) {
			return ajaxError("未登录");
		}
		Optional<Student> ops = studentService.loadById(studentId);
		return ops.map(this::ajaxQueryResult).orElse(ajaxError("用户不存在"));
	}

	/**
	 * 更新我的信息
	 */
	@RequestMapping(value="/updateMyInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object updateMyInfo(@RequestBody StudentProfileUpdateDto request){
		String studentId = SecurityUtil.getUserId();
		studentService.updateStudentInfo(studentId, request.getPhone(), request.getEmail(), request.getPhotoUrl());
		return ajaxDoneSuccess("更新成功");
	}
}
