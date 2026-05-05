package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideContentUpdateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideCreateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideUpdateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuidePageQueryDto;
public interface IExperimentGuideService extends IBaseService<ExperimentGuide>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
	Long createExperimentGuide(ExperimentGuideCreateDto experimentGuideCreateVo,SimpleUserBo simpleUser);

	int updateExperimentGuide(ExperimentGuideUpdateDto experimentGuideUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
	Optional<ExperimentGuide> loadByGuideCode(java.lang.String guideCode);

	List<ExperimentGuide> loadByCourseIdAndUnitCode(Long courseId, String unitCode);

	List<ExperimentGuide> loadByCourseIdAndUnitCodes(Long courseId, List<String> unitCodes);

	void updateMainContent(ExperimentGuideContentUpdateDto dto);
    
}