package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassActivity;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityCreateDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityUpdateDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityPageQueryDto;
public interface IClassActivityService extends IBaseService<ClassActivity>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassActivity(ClassActivityCreateDto classActivityCreateVo,SimpleUserBo simpleUser);

	int updateClassActivity(ClassActivityUpdateDto classActivityUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);

	List<ClassActivity> queryBySessionId(Long sessionId);
 
	ClassActivityUpdateDto loadforUpdateById(Long id);

	void startActivity(Long activityId, SimpleUserBo simpleUser);

	void deleteActivity(Long activityId);
	
	/**
	 * 根据课堂ID和学生ID查询活动列表
	 * @param sessionId 课堂ID
	 * @param studentId 学生ID
	 * @return 活动列表
	 */
	List<ClassActivity> loadActivitiesBySessionAndStudentId(Long sessionId, String studentId);
	
	/**
	 * 统计学生参与活动数量
	 * @param studentId 学生ID
	 * @return 活动数量
	 */
	int countActivitiesByStudentId(String studentId);
	
	/**
	 * 统计学生已完成活动数量
	 * @param studentId 学生ID
	 * @return 已完成活动数量
	 */
	int countCompletedActivitiesByStudentId(String studentId);
}