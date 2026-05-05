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
import com.smartmind.biz.bo.dto.teachingfile.TeachingFileCreateDto;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFileUpdateDto;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFilePageQueryDto;
import com.smartmind.biz.bo.dto.teachingfile.TeachingFileConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.ITeachingFileService;
import com.smartmind.biz.dao.TeachingFileMapper;
import com.smartmind.biz.bo.model.TeachingFile;
@Service
public class TeachingFileServiceImpl extends BaseServiceImpl<TeachingFileMapper, TeachingFile> implements ITeachingFileService{
 	@Autowired
	private TeachingFileMapper teachingFileMapper;
	
	@Override
	public BaseMapper<TeachingFile> getMapper() {
		return teachingFileMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createTeachingFile(TeachingFileCreateDto teachingFileCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teachingFileCreateDto);
		TeachingFile teachingFile = TeachingFileConvert.INSTANCE.convert(teachingFileCreateDto);
		
	      
		RedundantHandler.fillRedunFields(Self.newSelf( teachingFile, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( teachingFile, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	   
		return create(teachingFile);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateTeachingFile(TeachingFileUpdateDto teachingFileCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(teachingFileCreateDto);
		TeachingFile teachingFile = TeachingFileConvert.INSTANCE.convert(teachingFileCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( teachingFile, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( teachingFile, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	
		
		return  updateById(teachingFile);
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