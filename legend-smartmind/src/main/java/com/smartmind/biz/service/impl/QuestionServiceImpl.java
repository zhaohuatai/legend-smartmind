package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ids.ZUidUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.question.QuestionCreateDto;
import com.smartmind.biz.bo.dto.question.QuestionUpdateDto;
import com.smartmind.biz.bo.dto.question.QuestionPageQueryDto;
import com.smartmind.biz.bo.dto.question.QuestionConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IQuestionService;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import com.smartmind.biz.dao.QuestionMapper;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class QuestionServiceImpl extends BaseServiceImpl<QuestionMapper, Question> implements IQuestionService{
 	@Autowired
	private QuestionMapper questionMapper;
	
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private ILearningUnitService learningUnitService;
	
	@Override
	public BaseMapper<Question> getMapper() {
		return questionMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	Long createQuestion(QuestionCreateDto questionCreateDto,SimpleUserBo simpleUser){
		questionCreateDto.setQuestionCode(ZUidUtil.base36Snowid());
		ZBeanUtil.validateBean(questionCreateDto);
		Question question = QuestionConvert.INSTANCE.convert(questionCreateDto);
		
		fillRedundantFields(question);
	   
		create(question);
		return question.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateQuestion(QuestionUpdateDto questionCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(questionCreateDto);
		Question question = QuestionConvert.INSTANCE.convert(questionCreateDto);
		
		fillRedundantFields(question);
		
		return  updateById(question);
	}
	
	private void fillRedundantFields(Question question) {
		if (question.getCourseId() != null) {
			Optional<Course> courseOpt = courseService.loadById(question.getCourseId());
			if (courseOpt.isPresent()) {
				question.setCourseName(courseOpt.get().getCourseName());
			}
		}
		if (question.getUnitId() != null) {
			Optional<LearningUnit> unitOpt = learningUnitService.loadById(question.getUnitId());
			if (unitOpt.isPresent()) {
				question.setUnitName(unitOpt.get().getUnitName());
				question.setUnitCode(unitOpt.get().getUnitCode());
			}
		}
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
	
	@Override
	public List<Question> loadByCourseAndUnit(Long courseId, String unitCode) {
		return questionMapper.selectByCourseAndUnit(courseId, unitCode);
	}
	
	@Override
	public List<Question> loadByCourseAndUnitCodes(Long courseId, List<String> unitCodes) {
		if (courseId == null || unitCodes == null || unitCodes.isEmpty()) {
			return Lists.newArrayList();
		}
		ChainWrapper<Question> qw = ChainWrapper.create(Question.class)
			.where(Question::getCourseId, "=", courseId)
			.and(Question::getStatus, "=", "1");
		qw.nestedAnd(wrapper -> {
			for (int i = 0; i < unitCodes.size(); i++) {
				String unitCode = unitCodes.get(i);
				if (i == 0) {
					wrapper.where(Question::getUnitCode, "LIKE", unitCode + "%");
				} else {
					wrapper.or(Question::getUnitCode, "LIKE", unitCode + "%");
				}
			}
		});
		qw.orderBy(Question::getCreateTime, false);
		return questionMapper.selectByQuery(qw);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int batchCreateQuestions(List<QuestionCreateDto> questions, SimpleUserBo simpleUser) {
		if (questions == null || questions.isEmpty()) {
			return 0;
		}
		int count = 0;
		for (QuestionCreateDto dto : questions) {
			try {
				createQuestion(dto, simpleUser);
				count++;
			} catch (Exception e) {
				log.warn("Failed to create question: {}", e.getMessage());
			}
		}
		return count;
	}
	
}