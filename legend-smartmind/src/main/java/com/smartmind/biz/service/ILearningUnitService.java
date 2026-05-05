package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitCreateDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitUpdateDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitPageQueryDto;
public interface ILearningUnitService extends IBaseService<LearningUnit>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createLearningUnit(LearningUnitCreateDto learningUnitCreateVo,SimpleUserBo simpleUser);

	int updateLearningUnit(LearningUnitUpdateDto learningUnitUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);
    
    /**
     * 根据课程ID查询章节列表
     */
    List<LearningUnit> queryByCourseId(Long courseId);

	/**
	 * 判断课程是否已有学习单元
	 */
	boolean hasLearningUnits(Long courseId);

	/**
	 * 获取课程知识结构Markdown（用于生成学习单元）
	 *
	 * @param courseId 课程ID
	 * @return 知识结构的Markdown文本
	 * @throws IllegalStateException 如果知识结构未完成或课程已有学习单元
	 */
	String queryKnowledgeFramework(Long courseId);

	/**
	 * 从修改后的Markdown解析并保存学习单元
	 *
	 * @param courseId 课程ID
	 * @param markdown 用户修改后的Markdown内容
	 * @param level 生成章节层级: 2-两层, 3-三层
	 */
	void createLearningUnitsFromMarkdown(Long courseId, String markdown, Integer level);

	/**
	 * 删除学习单元（递归删除所有子单元）
	 *
	 * @param id 学习单元ID
	 */
	void deleteLearningUnit(Long id);

}