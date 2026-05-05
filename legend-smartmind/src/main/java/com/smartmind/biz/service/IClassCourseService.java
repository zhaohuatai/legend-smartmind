package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassCourse;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseCreateDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseUpdateDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCoursePageQueryDto;
public interface IClassCourseService extends IBaseService<ClassCourse>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);

   	int createClassCourse(ClassCourseCreateDto classCourseCreateVo,SimpleUserBo simpleUser);

	int updateClassCourse(ClassCourseUpdateDto classCourseUpdateVo,SimpleUserBo simpleUser);

    void setStatus(StatusListDto statusListDto);

    void setStatus(StatusDto status);

    /**
     * 查询课程关联的班级ID列表
     */
    List<Long> queryClassIdsByCourseId(Long courseId);

    /**
     * 保存课程班级关联（先删除旧关联，再保存新关联）
     */
    void saveCourseClassRelation(Long courseId, List<Long> classIds);

	List<ClassCourse> queryCourseClassByCourseId(Long courseId);
}