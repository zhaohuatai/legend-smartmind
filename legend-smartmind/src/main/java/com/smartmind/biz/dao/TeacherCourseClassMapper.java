package com.smartmind.biz.dao;
import java.util.List;
import com.smartmind.biz.bo.model.TeacherCourseClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.legend.framework.core.consts.DaoConst;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassPageQueryDto;
@Mapper
public interface TeacherCourseClassMapper extends BaseMapper<TeacherCourseClass>{
	
}
