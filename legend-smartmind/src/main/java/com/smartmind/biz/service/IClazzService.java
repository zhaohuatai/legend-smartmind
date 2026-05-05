package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.Clazz;
import com.smartmind.biz.bo.dto.clazz.ClazzCreateDto;
import com.smartmind.biz.bo.dto.clazz.ClazzUpdateDto;
import com.smartmind.biz.bo.dto.clazz.ClazzPageQueryDto;
public interface IClazzService extends IBaseService<Clazz>{
   	int createClazz(ClazzCreateDto clazzCreateVo,SimpleUserBo simpleUser);

	int updateClazz(ClazzUpdateDto clazzUpdateVo,SimpleUserBo simpleUser);

	List<SelectVo> loadSelectVo();

    void setStatus(StatusListDto statusListDto);

    void setStatus(StatusDto status);

    /**
     * 更新班级学生数量
     * @param classId 班级ID
     */
    void updateClassStudentCount(Long classId);
}