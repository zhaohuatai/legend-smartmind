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
import com.smartmind.biz.bo.dto.learningreport.LearningReportCreateDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportUpdateDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportPageQueryDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ILearningReportService;
import com.smartmind.biz.dao.LearningReportMapper;
import com.smartmind.biz.bo.model.LearningReport;
@Service
public class LearningReportServiceImpl extends BaseServiceImpl<LearningReportMapper, LearningReport> implements ILearningReportService{
 	@Autowired
	private LearningReportMapper learningReportMapper;
	
	@Override
	public BaseMapper<LearningReport> getMapper() {
		return learningReportMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createLearningReport(LearningReportCreateDto learningReportCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(learningReportCreateDto);
		LearningReport learningReport = LearningReportConvert.INSTANCE.convert(learningReportCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "activityId","activityName"),Ref.newRef("SmdClassActivity", "id","activityName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	   
		return create(learningReport);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateLearningReport(LearningReportUpdateDto learningReportCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(learningReportCreateDto);
		LearningReport learningReport = LearningReportConvert.INSTANCE.convert(learningReportCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "activityId","activityName"),Ref.newRef("SmdClassActivity", "id","activityName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( learningReport, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	
		
		return  updateById(learningReport);
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