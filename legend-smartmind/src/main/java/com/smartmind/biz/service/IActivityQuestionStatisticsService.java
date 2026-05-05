package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ActivityQuestionStatistics;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsUpdateDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsPageQueryDto;
public interface IActivityQuestionStatisticsService extends IBaseService<ActivityQuestionStatistics>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createActivityQuestionStatistics(ActivityQuestionStatisticsCreateDto activityQuestionStatisticsCreateVo,SimpleUserBo simpleUser);

	int updateActivityQuestionStatistics(ActivityQuestionStatisticsUpdateDto activityQuestionStatisticsUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}