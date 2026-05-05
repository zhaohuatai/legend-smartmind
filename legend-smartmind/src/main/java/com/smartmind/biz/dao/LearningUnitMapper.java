package com.smartmind.biz.dao;
import java.util.List;
import com.smartmind.biz.bo.model.LearningUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.legend.framework.core.consts.DaoConst;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitPageQueryDto;
@Mapper
public interface LearningUnitMapper extends BaseMapper<LearningUnit>{

	/**
	 * 查询课程下指定父单元的最大unitCode(树形编码)
	 * @param courseId 课程ID
	 * @param parentId 父单元ID
	 * @return 最大unitCode
	 */
	String selectMaxTreeCodeByCourseIdAndParentId(@Param("courseId") Long courseId, @Param("parentId") Long parentId);

	/**
	 * 查询指定父单元下的最大unitCode(树形编码)
	 * @param parentId 父单元ID
	 * @return 最大unitCode
	 */
	String selectMaxTreeCodeByParentId(@Param("parentId") Long parentId);

	/**
	 * 根据unitCode like删除子单元
	 * @param courseId 课程ID
	 * @param unitCodePattern unitCode匹配模式(如 100-01-%)
	 * @return 删除行数
	 */
	int deleteByUnitCodeLike(@Param("courseId") Long courseId, @Param("unitCodePattern") String unitCodePattern);

	List<LearningUnit> selectByIds(@Param("ids") java.util.List<Long> ids);

}
