package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionCreateDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionUpdateDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionPageQueryDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IExamPaperQuestionService;
import com.smartmind.biz.dao.ExamPaperQuestionMapper;
import com.smartmind.biz.bo.model.ExamPaperQuestion;
@Service
public class ExamPaperQuestionServiceImpl extends BaseServiceImpl<ExamPaperQuestionMapper, ExamPaperQuestion> implements IExamPaperQuestionService{
 	@Autowired
	private ExamPaperQuestionMapper examPaperQuestionMapper;
	
	@Override
	public BaseMapper<ExamPaperQuestion> getMapper() {
		return examPaperQuestionMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createExamPaperQuestion(ExamPaperQuestionCreateDto examPaperQuestionCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(examPaperQuestionCreateDto);
		ExamPaperQuestion examPaperQuestion = ExamPaperQuestionConvert.INSTANCE.convert(examPaperQuestionCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( examPaperQuestion, "questionId","questionContent"),Ref.newRef("SmdQuestionBank", "id","questionContent"));
	   
		return create(examPaperQuestion);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateExamPaperQuestion(ExamPaperQuestionUpdateDto examPaperQuestionCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(examPaperQuestionCreateDto);
		ExamPaperQuestion examPaperQuestion = ExamPaperQuestionConvert.INSTANCE.convert(examPaperQuestionCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( examPaperQuestion, "questionId","questionContent"),Ref.newRef("SmdQuestionBank", "id","questionContent"));
	
		
		return  updateById(examPaperQuestion);
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusListDto statusListDto) {
		ZBeanUtil.validateBean(statusListDto);
		setStatusByIds(statusListDto.getIds(),  statusListDto.getStatus()) ;
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setStatus(StatusDto status) {
		ZBeanUtil.validateBean(status);
		setStatusById(status.getId(),  status.getStatus()) ;
	}
	
}