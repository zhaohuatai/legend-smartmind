package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ActivitySubmitStatistics;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsUpdateDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsPageQueryDto;
public interface IActivitySubmitStatisticsService extends IBaseService<ActivitySubmitStatistics>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createActivitySubmitStatistics(ActivitySubmitStatisticsCreateDto activitySubmitStatisticsCreateVo,SimpleUserBo simpleUser);

	int updateActivitySubmitStatistics(ActivitySubmitStatisticsUpdateDto activitySubmitStatisticsUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}