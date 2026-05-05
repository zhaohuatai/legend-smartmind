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
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementCreateDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementUpdateDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementPageQueryDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IStandardAchievementService;
import com.smartmind.biz.dao.StandardAchievementMapper;
import com.smartmind.biz.bo.model.StandardAchievement;
@Service
public class StandardAchievementServiceImpl extends BaseServiceImpl<StandardAchievementMapper, StandardAchievement> implements IStandardAchievementService{
 	@Autowired
	private StandardAchievementMapper standardAchievementMapper;
	
	@Override
	public BaseMapper<StandardAchievement> getMapper() {
		return standardAchievementMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createStandardAchievement(StandardAchievementCreateDto standardAchievementCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(standardAchievementCreateDto);
		StandardAchievement standardAchievement = StandardAchievementConvert.INSTANCE.convert(standardAchievementCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	   
		return create(standardAchievement);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateStandardAchievement(StandardAchievementUpdateDto standardAchievementCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(standardAchievementCreateDto);
		StandardAchievement standardAchievement = StandardAchievementConvert.INSTANCE.convert(standardAchievementCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( standardAchievement, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
	
		
		return  updateById(standardAchievement);
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