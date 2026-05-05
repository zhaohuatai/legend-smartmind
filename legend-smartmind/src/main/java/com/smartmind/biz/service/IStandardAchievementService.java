package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.StandardAchievement;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementCreateDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementUpdateDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementPageQueryDto;
public interface IStandardAchievementService extends IBaseService<StandardAchievement>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createStandardAchievement(StandardAchievementCreateDto standardAchievementCreateVo,SimpleUserBo simpleUser);

	int updateStandardAchievement(StandardAchievementUpdateDto standardAchievementUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}