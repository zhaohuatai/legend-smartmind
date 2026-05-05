package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.biz.bo.dto.question.QuestionCreateDto;
import com.smartmind.biz.bo.dto.question.QuestionUpdateDto;
import com.smartmind.biz.bo.dto.question.QuestionPageQueryDto;
public interface IQuestionService extends IBaseService<Question>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	Long createQuestion(QuestionCreateDto questionCreateVo,SimpleUserBo simpleUser);

	int updateQuestion(QuestionUpdateDto questionUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    List<Question> loadByCourseAndUnit(Long courseId, String unitCode);
    
    List<Question> loadByCourseAndUnitCodes(Long courseId, List<String> unitCodes);
    
    int batchCreateQuestions(List<QuestionCreateDto> questions, SimpleUserBo simpleUser);
    
    
}