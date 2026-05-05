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
import com.smartmind.biz.bo.dto.exampaper.ExamPaperCreateDto;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperUpdateDto;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperPageQueryDto;
import com.smartmind.biz.bo.dto.exampaper.ExamPaperConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IExamPaperService;
import com.smartmind.biz.dao.ExamPaperMapper;
import com.smartmind.biz.bo.model.ExamPaper;
@Service
public class ExamPaperServiceImpl extends BaseServiceImpl<ExamPaperMapper, ExamPaper> implements IExamPaperService{
 	@Autowired
	private ExamPaperMapper examPaperMapper;
	
	@Override
	public BaseMapper<ExamPaper> getMapper() {
		return examPaperMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createExamPaper(ExamPaperCreateDto examPaperCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(examPaperCreateDto);
		ExamPaper examPaper = ExamPaperConvert.INSTANCE.convert(examPaperCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( examPaper, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( examPaper, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	   
		return create(examPaper);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateExamPaper(ExamPaperUpdateDto examPaperCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(examPaperCreateDto);
		ExamPaper examPaper = ExamPaperConvert.INSTANCE.convert(examPaperCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( examPaper, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( examPaper, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	
		
		return  updateById(examPaper);
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