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
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsUpdateDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsPageQueryDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IActivitySubmitStatisticsService;
import com.smartmind.biz.dao.ActivitySubmitStatisticsMapper;
import com.smartmind.biz.bo.model.ActivitySubmitStatistics;
@Service
public class ActivitySubmitStatisticsServiceImpl extends BaseServiceImpl<ActivitySubmitStatisticsMapper, ActivitySubmitStatistics> implements IActivitySubmitStatisticsService{
 	@Autowired
	private ActivitySubmitStatisticsMapper activitySubmitStatisticsMapper;
	
	@Override
	public BaseMapper<ActivitySubmitStatistics> getMapper() {
		return activitySubmitStatisticsMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createActivitySubmitStatistics(ActivitySubmitStatisticsCreateDto activitySubmitStatisticsCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(activitySubmitStatisticsCreateDto);
		ActivitySubmitStatistics activitySubmitStatistics = ActivitySubmitStatisticsConvert.INSTANCE.convert(activitySubmitStatisticsCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "activityId","activityName"),Ref.newRef("SmdClassActivity", "id","activityName"));
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
	   
		return create(activitySubmitStatistics);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateActivitySubmitStatistics(ActivitySubmitStatisticsUpdateDto activitySubmitStatisticsCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(activitySubmitStatisticsCreateDto);
		ActivitySubmitStatistics activitySubmitStatistics = ActivitySubmitStatisticsConvert.INSTANCE.convert(activitySubmitStatisticsCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "activityId","activityName"),Ref.newRef("SmdClassActivity", "id","activityName"));
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "studentId","studentName"),Ref.newRef("SmdStudent", "id","studentName"));
		RedundantHandler.fillRedunFields(Self.newSelf( activitySubmitStatistics, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
	
		
		return  updateById(activitySubmitStatistics);
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