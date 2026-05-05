package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.LearningSnapshot;
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotCreateDto;
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotUpdateDto;
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotPageQueryDto;
public interface ILearningSnapshotService extends IBaseService<LearningSnapshot>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createLearningSnapshot(LearningSnapshotCreateDto learningSnapshotCreateVo,SimpleUserBo simpleUser);

	int updateLearningSnapshot(LearningSnapshotUpdateDto learningSnapshotUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    
}