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
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicCreateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicUpdateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicPageQueryDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;

import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.IDiscussionTopicService;
import com.smartmind.biz.service.ILearningUnitService;
import com.smartmind.biz.dao.DiscussionTopicMapper;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.DiscussionTopic;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.bo.model.LearningUnit;
@Service
public class DiscussionTopicServiceImpl extends BaseServiceImpl<DiscussionTopicMapper, DiscussionTopic> implements IDiscussionTopicService{
 	@Autowired
	private DiscussionTopicMapper discussionTopicMapper;
	@Autowired
	private ICourseService courseService;
	
	@Autowired
	private ILearningUnitService learningUnitService;
	@Override
	public BaseMapper<DiscussionTopic> getMapper() {
		return discussionTopicMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createDiscussionTopic(DiscussionTopicCreateDto discussionTopicCreateDto,SimpleUserBo simpleUser){
		discussionTopicCreateDto.setTopicCode(ZUidUtil.base36Snowid());
		ZBeanUtil.validateBean(discussionTopicCreateDto);
		DiscussionTopic discussionTopic = DiscussionTopicConvert.INSTANCE.convert(discussionTopicCreateDto);
		
		fillRedundantFields( discussionTopic);
	   
		return create(discussionTopic);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateDiscussionTopic(DiscussionTopicUpdateDto discussionTopicCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(discussionTopicCreateDto);
		DiscussionTopic discussionTopic = DiscussionTopicConvert.INSTANCE.convert(discussionTopicCreateDto);
	
		fillRedundantFields( discussionTopic);
		return  updateById(discussionTopic);
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public List<DiscussionTopic> loadByCourseIdAndUnitCode(Long courseId, String unitCode) {
		if (courseId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<DiscussionTopic> qw = ChainWrapper.create(DiscussionTopic.class)
			.where(DiscussionTopic::getCourseId, "=", courseId);
		if (ZStrUtil.hasText(unitCode)) {
			qw.and(DiscussionTopic::getUnitCode, "LIKE", unitCode + "%");
		}
		return loadByChainWrapper(qw);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public List<DiscussionTopic> loadByCourseIdAndUnitCodes(Long courseId, List<String> unitCodes) {
		if (courseId == null || unitCodes == null || unitCodes.isEmpty()) {
			return Lists.newArrayList();
		}
		ChainWrapper<DiscussionTopic> qw = ChainWrapper.create(DiscussionTopic.class)
			.where(DiscussionTopic::getCourseId, "=", courseId)
			.and(DiscussionTopic::getStatus, "=", "1");
		qw.nestedAnd(wrapper -> {
			for (int i = 0; i < unitCodes.size(); i++) {
				String unitCode = unitCodes.get(i);
				if (i == 0) {
					wrapper.where(DiscussionTopic::getUnitCode, "LIKE", unitCode + "%");
				} else {
					wrapper.or(DiscussionTopic::getUnitCode, "LIKE", unitCode + "%");
				}
			}
		});
		qw.orderBy(DiscussionTopic::getCreateTime, false);
		return loadByChainWrapper(qw);
	}
	
	private void fillRedundantFields(DiscussionTopic discussionTopic) {
		if (discussionTopic.getCourseId() != null) {
			Optional<Course> courseOpt = courseService.loadById(discussionTopic.getCourseId());
			if (courseOpt.isPresent()) {
				discussionTopic.setCourseName(courseOpt.get().getCourseName());
			}
		}
		if (discussionTopic.getUnitId() != null) {
			Optional<LearningUnit> unitOpt = learningUnitService.loadById(discussionTopic.getUnitId());
			if (unitOpt.isPresent()) {
				discussionTopic.setUnitName(unitOpt.get().getUnitName());
				discussionTopic.setUnitCode(unitOpt.get().getUnitCode());
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
	@Transactional(rollbackFor=Exception.class)
	public int batchCreateTopics(List<DiscussionTopicCreateDto> topics, SimpleUserBo simpleUser) {
		if (topics == null || topics.isEmpty()) {
			return 0;
		}
		int count = 0;
		for (DiscussionTopicCreateDto dto : topics) {
			DiscussionTopic topic = DiscussionTopicConvert.INSTANCE.convert(dto);
			fillRedundantFields(topic);
			count += create(topic);
		}
		return count;
	}
	
}