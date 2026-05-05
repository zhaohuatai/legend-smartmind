package com.smartmind.biz.service;

public interface IStudentActivitySubmitAsyncService {

	void triggerGrading(Long detailId, String resourceType);
}
