package com.smartmind.biz.service.impl;
import java.math.BigDecimal;
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
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailUpdateDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailPageQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IClassActivitySubmitDetailService;
import com.smartmind.biz.dao.ClassActivitySubmitDetailMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;
import com.smartmind.biz.dao.ClassActivitySubmitMapper;
import com.smartmind.biz.bo.model.ClassActivitySubmit;
import com.smartmind.biz.dao.ClassActivityMapper;
import com.smartmind.biz.bo.model.ClassActivity;
@Service
public class ClassActivitySubmitDetailServiceImpl extends BaseServiceImpl<ClassActivitySubmitDetailMapper, ClassActivitySubmitDetail> implements IClassActivitySubmitDetailService{
 	@Autowired
	private ClassActivitySubmitDetailMapper classActivitySubmitDetailMapper;
	
	@Autowired
	private ClassActivitySubmitMapper classActivitySubmitMapper;
	
	@Autowired
	private ClassActivityMapper classActivityMapper;
	
	@Override
	public BaseMapper<ClassActivitySubmitDetail> getMapper() {
		return classActivitySubmitDetailMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createClassActivitySubmitDetail(ClassActivitySubmitDetailCreateDto classActivitySubmitDetailCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivitySubmitDetailCreateDto);
		ClassActivitySubmitDetail classActivitySubmitDetail = ClassActivitySubmitDetailConvert.INSTANCE.convert(classActivitySubmitDetailCreateDto);
		
	      
	   
		return create(classActivitySubmitDetail);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateClassActivitySubmitDetail(ClassActivitySubmitDetailUpdateDto classActivitySubmitDetailCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(classActivitySubmitDetailCreateDto);
		ClassActivitySubmitDetail classActivitySubmitDetail = ClassActivitySubmitDetailConvert.INSTANCE.convert(classActivitySubmitDetailCreateDto);
	
		
		int result = updateById(classActivitySubmitDetail);
		if (classActivitySubmitDetail.getSubmitId() != null) {
			recalculateSubmit(classActivitySubmitDetail.getSubmitId());
		}
		return result;
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
	
	private void recalculateSubmit(Long submitId) {
		if (submitId == null) {
			return;
		}
		ClassActivitySubmit submit = classActivitySubmitMapper.selectById(submitId);
		if (submit == null) {
			return;
		}
		
		ChainWrapper<ClassActivitySubmitDetail> detailQw = ChainWrapper.create(ClassActivitySubmitDetail.class)
			.where(ClassActivitySubmitDetail::getSubmitId, "=", submitId);
		List<ClassActivitySubmitDetail> details = classActivitySubmitDetailMapper.selectByQuery(detailQw);
		
		BigDecimal totalScore = BigDecimal.ZERO;
		int submitCount = 0;
		
		if (details != null && !details.isEmpty()) {
			for (ClassActivitySubmitDetail detail : details) {
				if (detail.getStatus() != null && !"0".equals(detail.getStatus())) {
					submitCount++;
					if (detail.getScore() != null) {
						totalScore = totalScore.add(detail.getScore());
					}
				}
			}
		}
		
		submit.setScore(totalScore);
		submit.setSubmitCount(submitCount);
		submit.setUpdateTime(new java.util.Date());
		classActivitySubmitMapper.updateById(submit);
		
		if (submit.getActivityId() != null) {
			ChainWrapper<ClassActivitySubmit> submitListQw = ChainWrapper.create(ClassActivitySubmit.class)
				.where(ClassActivitySubmit::getActivityId, "=", submit.getActivityId());
			List<ClassActivitySubmit> allSubmits = classActivitySubmitMapper.selectByQuery(submitListQw);
			
			int submittedCount = 0;
			BigDecimal totalScoreSum = BigDecimal.ZERO;
			int scoredCount = 0;
			
			for (ClassActivitySubmit s : allSubmits) {
				if (s.getStatus() != null && !"0".equals(s.getStatus())) {
					submittedCount++;
				}
				if (s.getScore() != null) {
					totalScoreSum = totalScoreSum.add(s.getScore());
					scoredCount++;
				}
			}
			
			BigDecimal avgScore = scoredCount > 0 ? totalScoreSum.divide(BigDecimal.valueOf(scoredCount), 1, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
			
			ClassActivity activity = classActivityMapper.selectById(submit.getActivityId());
			if (activity != null) {
				activity.setSubmitStuCount(submittedCount);
				activity.setAvgScore(avgScore);
				activity.setUpdateTime(new java.util.Date());
				classActivityMapper.updateById(activity);
			}
		}
	}

	@Override
	public ClassActivitySubmitDetail selectBySubmitAndResource(Long submitId, Long resourceId,String resourceType) {
		if (submitId == null && resourceId == null) {
			return null;
		}
		ChainWrapper<ClassActivitySubmitDetail> qw = ChainWrapper.create(ClassActivitySubmitDetail.class);
		if (submitId != null) {
			qw.where(ClassActivitySubmitDetail::getSubmitId, "=", submitId);
		}
		if (resourceId != null) {
			qw.and(ClassActivitySubmitDetail::getResourceId, "=", resourceId);
		}
		if (resourceType != null) {
			qw.and(ClassActivitySubmitDetail::getResourceType, "=", resourceType);
		}
		
		List<ClassActivitySubmitDetail> res = classActivitySubmitDetailMapper.selectByQuery(qw);
		
		return res==null||res.isEmpty()?null:res.get(0);
	}
	@Override
	public List<ClassActivitySubmitDetail> selectBySubmitId(Long submitId) {
		if (submitId == null ) {
			return new java.util.ArrayList<>();
		}
		ChainWrapper<ClassActivitySubmitDetail> qw = ChainWrapper.create(ClassActivitySubmitDetail.class);
		if (submitId != null) {
			qw.where(ClassActivitySubmitDetail::getSubmitId, "=", submitId);
		}
		return classActivitySubmitDetailMapper.selectByQuery(qw);
	}
}