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
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogCreateDto;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogUpdateDto;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogPageQueryDto;
import com.smartmind.biz.bo.dto.dataexportlog.DataExportLogConvert;
import org.legend.framework.bss.cache.DictCache;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;
import com.smartmind.biz.service.IDataExportLogService;
import com.smartmind.biz.dao.DataExportLogMapper;
import com.smartmind.biz.bo.model.DataExportLog;
@Service
public class DataExportLogServiceImpl extends BaseServiceImpl<DataExportLogMapper, DataExportLog> implements IDataExportLogService{
 	@Autowired
	private DataExportLogMapper dataExportLogMapper;
	
	@Override
	public BaseMapper<DataExportLog> getMapper() {
		return dataExportLogMapper;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int createDataExportLog(DataExportLogCreateDto dataExportLogCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(dataExportLogCreateDto);
		DataExportLog dataExportLog = DataExportLogConvert.INSTANCE.convert(dataExportLogCreateDto);
		
		dataExportLog.setCreateBy(simpleUser.getUserId());
	      
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	   
		return create(dataExportLog);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public	int updateDataExportLog(DataExportLogUpdateDto dataExportLogCreateDto,SimpleUserBo simpleUser){
		ZBeanUtil.validateBean(dataExportLogCreateDto);
		DataExportLog dataExportLog = DataExportLogConvert.INSTANCE.convert(dataExportLogCreateDto);
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "courseId","courseName"),Ref.newRef("SmdCourse", "id","courseName"));
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "classId","className"),Ref.newRef("SmdClazz", "id","className"));
		RedundantHandler.fillRedunFields(Self.newSelf( dataExportLog, "unitId","unitName"),Ref.newRef("SmdLearningUnit", "id","unitName"));
	
		
		return  updateById(dataExportLog);
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