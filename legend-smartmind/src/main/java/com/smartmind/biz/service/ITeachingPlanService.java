package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.TeachingPlan;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanContentUpdateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanCreateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanUpdateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanPageQueryDto;
public interface ITeachingPlanService extends IBaseService<TeachingPlan>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	Long createTeachingPlan(TeachingPlanCreateDto teachingPlanCreateVo,SimpleUserBo simpleUser);

	int updateTeachingPlan(TeachingPlanUpdateDto teachingPlanUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
	Optional<TeachingPlan> loadByPlanCode(java.lang.String planCode);

	List<TeachingPlan> loadByCourseIdAndUnitCode(Long courseId, String unitCode);

	void updateMarkdownContent(TeachingPlanContentUpdateDto dto);
    
}