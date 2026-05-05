package com.smartmind.biz.service;

import com.smartmind.biz.bo.dto.classactivity.detail.StudentActivityDetailVo;

public interface IStudentActivityDetailQueryService {

	StudentActivityDetailVo queryStudentActivityDetail(Long activityId, String studentId, boolean needAnswer);
}
