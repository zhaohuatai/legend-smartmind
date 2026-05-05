package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.AbilityTransition;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionCreateDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionUpdateDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionPageQueryDto;
public interface IAbilityTransitionService extends IBaseService<AbilityTransition>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createAbilityTransition(AbilityTransitionCreateDto abilityTransitionCreateVo,SimpleUserBo simpleUser);

	int updateAbilityTransition(AbilityTransitionUpdateDto abilityTransitionUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}