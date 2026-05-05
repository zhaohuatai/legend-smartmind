package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Courseware;
import com.smartmind.biz.bo.dto.courseware.CoursewareCreateDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareUpdateDto;
import com.smartmind.biz.bo.dto.courseware.CoursewarePageQueryDto;
import reactor.core.publisher.Flux;
public interface ICoursewareService extends IBaseService<Courseware>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createCourseware(CoursewareCreateDto coursewareCreateVo,SimpleUserBo simpleUser);

	int updateCourseware(CoursewareUpdateDto coursewareUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);

    List<Courseware> loadByCourseIdAndUnitCode(Long courseId, String unitCode);

    void batchSave(List<Courseware> coursewareList, org.legend.framework.core.auth.SimpleUserBo simpleUser);



}