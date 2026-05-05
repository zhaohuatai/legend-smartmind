package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.SignInActivity;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityCreateDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityUpdateDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityPageQueryDto;
public interface ISignInActivityService extends IBaseService<SignInActivity>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createSignInActivity(SignInActivityCreateDto signInActivityCreateVo,SimpleUserBo simpleUser);

	int updateSignInActivity(SignInActivityUpdateDto signInActivityUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}