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
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceUpdateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourcePageQueryDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassActivityResourceService;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceBatchSaveDto;
import com.smartmind.biz.dao.ClassActivityResourceMapper;
import com.smartmind.biz.bo.model.ClassActivityResource;
@Service
public class ClassActivityResourceServiceImpl extends BaseServiceImpl<ClassActivityResourceMapper, ClassActivityResource> implements IClassActivityResourceService{
 	@Autowired
	private ClassActivityResourceMapper classActivityResourceMapper;
	
	@Override
	public BaseMapper<ClassActivityResource> getMapper() {
		return classActivityResourceMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassActivityResource(ClassActivityResourceCreateDto classActivityResourceCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivityResourceCreateDto);
		ClassActivityResource classActivityResource = ClassActivityResourceConvert.INSTANCE.convert(classActivityResourceCreateDto);
		
	      
	   
		return create(classActivityResource);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassActivityResource(ClassActivityResourceUpdateDto classActivityResourceCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivityResourceCreateDto);
		ClassActivityResource classActivityResource = ClassActivityResourceConvert.INSTANCE.convert(classActivityResourceCreateDto);
	
		
		return  updateById(classActivityResource);
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
	public int batchSaveResources(ClassActivityResourceBatchSaveDto batchSaveDto, SimpleUserBo simpleUser) {
		ZAssert.notNull(batchSaveDto.getActivityId(), "课堂活动ID不能为空");
		ZAssert.notNull(batchSaveDto.getResourceType(), "资源类型不能为空");
		ZAssert.notNull(batchSaveDto.getResourceIds(), "资源ID列表不能为空");
		
		deleteByActivityIdAndType(batchSaveDto.getActivityId(), batchSaveDto.getResourceType());
		
		int count = 0;
		for (int i = 0; i < batchSaveDto.getResourceIds().size(); i++) {
			Long resourceId = batchSaveDto.getResourceIds().get(i);
			ClassActivityResource resource = new ClassActivityResource();
			resource.setActivityId(batchSaveDto.getActivityId());
			resource.setResourceType(batchSaveDto.getResourceType());
			resource.setResourceId(resourceId);
			resource.setScore(batchSaveDto.getScore());
			resource.setSortOrder(i + 1);
			resource.setCreateTime(new java.util.Date());
			classActivityResourceMapper.insertSelective(resource);
			count++;
		}
		return count;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=true)
	public List<ClassActivityResource> loadByActivityId(Long activityId) {
		if (activityId == null) {
			return Lists.newArrayList();
		}
		ChainWrapper<ClassActivityResource> qw = ChainWrapper.create(ClassActivityResource.class)
			.where(ClassActivityResource::getActivityId, "=", activityId)
			.orderBy(ClassActivityResource::getSortOrder, true);
		return loadByChainWrapper(qw);
	}
	
	private void deleteByActivityIdAndType(Long activityId, String resourceType) {
		ChainWrapper<ClassActivityResource> qw = ChainWrapper.create(ClassActivityResource.class)
			.where(ClassActivityResource::getActivityId, "=", activityId)
			.and(ClassActivityResource::getResourceType, "=", resourceType);
		classActivityResourceMapper.deleteByQuery(qw);
	}
	
}