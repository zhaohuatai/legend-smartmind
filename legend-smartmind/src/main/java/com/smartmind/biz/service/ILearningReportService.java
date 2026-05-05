package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.LearningReport;
import com.smartmind.biz.bo.dto.learningreport.LearningReportCreateDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportUpdateDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportPageQueryDto;
public interface ILearningReportService extends IBaseService<LearningReport>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createLearningReport(LearningReportCreateDto learningReportCreateVo,SimpleUserBo simpleUser);

	int updateLearningReport(LearningReportUpdateDto learningReportUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}