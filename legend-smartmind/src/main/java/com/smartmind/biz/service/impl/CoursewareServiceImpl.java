package com.smartmind.biz.service.impl;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.util.ZStrUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareCreateDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareUpdateDto;
import com.smartmind.biz.bo.dto.courseware.CoursewarePageQueryDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ICoursewareService;
import com.smartmind.biz.dao.CoursewareMapper;
import com.smartmind.biz.dao.CourseMapper;
import com.smartmind.biz.dao.LearningUnitMapper;
import com.smartmind.biz.dao.TeachingFileMapper;
import com.smartmind.biz.dao.TeachingPlanMapper;
import com.smartmind.biz.dao.QuestionMapper;
import com.smartmind.biz.bo.model.Courseware;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.model.TeachingFile;
import com.smartmind.biz.bo.model.TeachingPlan;
import com.smartmind.biz.bo.model.Question;
import com.smartmind.ai.agent.PPTGenerationAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.model.ChatModel;
import reactor.core.publisher.Flux;
@Service
public class CoursewareServiceImpl extends BaseServiceImpl<CoursewareMapper, Courseware> implements ICoursewareService{
 	@Autowired
	private CoursewareMapper coursewareMapper;
	
	@Autowired
	private CourseMapper courseMapper;
	
	@Autowired
	private LearningUnitMapper learningUnitMapper;
	
	@Autowired
	private TeachingFileMapper teachingFileMapper;
	
	@Autowired
	private TeachingPlanMapper teachingPlanMapper;
	
	@Autowired
	private QuestionMapper questionMapper;
	
	@Autowired(required = false)
	private ChatModel chatModel;
	
	private PPTGenerationAgent pptGenerationAgent;
	
	@Override
	public BaseMapper<Courseware> getMapper() {
		return coursewareMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createCourseware(CoursewareCreateDto coursewareCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(coursewareCreateDto);
		Courseware courseware = CoursewareConvert.INSTANCE.convert(coursewareCreateDto);
	   
		return create(courseware);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateCourseware(CoursewareUpdateDto coursewareCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(coursewareCreateDto);
		Courseware courseware = CoursewareConvert.INSTANCE.convert(coursewareCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( courseware, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( courseware, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( courseware, "unitId","unitCode"),Ref.newRef("SmdLearningUnit", "id","unitCode"));
		RedundantHandler.fillRedunFields(Self.newSelf( courseware, "fileId","fileName"),Ref.newRef("SmdFile", "id","fileName"));
	
		
		return  updateById(courseware);
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
	public List<Courseware> loadByCourseIdAndUnitCode(Long courseId, String unitCode) {
		if (courseId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<Courseware> qw = ChainWrapper.create(Courseware.class)
			.where(Courseware::getCourseId, "=", courseId)
			.and(Courseware::getStatus, "=", "1");
		if (ZStrUtil.hasText(unitCode)) {
			qw.and(Courseware::getUnitCode, "LIKE", unitCode + "%");
		}
		qw.orderBy(Courseware::getSortOrder, true)
		  .orderBy(Courseware::getId, true);
		return coursewareMapper.selectByQuery(qw);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<Courseware> coursewareList, SimpleUserBo simpleUser) {
		if (coursewareList == null || coursewareList.isEmpty()) {
			return;
		}
		Courseware first = coursewareList.get(0);
		Long courseId = first.getCourseId();
		String unitCode = first.getUnitCode();

		ChainWrapper<Courseware> deleteQw = ChainWrapper.create(Courseware.class)
			.where(Courseware::getCourseId, "=", courseId)
			.and(Courseware::getUnitCode, "LIKE", unitCode + "%");
		coursewareMapper.deleteByQuery(deleteQw);

		java.util.Date now = new java.util.Date();
		for (Courseware courseware : coursewareList) {
			courseware.setId(null);
			courseware.setCreateTime(now);
			courseware.setUpdateTime(now);
			courseware.setCreateBy(simpleUser != null ? simpleUser.getAccount() : "admin");
			courseware.setUpdateBy(simpleUser != null ? simpleUser.getAccount() : "admin");
			courseware.setStatus("1");
			courseware.setCoursewareType((byte) 1);
		}
		coursewareMapper.insertBatch(coursewareList);
	}

//	@Override
//	public Flux<String> aiGeneratePPT(Long courseId, String unitCode, String userPrompt) {
//		if (pptGenerationAgent == null) {
//			pptGenerationAgent = new PPTGenerationAgent(chatModel);
//		}
//
//		Course course = courseMapper.selectById(courseId);
//		
//		ChainWrapper<LearningUnit> unitQw = ChainWrapper.create(LearningUnit.class)
//			.where(LearningUnit::getCourseId, "=", courseId)
//			.and(LearningUnit::getUnitCode, "=", unitCode)
//			.and(LearningUnit::getStatus, "=", "1");
//		List<LearningUnit> unitList = learningUnitMapper.selectByQuery(unitQw);
//		LearningUnit unit = unitList.isEmpty() ? null : unitList.get(0);
//		Long unitId = unit != null ? unit.getId() : null;
//
//		ChainWrapper<TeachingFile> fileQw = ChainWrapper.create(TeachingFile.class)
//			.where(TeachingFile::getCourseId, "=", courseId)
//			.and(TeachingFile::getStatus, "=", "1")
//			.orderBy(TeachingFile::getCreateTime, false);
//		if (unitId != null) {
//			fileQw.and(TeachingFile::getUnitId, "=", unitId);
//		}
//		List<TeachingFile> teachingFiles = teachingFileMapper.selectByQuery(fileQw);
//		if (teachingFiles.size() > 3) {
//			teachingFiles = teachingFiles.subList(0, 3);
//		}
//
//		ChainWrapper<TeachingPlan> planQw = ChainWrapper.create(TeachingPlan.class)
//			.where(TeachingPlan::getCourseId, "=", courseId)
//			.and(TeachingPlan::getStatus, "=", "1")
//			.orderBy(TeachingPlan::getCreateTime, false);
//		if (unitId != null) {
//			planQw.and(TeachingPlan::getUnitId, "=", unitId);
//		}
//		List<TeachingPlan> planList = teachingPlanMapper.selectByQuery(planQw);
//		TeachingPlan teachingPlan = planList.isEmpty() ? null : planList.get(0);
//
//		ChainWrapper<Question> questionQw = ChainWrapper.create(Question.class)
//			.where(Question::getCourseId, "=", courseId)
//			.and(Question::getStatus, "=", "1")
//			.orderBy(Question::getDifficultyLevel, true);
//		if (unitId != null) {
//			questionQw.and(Question::getUnitId, "=", unitId);
//		}
//		List<Question> questions = questionMapper.selectByQuery(questionQw);
//		if (questions.size() > 5) {
//			questions = questions.subList(0, 5);
//		}
//
//		try {
//			return pptGenerationAgent.generatePPT(course, unit, teachingFiles, teachingPlan, questions, userPrompt);
//		} catch (GraphRunnerException e) {
//			return Flux.just("生成失败: " + e.getMessage(), "[DONE]");
//		}
//	}

}