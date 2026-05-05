package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.KnowledgeHeatmap;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapCreateDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapUpdateDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapPageQueryDto;
public interface IKnowledgeHeatmapService extends IBaseService<KnowledgeHeatmap>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createKnowledgeHeatmap(KnowledgeHeatmapCreateDto knowledgeHeatmapCreateVo,SimpleUserBo simpleUser);

	int updateKnowledgeHeatmap(KnowledgeHeatmapUpdateDto knowledgeHeatmapUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}