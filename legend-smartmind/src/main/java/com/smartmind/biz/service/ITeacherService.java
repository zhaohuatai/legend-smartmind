package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Teacher;
import com.smartmind.biz.bo.dto.teacher.TeacherCreateDto;
import com.smartmind.biz.bo.dto.teacher.TeacherUpdateDto;
import com.smartmind.biz.bo.dto.teacher.TeacherPageQueryDto;
public interface ITeacherService extends IBaseService<Teacher>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createTeacher(TeacherCreateDto teacherCreateVo,SimpleUserBo simpleUser);

	int updateTeacher(TeacherUpdateDto teacherUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}