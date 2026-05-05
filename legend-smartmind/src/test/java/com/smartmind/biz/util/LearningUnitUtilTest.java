//package com.smartmind.biz.util;
//
//import com.smartmind.biz.bo.dto.learningunit.LearningUnitNode;
//import org.junit.Test;
//
//import java.util.List;
//
//public class LearningUnitUtilTest {
//
//    @Test
//    public void testParseMarkdown() {
//        String markdown = """
//                # 高中数学必修一
//                
//                ## 第一单元 集合与函数
//                ### 1.1 集合的概念
//                #### 课时1 集合的含义与表示
//                #### 课时2 集合间的基本关系
//                ### 1.2 函数的概念
//                #### 课时3 函数的概念
//                #### 课时4 函数的表示法
//                
//                ## 第二单元 基本初等函数
//                ### 2.1 指数函数
//                #### 课时5 指数与指数幂
//                #### 课时6 指数函数及其性质
//                ### 2.2 对数函数
//                #### 课时7 对数与对数运算
//                """;
//
//        List<LearningUnitNode> roots = LearningUnitUtil.parseMarkdown(markdown);
//        
//        System.out.println("解析结果：");
//        printNodes(roots, 0);
//    }
//
//    private void printNodes(List<LearningUnitNode> nodes, int indent) {
//        String prefix = "  ".repeat(indent);
//        for (LearningUnitNode node : nodes) {
//            System.out.println(prefix + node.getLevelText() + ": " + node.getTitle() 
//                + " (子节点数: " + node.getChildren().size() + ")");
//            if (node.hasChildren()) {
//                printNodes(node.getChildren(), indent + 1);
//            }
//        }
//    }
//}
