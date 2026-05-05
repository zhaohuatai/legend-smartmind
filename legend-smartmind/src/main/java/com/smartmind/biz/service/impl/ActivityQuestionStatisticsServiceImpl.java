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
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsUpdateDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsPageQueryDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IActivityQuestionStatisticsService;
import com.smartmind.biz.dao.ActivityQuestionStatisticsMapper;
import com.smartmind.biz.bo.model.ActivityQuestionStatistics;
@Service
public class ActivityQuestionStatisticsServiceImpl extends BaseServiceImpl<ActivityQuestionStatisticsMapper, ActivityQuestionStatistics> implements IActivityQuestionStatisticsService{
 	@Autowired
	private ActivityQuestionStatisticsMapper activityQuestionStatisticsMapper;
	
	@Override
	public BaseMapper<ActivityQuestionStatistics> getMapper() {
		return activityQuestionStatisticsMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createActivityQuestionStatistics(ActivityQuestionStatisticsCreateDto activityQuestionStatisticsCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(activityQuestionStatisticsCreateDto);
		ActivityQuestionStatistics activityQuestionStatistics = ActivityQuestionStatisticsConvert.INSTANCE.convert(activityQuestionStatisticsCreateDto);
		
	      
	   
		return create(activityQuestionStatistics);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateActivityQuestionStatistics(ActivityQuestionStatisticsUpdateDto activityQuestionStatisticsCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(activityQuestionStatisticsCreateDto);
		ActivityQuestionStatistics activityQuestionStatistics = ActivityQuestionStatisticsConvert.INSTANCE.convert(activityQuestionStatisticsCreateDto);
	
		
		return  updateById(activityQuestionStatistics);
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