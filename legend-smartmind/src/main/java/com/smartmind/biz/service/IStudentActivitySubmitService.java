package com.smartmind.biz.service;

import com.smartmind.biz.bo.dto.classactivitysubmit.BatchSubmitRequestDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmitRequestDto;

public interface IStudentActivitySubmitService {

	void submitAnswer(StudentSubmitRequestDto answer);

	void batchSubmitAnswer(BatchSubmitRequestDto batchRequest);
}
