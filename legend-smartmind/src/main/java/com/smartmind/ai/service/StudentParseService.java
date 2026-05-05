package com.smartmind.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.vo.StudentParseResultVo;
import lombok.extern.slf4j.Slf4j;
import org.legend.framework.core.util.ZAlert;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生信息解析服务
 * 使用大模型解析文本中的学生信息
 */
@Slf4j
@Service
public class StudentParseService {

    @Autowired(required = false)
    private ChatModel chatModel;

    @Autowired
    private ObjectMapper objectMapper;

    // 文本长度限制：最大5000字符
    private static final int MAX_TEXT_LENGTH = 5000;

    /**
     * 解析文本中的学生信息
     *
     * @param text 用户粘贴的文本
     * @param classList 系统中已有的班级列表，用于智能匹配
     * @return 解析出的学生信息列表
     */
    public List<StudentParseResultVo> parseStudentInfo(String text, List<com.smartmind.biz.bo.model.Clazz> classList) {
        if (chatModel == null) {
            log.warn("ChatModel not available, cannot parse student info");
            return Collections.emptyList();
        }

        // 文本长度限制检查
        if (text == null || text.isEmpty()) {
            ZAlert.throwSLE("解析内容不能为空");
        }

        if (text.length() > MAX_TEXT_LENGTH) {
            ZAlert.throwSLE("解析内容过长，最大支持" + MAX_TEXT_LENGTH + "字符，当前" + text.length() + "字符");
        }

        try {
            String prompt = buildPrompt(text, classList);
            log.debug("AI parse prompt length: {}", prompt.length());
            String response = chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
            
            log.debug("AI parse response: {}", response);
            
            // 提取JSON部分
            String json = extractJson(response);
            
            // 解析JSON
            List<StudentParseResultVo> result = objectMapper.readValue(json, 
                new TypeReference<List<StudentParseResultVo>>() {});
            
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to parse student info from text", e);
            return Collections.emptyList();
        }
    }

    /**
     * 构建提示词，包含班级列表用于智能匹配
     */
    private String buildPrompt(String text, List<com.smartmind.biz.bo.model.Clazz> classList) {
        // 构建班级列表字符串
        String classListStr = "";
        if (classList != null && !classList.isEmpty()) {
            classListStr = classList.stream()
                .filter(c -> c.getClassName() != null)
                .map(c -> String.format("  - %s (ID:%d)", c.getClassName(), c.getId()))
                .collect(Collectors.joining("\n"));
        }
        
        return String.format(
            "请从以下文本中识别学生信息，并以JSON数组格式返回。\n\n" +
            "【系统中已有的班级列表】\n" +
            "%s\n\n" +
            "【文本内容】\n" +
            "%s\n\n" +
            "【识别要求】\n" +
            "请识别以下字段（如无法识别则留空或null）：\n" +
            "- studentName: 学生姓名\n" +
            "- studentNo: 学号\n" +
            "- gender: 性别（男=1, 女=0, 其他=2）\n" +
            "- phone: 联系电话\n" +
            "- classId: 班级ID（必须从上面的班级列表中选择最匹配的ID）\n" +
            "- className: 班级名称（必须从上面的班级列表中选择最匹配的名称）\n\n" +
            "【匹配规则】\n" +
            "1. 班级匹配：用户输入的班级名称可能与系统不完全一致，请智能匹配最相似的班级\n" +
            "   例如：用户输入\"一班\"应匹配\"一年级一班\"，输入\"1班\"也应匹配\"一年级一班\"\n" +
            "2. 如果无法确定班级，classId和className都留空\n" +
            "3. 性别识别：男=1, 女=0, 其他或未知=2\n\n" +
            "【返回格式】\n" +
            "1. 只返回JSON数组，不要任何其他说明文字\n" +
            "2. 每个数组元素是一个学生对象\n" +
            "3. 字段名使用camelCase\n" +
            "4. 如果文本中没有识别到任何学生信息，返回空数组[]\n\n" +
            "【示例输出】\n" +
            "[\n" +
            "  {\"studentName\": \"张三\", \"studentNo\": \"2024001\", \"gender\": \"1\", \"phone\": \"13800138001\", \"classId\": 1, \"className\": \"一年级一班\"},\n" +
            "  {\"studentName\": \"李四\", \"studentNo\": \"2024002\", \"gender\": \"0\", \"phone\": \"13900139001\", \"classId\": 2, \"className\": \"一年级二班\"}\n" +
            "]",
            classListStr.isEmpty() ? "  （暂无班级数据）" : classListStr,
            text
        );
    }

    /**
     * 从响应中提取JSON
     */
    private String extractJson(String response) {
        if (response == null || response.isEmpty()) {
            return "[]";
        }
        
        // 尝试直接解析
        response = response.trim();
        
        // 如果包含markdown代码块，提取其中的内容
        if (response.contains("```json")) {
            int start = response.indexOf("```json") + 7;
            int end = response.lastIndexOf("```");
            if (end > start) {
                response = response.substring(start, end).trim();
            }
        } else if (response.contains("```")) {
            int start = response.indexOf("```") + 3;
            int end = response.lastIndexOf("```");
            if (end > start) {
                response = response.substring(start, end).trim();
            }
        }
        
        // 查找JSON数组的开始和结束
        int startIdx = response.indexOf("[");
        int endIdx = response.lastIndexOf("]");
        
        if (startIdx >= 0 && endIdx > startIdx) {
            return response.substring(startIdx, endIdx + 1);
        }
        
        return "[]";
    }
}
