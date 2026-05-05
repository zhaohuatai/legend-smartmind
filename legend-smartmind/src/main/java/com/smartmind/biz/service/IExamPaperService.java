package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ExamPaper;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperCreateDto;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperUpdateDto;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperPageQueryDto;
public interface IExamPaperService extends IBaseService<ExamPaper>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createExamPaper(ExamPaperCreateDto examPaperCreateVo,SimpleUserBo simpleUser);

	int updateExamPaper(ExamPaperUpdateDto examPaperUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}