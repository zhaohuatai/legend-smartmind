package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.DataExportLog;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogCreateDto;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogUpdateDto;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogPageQueryDto;
public interface IDataExportLogService extends IBaseService<DataExportLog>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createDataExportLog(DataExportLogCreateDto dataExportLogCreateVo,SimpleUserBo simpleUser);

	int updateDataExportLog(DataExportLogUpdateDto dataExportLogUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}