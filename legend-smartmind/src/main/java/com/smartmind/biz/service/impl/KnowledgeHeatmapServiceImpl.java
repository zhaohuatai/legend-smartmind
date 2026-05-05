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
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapCreateDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapUpdateDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapPageQueryDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IKnowledgeHeatmapService;
import com.smartmind.biz.dao.KnowledgeHeatmapMapper;
import com.smartmind.biz.bo.model.KnowledgeHeatmap;
@Service
public class KnowledgeHeatmapServiceImpl extends BaseServiceImpl<KnowledgeHeatmapMapper, KnowledgeHeatmap> implements IKnowledgeHeatmapService{
 	@Autowired
	private KnowledgeHeatmapMapper knowledgeHeatmapMapper;
	
	@Override
	public BaseMapper<KnowledgeHeatmap> getMapper() {
		return knowledgeHeatmapMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createKnowledgeHeatmap(KnowledgeHeatmapCreateDto knowledgeHeatmapCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(knowledgeHeatmapCreateDto);
		KnowledgeHeatmap knowledgeHeatmap = KnowledgeHeatmapConvert.INSTANCE.convert(knowledgeHeatmapCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	   
		return create(knowledgeHeatmap);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateKnowledgeHeatmap(KnowledgeHeatmapUpdateDto knowledgeHeatmapCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(knowledgeHeatmapCreateDto);
		KnowledgeHeatmap knowledgeHeatmap = KnowledgeHeatmapConvert.INSTANCE.convert(knowledgeHeatmapCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( knowledgeHeatmap, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	
		
		return  updateById(knowledgeHeatmap);
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