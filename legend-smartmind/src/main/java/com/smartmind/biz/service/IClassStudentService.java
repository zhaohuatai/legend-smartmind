package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassStudent;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentCreateDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentUpdateDto;
import com.smartmind.biz.bo.dto.classstudent.ClassStudentPageQueryDto;
public interface IClassStudentService extends IBaseService<ClassStudent>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassStudent(ClassStudentCreateDto classStudentCreateVo,SimpleUserBo simpleUser);

	int updateClassStudent(ClassStudentUpdateDto classStudentUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}