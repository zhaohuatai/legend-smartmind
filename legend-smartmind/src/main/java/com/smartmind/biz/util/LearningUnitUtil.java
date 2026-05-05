package com.smartmind.biz.util;

import com.smartmind.biz.bo.dto.learningunit.LearningUnitNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 学习单元工具类
 * Markdown解析和转换工具
 * 支持标题和列表项两种格式
 */
public class LearningUnitUtil {

    /**
     * 解析Markdown为学习单元节点树
     * 支持格式：
     * - 标题：# ## ### #### （层级关系）
     * - 列表项：- 或 * （作为子内容或子章节）
     *
     * @param markdown Markdown内容
     * @return 根节点列表
     */
    public static List<LearningUnitNode> parseMarkdown(String markdown) {
        List<LearningUnitNode> roots = new ArrayList<>();
        if (markdown == null || markdown.trim().isEmpty()) {
            return roots;
        }

        List<LineInfo> lines = parseLines(markdown);
        return buildTree(lines);
    }

    /**
     * 解析所有行，提取标题和列表项
     */
    private static List<LineInfo> parseLines(String markdown) {
        List<LineInfo> lines = new ArrayList<>();

        for (String rawLine : markdown.split("\n")) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            // 标题：1-4级
            if (line.startsWith("#") && !line.startsWith("#####")) {
                int level = 0;
                while (level < line.length() && line.charAt(level) == '#') {
                    level++;
                }
                String title = line.substring(level).trim();
                if (!title.isEmpty()) {
                    lines.add(new LineInfo(LineType.HEADING, level, title));
                }
                continue;
            }

            // 列表项：- 或 *
            if (line.startsWith("-") || line.startsWith("*")) {
                String content = line.substring(1).trim();
                if (!content.isEmpty()) {
                    lines.add(new LineInfo(LineType.LIST_ITEM, 0, content));
                }
            }
        }

        return lines;
    }

    /**
     * 构建树形结构
     * 策略：
     * 1. 标题按层级构建树
     * 2. 列表项归属于最近的标题，如果列表项前面没有标题，则作为独立节点
     * 3. 如果列表项出现在二级标题下，则作为该标题的内容
     * 4. 如果列表项出现在三级标题下，则作为该标题的内容
     */
    private static List<LearningUnitNode> buildTree(List<LineInfo> lines) {
        List<LearningUnitNode> roots = new ArrayList<>();
        List<LearningUnitNode> stack = new ArrayList<>();
        LearningUnitNode lastNode = null;

        for (LineInfo line : lines) {
            if (line.type == LineType.HEADING) {
                // 标题：创建节点
                if (line.level == 1) {
                    // 一级标题：课程名，跳过
                    continue;
                }

                // 映射层级：2->1(大单元), 3->2(子单元), 4->3(课时)
                int unitLevel = line.level - 1;
                LearningUnitNode node = new LearningUnitNode(unitLevel, line.text);

                // 找到父节点
                while (!stack.isEmpty()) {
                    LearningUnitNode last = stack.get(stack.size() - 1);
                    if (last.getLevel() < unitLevel) {
                        last.getChildren().add(node);
                        break;
                    }
                    stack.remove(stack.size() - 1);
                }

                // 没有父节点，作为根节点（大单元）
                if (stack.isEmpty() && unitLevel == 1) {
                    roots.add(node);
                }

                stack.add(node);
                lastNode = node;

            } else if (line.type == LineType.LIST_ITEM) {
                // 列表项：作为当前节点的内容
                if (lastNode != null) {
                    // 如果有最近的节点，作为该节点的内容
                    lastNode.addContent(line.text);
                } else {
                    // 没有前置节点，创建一个课时节点（默认层级3）
                    LearningUnitNode node = new LearningUnitNode(3, line.text);
                    roots.add(node);
                    lastNode = node;
                    stack.add(node);
                }
            }
        }

        return roots;
    }

    /**
     * 统计Markdown中的有效内容行数（标题+列表项）
     */
    public static int countContentLines(String markdown) {
        int count = 0;
        for (String line : markdown.split("\n")) {
            String trim = line.trim();
            if (trim.startsWith("#") && !trim.startsWith("#####")) {
                count++;
            } else if (trim.startsWith("-") || trim.startsWith("*")) {
                count++;
            }
        }
        return count;
    }

    /**
     * 将节点列表扁平化为层级列表
     */
    public static void flattenNodes(List<LearningUnitNode> nodes, List<LearningUnitNode> results, int level) {
        for (LearningUnitNode node : nodes) {
            results.add(node);
            if (!node.getChildren().isEmpty()) {
                flattenNodes(node.getChildren(), results, level + 1);
            }
        }
    }

    /**
     * 行类型枚举
     */
    private enum LineType {
        HEADING,    // 标题
        LIST_ITEM   // 列表项
    }

    /**
     * 行信息内部类
     */
    private static class LineInfo {
        final LineType type;
        final int level;      // 标题层级（列表项为0）
        final String text;    // 内容

        LineInfo(LineType type, int level, String text) {
            this.type = type;
            this.level = level;
            this.text = text;
        }
    }
}
