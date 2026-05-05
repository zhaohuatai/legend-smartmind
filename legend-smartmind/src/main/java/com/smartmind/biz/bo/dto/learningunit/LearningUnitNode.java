package com.smartmind.biz.bo.dto.learningunit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 学习单元节点
 * 用于Markdown解析后的树形结构，支持保存到数据库
 */
@Data
public class LearningUnitNode {

    /** 层级: 1-大单元, 2-子单元, 3-课时 */
    private int level;

    /** 标题 */
    private String title;

    /** 内容列表（课时下的知识点等） */
    private List<String> content = new ArrayList<>();

    /** 子节点 */
    private List<LearningUnitNode> children = new ArrayList<>();

    public LearningUnitNode() {
    }

    public LearningUnitNode(int level, String title) {
        this.level = level;
        this.title = title;
    }

    /**
     * 获取单元层级字符串
     */
    public String getUnitLevel() {
        return String.valueOf(level);
    }

    /**
     * 获取层级文本
     */
    public String getLevelText() {
        switch (level) {
            case 1:
                return "大单元";
            case 2:
                return "子单元";
            case 3:
                return "课时";
            default:
                return "未知";
        }
    }

    /**
     * 预估课时数
     */
    public Integer estimateHours() {
        switch (level) {
            case 1:
                return 8;
            case 2:
                return 4;
            case 3:
                return 1;
            default:
                return 1;
        }
    }

    /**
     * 获取内容描述（用换行拼接）
     */
    public String getContentDesc() {
        if (content.isEmpty()) {
            return null;
        }
        return String.join("\n", content);
    }

    /**
     * 添加子节点
     */
    public void addChild(LearningUnitNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    /**
     * 添加内容
     */
    public void addContent(String item) {
        if (content == null) {
            content = new ArrayList<>();
        }
        content.add(item);
    }

    /**
     * 是否有子节点
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    /**
     * 是否有内容
     */
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }
}
