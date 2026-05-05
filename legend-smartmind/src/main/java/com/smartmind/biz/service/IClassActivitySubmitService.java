package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitUpdateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitPageQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmissionVo;
import com.smartmind.biz.bo.dto.classactivitysubmit.ActivityStatsVo;
import com.smartmind.biz.bo.dto.student.StudentTypeStatsDto;
import com.smartmind.biz.bo.dto.student.StudentDailyStudyTimeDto;
public interface IClassActivitySubmitService extends IBaseService<ClassActivitySubmit>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassActivitySubmit(ClassActivitySubmitCreateDto classActivitySubmitCreateVo,SimpleUserBo simpleUser);

	int updateClassActivitySubmit(ClassActivitySubmitUpdateDto classActivitySubmitUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    List<StudentSubmissionVo> getStudentSubmissions(Long activityId);
    
    ActivityStatsVo getActivityStats(Long activityId);
    
    void updateActivityStats(Long activityId);
    
    ClassActivitySubmit selectByActivityAndStudent(Long activityId, String studentId);
    
    /**
     * 计算学生平均得分
     * @param studentId 学生ID
     * @return 平均得分
     */
    BigDecimal calculateAvgScoreByStudentId(String studentId);
    
    /**
     * 计算学生正确率
     * @param studentId 学生ID
     * @return 正确率(百分比)
     */
    double calculateAccuracyRateByStudentId(String studentId);
    
    /**
     * 计算学生各题型表现统计
     * @param studentId 学生ID
     * @return 题型表现列表
     */
    List<StudentTypeStatsDto> calculateTypeStatsByStudentId(String studentId);
    
    /**
     * 计算学生最近7天学习时长
     * @param studentId 学生ID
     * @return 每日学习时长列表
     */
    List<StudentDailyStudyTimeDto> calculateWeekStudyTimeByStudentId(String studentId);
}