package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.TeachingFile;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFileCreateDto;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFileUpdateDto;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFilePageQueryDto;
public interface ITeachingFileService extends IBaseService<TeachingFile>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createTeachingFile(TeachingFileCreateDto teachingFileCreateVo,SimpleUserBo simpleUser);

	int updateTeachingFile(TeachingFileUpdateDto teachingFileUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}