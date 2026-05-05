package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.ExamPaperQuestion;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionCreateDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionUpdateDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionPageQueryDto;
public interface IExamPaperQuestionService extends IBaseService<ExamPaperQuestion>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createExamPaperQuestion(ExamPaperQuestionCreateDto examPaperQuestionCreateVo,SimpleUserBo simpleUser);

	int updateExamPaperQuestion(ExamPaperQuestionUpdateDto examPaperQuestionUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}