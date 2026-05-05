package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.dto.course.CourseCreateDto;
import com.smartmind.biz.bo.dto.course.CourseUpdateDto;
import com.smartmind.biz.bo.dto.course.CoursePageQueryDto;
import com.smartmind.biz.bo.dto.student.StudentCourseProgressDto;
public interface ICourseService extends IBaseService<Course>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
	Long createCourse(CourseCreateDto courseCreateVo,SimpleUserBo simpleUser);

	Long updateCourse(CourseUpdateDto courseUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    /**
     * 更新课程知识框架
     * @param id 课程ID
     * @param knowledgeFramework 知识框架内容
     */
    void updateKnowledgeFramework(Long id, String knowledgeFramework);
    
    /**
     * 根据学生ID查询其所在班级的所有课程
     * @param studentId 学生ID
     * @return 课程列表
     */
    List<Course> loadCoursesByStudentId(String studentId);
    
    /**
     * 统计学生课程数量
     * @param studentId 学生ID
     * @return 课程数量
     */
    int countCoursesByStudentId(String studentId);
    
    /**
     * 加载学生课程完成度
     * @param studentId 学生ID
     * @return 课程完成度列表
     */
    List<StudentCourseProgressDto> loadCourseProgressByStudentId(String studentId);
}