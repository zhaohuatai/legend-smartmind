package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.bo.dto.student.StudentCreateDto;
import com.smartmind.biz.bo.dto.student.StudentUpdateDto;
import com.smartmind.biz.bo.dto.student.StudentPageQueryDto;
public interface IStudentService extends IBaseService<Student>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createStudent(StudentCreateDto studentCreateVo,SimpleUserBo simpleUser);

	int updateStudent(StudentUpdateDto studentUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    /**
     * 更新学生个人信息（仅更新phone、email、photoUrl）
     * @param studentId 学生ID
     * @param phone 联系电话
     * @param email 邮箱
     * @param photoUrl 照片地址
     */
    void updateStudentInfo(String studentId, String phone, String email, String photoUrl);

	int batchCreate(List<StudentCreateDto> students);
}