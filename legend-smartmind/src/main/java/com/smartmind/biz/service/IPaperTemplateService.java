package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.PaperTemplate;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateCreateDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateUpdateDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplatePageQueryDto;
public interface IPaperTemplateService extends IBaseService<PaperTemplate>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createPaperTemplate(PaperTemplateCreateDto paperTemplateCreateVo,SimpleUserBo simpleUser);

	int updatePaperTemplate(PaperTemplateUpdateDto paperTemplateUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}