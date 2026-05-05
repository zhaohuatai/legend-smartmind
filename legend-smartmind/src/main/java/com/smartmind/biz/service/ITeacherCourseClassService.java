package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.TeacherCourseClass;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassUpdateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassPageQueryDto;
public interface ITeacherCourseClassService extends IBaseService<TeacherCourseClass>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createTeacherCourseClass(TeacherCourseClassCreateDto teacherCourseClassCreateVo,SimpleUserBo simpleUser);

	// 根据课程创建关联（自动关联班级）
	void createByCourse(TeacherCourseClassBatchCreateDto batchCreateDto, SimpleUserBo simpleUser);

	// 批量创建关联
	void createBatch(TeacherCourseClassBatchDto batchDto, SimpleUserBo simpleUser);

	int updateTeacherCourseClass(TeacherCourseClassUpdateDto teacherCourseClassUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}