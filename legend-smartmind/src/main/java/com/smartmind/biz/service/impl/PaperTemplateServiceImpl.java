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
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateCreateDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateUpdateDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplatePageQueryDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IPaperTemplateService;
import com.smartmind.biz.dao.PaperTemplateMapper;
import com.smartmind.biz.bo.model.PaperTemplate;
@Service
public class PaperTemplateServiceImpl extends BaseServiceImpl<PaperTemplateMapper, PaperTemplate> implements IPaperTemplateService{
 	@Autowired
	private PaperTemplateMapper paperTemplateMapper;
	
	@Override
	public BaseMapper<PaperTemplate> getMapper() {
		return paperTemplateMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createPaperTemplate(PaperTemplateCreateDto paperTemplateCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(paperTemplateCreateDto);
		PaperTemplate paperTemplate = PaperTemplateConvert.INSTANCE.convert(paperTemplateCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( paperTemplate, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
	   
		return create(paperTemplate);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updatePaperTemplate(PaperTemplateUpdateDto paperTemplateCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(paperTemplateCreateDto);
		PaperTemplate paperTemplate = PaperTemplateConvert.INSTANCE.convert(paperTemplateCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( paperTemplate, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
	
		
		return  updateById(paperTemplate);
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