package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ClassActivityResource;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceUpdateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourcePageQueryDto;
public interface IClassActivityResourceService extends IBaseService<ClassActivityResource>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createClassActivityResource(ClassActivityResourceCreateDto classActivityResourceCreateVo,SimpleUserBo simpleUser);

	int updateClassActivityResource(ClassActivityResourceUpdateDto classActivityResourceUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);

	int batchSaveResources(com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceBatchSaveDto batchSaveDto, SimpleUserBo simpleUser);

	List<ClassActivityResource> loadByActivityId(Long activityId);
    
    
}