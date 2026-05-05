package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailUpdateDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailPageQueryDto;
public interface IClassActivitySubmitDetailService extends IBaseService<ClassActivitySubmitDetail>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassActivitySubmitDetail(ClassActivitySubmitDetailCreateDto classActivitySubmitDetailCreateVo,SimpleUserBo simpleUser);

	int updateClassActivitySubmitDetail(ClassActivitySubmitDetailUpdateDto classActivitySubmitDetailUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    ClassActivitySubmitDetail selectBySubmitAndResource(Long submitId, Long resourceId,String resource_type);
    
    List<ClassActivitySubmitDetail> selectBySubmitId(Long submitId);
}