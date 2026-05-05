package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassSession;
import com.smartmind.biz.bo.dto.classsession.ClassSessionCreateDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionUpdateDto;
import com.smartmind.biz.bo.dto.classsession.ClassSessionPageQueryDto;
public interface IClassSessionService extends IBaseService<ClassSession>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassSession(ClassSessionCreateDto classSessionCreateVo,SimpleUserBo simpleUser);

	int updateClassSession(ClassSessionUpdateDto classSessionUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    /**
     * 根据课程ID和学生ID查询课堂列表
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 课堂列表
     */
    List<ClassSession> loadSessionsByCourseAndStudentId(Long courseId, String studentId);
}