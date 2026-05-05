package com.smartmind.biz.service.impl;

import java.util.List;

import com.smartmind.biz.bo.dto.learningunit.LearningUnitNode;
import com.smartmind.biz.util.LearningUnitUtil;
import org.legend.framework.core.auth.SimpleUserBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.core.util.ids.ZUidUtil;
import org.legend.framework.core.util.AppCtxUtil;
import org.legend.framework.core.util.ZAlert;
import org.legend.framework.core.util.ZAssert;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.base.redundant.RedundantHandler;
import com.google.common.collect.Lists;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitCreateDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitUpdateDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitConvert;
import org.legend.framework.base.service.BaseServiceImpl;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.base.redundant.Ref;
import org.legend.framework.base.redundant.Self;

import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import com.smartmind.biz.dao.LearningUnitMapper;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.model.Course;

@Service
public class LearningUnitServiceImpl extends BaseServiceImpl<LearningUnitMapper, LearningUnit> implements ILearningUnitService {

    @Autowired
    private LearningUnitMapper learningUnitMapper;

    @Override
    public BaseMapper<LearningUnit> getMapper() {
        return learningUnitMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createLearningUnit(LearningUnitCreateDto learningUnitCreateDto, SimpleUserBo simpleUser) {

        LearningUnit learningUnit = LearningUnitConvert.INSTANCE.convert(learningUnitCreateDto);

        // 获取课程信息
        Course course = AppCtxUtil.cfg.getBean(ICourseService.class).loadById(learningUnit.getCourseId())
                .orElseThrow(() -> new RuntimeException("未查询到课程数据"));

        learningUnit.setCourseId(course.getId());
        learningUnit.setCourseName(course.getCourseName());

        // 生成树形编码作为单元编码: courseId-层级编码(如100-01-01)
        String unitCode = generateTreeCode(learningUnit.getCourseId(), learningUnit.getParentId());
        learningUnit.setUnitCode(unitCode);

        ZBeanUtil.validateBean(learningUnit);
        return create(learningUnit);
    }

    /**
     * 生成树形编码
     * 格式: courseId-层级编码(每层2位数字)
     * 示例: 100-01(第一章), 100-01-01(第一章第一节)
     *
     * @param courseId 课程ID
     * @param parentId 父单元ID(0表示根)
     * @return 树形编码
     */
    private String generateTreeCode(Long courseId, Long parentId) {
        // 根节点
        if (parentId == null || parentId == 0) {
            // 查询当前课程下最大的根节点编码
            String maxUnitCode = learningUnitMapper.selectMaxTreeCodeByCourseIdAndParentId(courseId, 0L);
            int nextSeq = 1;
            if (maxUnitCode != null) {
                // 提取最后的序列号
                String[] parts = maxUnitCode.split("-");
                if (parts.length >= 2) {
                    try {
                        nextSeq = Integer.parseInt(parts[1]) + 1;
                    } catch (NumberFormatException e) {
                        nextSeq = 1;
                    }
                }
            }
            return courseId + "-" + String.format("%02d", nextSeq);
        } else {
            // 子节点，查询父节点的unitCode(树形编码)
            LearningUnit parentUnit = learningUnitMapper.selectById(parentId);
            if (parentUnit == null || parentUnit.getUnitCode() == null) {
                throw new RuntimeException("父单元不存在或unitCode为空");
            }
            String parentUnitCode = parentUnit.getUnitCode();
            // 查询同级最大的编码
            String maxUnitCode = learningUnitMapper.selectMaxTreeCodeByParentId(parentId);
            int nextSeq = 1;
            if (maxUnitCode != null && maxUnitCode.startsWith(parentUnitCode)) {
                // 提取当前层级最后的序列号
                String suffix = maxUnitCode.substring(parentUnitCode.length() + 1);
                String[] parts = suffix.split("-");
                if (parts.length > 0) {
                    try {
                        nextSeq = Integer.parseInt(parts[0]) + 1;
                    } catch (NumberFormatException e) {
                        nextSeq = 1;
                    }
                }
            }
            return parentUnitCode + "-" + String.format("%02d", nextSeq);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateLearningUnit(LearningUnitUpdateDto learningUnitCreateDto, SimpleUserBo simpleUser) {
        ZBeanUtil.validateBean(learningUnitCreateDto);
        LearningUnit learningUnit = LearningUnitConvert.INSTANCE.convert(learningUnitCreateDto);
        RedundantHandler.fillRedunFields(Self.newSelf(learningUnit, "courseId", "courseName"), Ref.newRef("SmdCourse", "id", "courseName"));


        return updateById(learningUnit);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setStatus(StatusListDto statusListDto) {
        ZBeanUtil.validateBean(statusListDto);
        setStatusByIds(statusListDto.getIds(), statusListDto.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setStatus(StatusDto status) {
        ZBeanUtil.validateBean(status);
        setStatusById(status.getId(), status.getStatus());
    }

    @Override
    public List<LearningUnit> queryByCourseId(Long courseId) {
        if (courseId == null) {
            return Lists.newArrayList();
        }
        ChainWrapper<LearningUnit> qw = ChainWrapper.create(LearningUnit.class)
                .where("course_id", "=", courseId)
                .orderBy("sort_order", true);
        return learningUnitMapper.selectByQuery(qw);
    }

    /**
     * 判断课程是否已有学习单元
     */
    @Override
    public boolean hasLearningUnits(Long courseId) {
        if (courseId == null) {
            return false;
        }
        ChainWrapper<LearningUnit> qw = ChainWrapper.create(LearningUnit.class)
                .where("course_id", "=", courseId);
        return learningUnitMapper.countByQuery(qw) > 0;
    }
    

    /**
     * 获取课程知识结构Markdown（用于生成学习单元）
     *
     * @param courseId 课程ID
     * @return 知识结构的Markdown文本
     * @throws IllegalStateException 如果知识结构未完成或课程已有学习单元
     */
    @Override
    public String queryKnowledgeFramework(Long courseId) {
        ZAssert.notNull(courseId, "课程ID不能为空");

        // 1. 查询课程
        Course course = AppCtxUtil.cfg.getBean(ICourseService.class).loadById(courseId)
                .orElseThrow(() -> ZAlert.newSLE("课程不存在"));

        String knowledgeFramework = course.getKnowledgeFramework();

        // 2. 检查知识结构是否完整
        if (knowledgeFramework == null || knowledgeFramework.trim().isEmpty()) {
        	ZAlert.throwSLE("该课程尚未生成知识结构，请先使用AI生成知识框架");
        }

        // 检查内容是否太少（至少要有几个内容行）
        int contentCount = LearningUnitUtil.countContentLines(knowledgeFramework);
        if (contentCount < 3) {
        	ZAlert.throwSLE("知识结构内容太少，请完善后再生成学习单元");
        }

        // 3. 检查是否已有学习单元
        if (hasLearningUnits(courseId)) {
        	ZAlert.throwSLE("该课程已有学习单元，如需重新生成请先删除现有学习单元或手动维护");
        }

        return knowledgeFramework;
    }

    /**
     * 从修改后的Markdown解析并保存学习单元
     *
     * @param courseId 课程ID
     * @param markdown 用户修改后的Markdown内容
     * @param level 生成章节层级: 2-两层, 3-三层
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createLearningUnitsFromMarkdown(Long courseId, String markdown, Integer level) {
        ZAssert.notNull(courseId, "课程ID不能为空");
        ZAssert.hasText(markdown, "知识框架内容不能为空");
        if (level == null) {
            level = 3; // 默认三层
        }

        // 查询课程
        Course course = AppCtxUtil.cfg.getBean(ICourseService.class).loadById(courseId)
                .orElseThrow(() ->  ZAlert.newSLE("课程不存在"));

        // 解析Markdown为节点树
        List<LearningUnitNode> nodes = LearningUnitUtil.parseMarkdown(markdown);

        // 根据层级过滤节点（如果level=2，则只保留两层，移除第三层）
        if (level != null && level < 3) {
            filterNodesByLevel(nodes, level);
        }

        // 保存节点树
        saveNodes(courseId, course.getCourseName(), nodes, 0L);
    }

    /**
     * 根据层级递归过滤节点
     * @param nodes 节点列表
     * @param maxLevel 最大层级
     */
    private void filterNodesByLevel(List<LearningUnitNode> nodes, int maxLevel) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        // 移除超过最大层级的节点
        nodes.removeIf(node -> node.getLevel() > maxLevel);
        // 对于保留的节点，递归过滤其子节点
        for (LearningUnitNode node : nodes) {
            if (node.hasChildren()) {
                filterNodesByLevel(node.getChildren(), maxLevel);
            }
        }
    }

    /**
     * 递归保存节点到数据库（批量生成时，基于索引生成treeCode）
     */
    private void saveNodes(Long courseId, String courseName, List<LearningUnitNode> nodes, Long parentId) {
        saveNodesWithTreeCode(courseId, courseName, nodes, parentId, null);
    }

    /**
     * 递归保存节点到数据库，携带父级unitCode(树形编码)
     * @param parentUnitCode 父级unitCode(树形编码)，根节点时为null
     */
    private void saveNodesWithTreeCode(Long courseId, String courseName, List<LearningUnitNode> nodes, Long parentId, String parentUnitCode) {
        int index = 1;
        for (LearningUnitNode node : nodes) {
            LearningUnit unit = new LearningUnit();
            unit.setCourseId(courseId);
            unit.setCourseName(courseName);
            unit.setParentId(parentId);
            unit.setUnitLevel(node.getUnitLevel());
            unit.setUnitName(node.getTitle());
            unit.setSortOrder(index);
            unit.setEstimatedHours(node.estimateHours());
            unit.setUnitDesc(node.getContentDesc());
            unit.setStatus("1");
            // 先设置ID，确保子节点能获取到parentId
            unit.setId(ZUidUtil.snowid());
            // 生成树形编码作为unitCode：根节点用courseId开头，子节点用父unitCode拼接
            String unitCode;
            if (parentUnitCode == null) {
                unitCode = courseId + "-" + String.format("%02d", index);
            } else {
                unitCode = parentUnitCode + "-" + String.format("%02d", index);
            }
            unit.setUnitCode(unitCode);
            learningUnitMapper.insertSelective(unit);
            // 递归保存子节点
            if (node.hasChildren()) {
                saveNodesWithTreeCode(courseId, courseName, node.getChildren(), unit.getId(), unitCode);
            }
            index++;
        }
    }

    /**
     * 删除学习单元（使用unitCode like删除所有子单元）
     *
     * @param id 学习单元ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLearningUnit(Long id) {
        ZAssert.notNull(id, "学习单元ID不能为空");

        // 查询该单元是否存在
        LearningUnit unit = learningUnitMapper.selectByPrimaryKey(id);
        if (unit == null) {
            ZAlert.throwSLE("学习单元不存在");
        }

        // 使用unitCode like删除所有子单元
        String unitCode = unit.getUnitCode();
        if (unitCode != null) {
            learningUnitMapper.deleteByUnitCodeLike(unit.getCourseId(), unitCode + "-%");
        }

        // 删除当前单元
        learningUnitMapper.deleteByPrimaryKey(id);
    }

}
