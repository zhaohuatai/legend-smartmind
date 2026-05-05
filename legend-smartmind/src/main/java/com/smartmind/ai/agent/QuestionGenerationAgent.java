package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.dto.question.QuestionGenerationBatchResultDto;
import com.smartmind.biz.bo.dto.question.QuestionGenerationRequestDto;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class QuestionGenerationAgent {

    private final ReactAgent agent;
    private final ObjectMapper objectMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

  

    private static final Map<String, String> QUESTION_TYPE_MAP = Map.of(
            "1", "单选题", "2", "多选题", "3", "判断题",
            "4", "填空题", "5", "简答题", "6", "计算题"
    );

    private static final Map<String, String> DIFFICULTY_MAP = Map.of(
            "1", "容易", "2", "较易", "3", "中等", "4", "较难", "5", "困难"
    );

    private static final Map<String, String> COGNITIVE_MAP = Map.of(
            "1", "识记", "2", "理解", "3", "应用", "4", "分析", "5", "综合", "6", "评价"
    );

    private static final Map<String, Integer> SCORE_MAP = Map.of(
            "1", 2, "2", 3, "3", 2, "4", 3, "5", 5, "6", 5
    );

    private final Random random = new Random();

    public QuestionGenerationAgent(ChatModel chatModel, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        if (chatModel != null) {
            this.agent = ReactAgent.builder()
                    .name("QuestionGenerationAgent")
                    .model(chatModel)
                    .outputType(QuestionGenerationBatchResultDto.class)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    public QuestionGenerationBatchResultDto generate(QuestionGenerationRequestDto requestDto) {
        if (agent == null) {
            log.warn("Agent not available, cannot generate questions");
            return new QuestionGenerationBatchResultDto();
        }

        Course course = courseService.loadById(requestDto.getCourseId()).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", requestDto.getUnitCode()).orElse(null);

        String knowledgeContext =queryKnowledgeBase(course.getId(), unit.getUnitCode());

        String prompt = buildPrompt(course, unit, requestDto, knowledgeContext);
        log.info("AI generate questions prompt：{}", prompt);

        try {
            AssistantMessage result = agent.call(prompt);
            log.info("AI generate questions response : {}", result.getText());

            QuestionGenerationBatchResultDto batchResult = objectMapper.readValue(
                    result.getText(), QuestionGenerationBatchResultDto.class);
            return batchResult != null ? batchResult : new QuestionGenerationBatchResultDto();
        } catch (Exception e) {
            log.error("Failed to generate questions", e);
            return new QuestionGenerationBatchResultDto();
        }
    }

    /**
     * 查询知识库（空实现，待后续接入RAG或LLM Wiki）
     *
     * @param courseId 课程ID
     * @param unitCode 单元编码
     * @return 知识库查询结果
     */
    public String queryKnowledgeBase(Long courseId, String unitCode) {
        // TODO: 后续接入知识库查询（RAG/LLM Wiki）
        // 当前返回空字符串
        return "";
    }

    
    private String buildPrompt(Course course, LearningUnit unit, QuestionGenerationRequestDto requestDto, String knowledgeContext) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("你是一位经验丰富的教育专家和出题专家，请根据以下教学内容，生成高质量的考试题目。\n\n");

        prompt.append("## 课程信息\n");
        if (course != null) {
            if (course.getCourseName() != null) {
                prompt.append("- 课程名称: ").append(course.getCourseName()).append("\n");
            }
            if (course.getCourseDesc() != null) {
                prompt.append("- 课程描述: ").append(course.getCourseDesc()).append("\n");
            }
        }
        prompt.append("\n");

        prompt.append("## 单元信息\n");
        if (unit != null) {
            if (unit.getUnitName() != null) {
                prompt.append("- 单元名称: ").append(unit.getUnitName()).append("\n");
            }
            if (unit.getUnitDesc() != null) {
                prompt.append("- 单元描述: ").append(unit.getUnitDesc()).append("\n");
            }
            if (unit.getUnitObjectives() != null) {
                prompt.append("- 单元目标: ").append(unit.getUnitObjectives()).append("\n");
            }
            if (unit.getKeyPoints() != null) {
                prompt.append("- 重难点: ").append(unit.getKeyPoints()).append("\n");
            }
            if (unit.getKnowledgeTags() != null) {
                prompt.append("- 知识点标签: ").append(unit.getKnowledgeTags()).append("\n");
            }
        }
        prompt.append("\n");

        if (knowledgeContext != null && !knowledgeContext.isEmpty()) {
            prompt.append("## 知识库参考内容\n").append(knowledgeContext).append("\n\n");
        }

        prompt.append("## 出题要求\n");
        prompt.append("- 题目类型: ").append(formatQuestionTypes(requestDto.getQuestionTypes())).append("\n");
        prompt.append("- 出题总数: ").append(requestDto.getTotalCount()).append("题\n");

        if (requestDto.getDifficultyDistribution() != null && !requestDto.getDifficultyDistribution().isEmpty()) {
            prompt.append("- 难度分布: ").append(formatDistribution(requestDto.getDifficultyDistribution(), DIFFICULTY_MAP)).append("\n");
        } else if (requestDto.getDifficultyLevels() != null && !requestDto.getDifficultyLevels().isEmpty()) {
            prompt.append("- 难度等级范围: ").append(formatDifficultyLevels(requestDto.getDifficultyLevels())).append("\n");
        } else if (requestDto.getDifficultyLevel() != null) {
            prompt.append("- 难度等级: ").append(DIFFICULTY_MAP.getOrDefault(requestDto.getDifficultyLevel(), "自动分配")).append("\n");
        } else {
            prompt.append("- 难度等级: 请根据总数自动分配，确保有易有难\n");
        }

        if (requestDto.getCognitiveDistribution() != null && !requestDto.getCognitiveDistribution().isEmpty()) {
            prompt.append("- 认知层次分布: ").append(formatDistribution(requestDto.getCognitiveDistribution(), COGNITIVE_MAP)).append("\n");
        } else if (requestDto.getCognitiveLevels() != null && !requestDto.getCognitiveLevels().isEmpty()) {
            prompt.append("- 认知层次: ").append(formatCognitiveLevels(requestDto.getCognitiveLevels())).append("\n");
        } else {
            prompt.append("- 认知层次: 请根据总数自动分配，覆盖识记、理解、应用、分析等层次\n");
        }
        prompt.append("\n");

        if (requestDto.getContentDescription() != null && !requestDto.getContentDescription().isEmpty()) {
            prompt.append("## 内容说明\n").append(requestDto.getContentDescription()).append("\n\n");
        }

        prompt.append("## 字段说明\n");
        prompt.append("- questionType: 题目类型编号，1=单选题, 2=多选题, 3=判断题, 4=填空题, 5=简答题, 6=计算题, 7=应用题, 8=综合题\n");
        prompt.append("- difficultyLevel: 难度等级编号，1=容易, 2=较易, 3=中等, 4=较难, 5=困难\n");
        prompt.append("- cognitiveLevel: 认知层次编号，1=识记, 2=理解, 3=应用, 4=分析, 5=综合, 6=评价\n");
        prompt.append("- options: 选项数组，单选题固定4个选项，多选题4-5个选项，判断题固定[\"正确\",\"错误\"]，填空题和简答题为空数组[]\n");
        prompt.append("- answer: 单选题/判断题为选项字母(A/B/C/D)或\"正确\"/\"错误\"，多选题为逗号分隔的字母如\"A,C\"，填空题为答案文本，简答题为答案要点\n");
        prompt.append("- score: 题目分值，单选题2分，多选题3分，判断题2分，填空题3分，简答题5分，计算题5-10分\n");
        prompt.append("\n");

        prompt.append("## 输出格式要求\n");
        prompt.append("请严格按照以下JSON格式输出，不要添加任何其他内容：\n");
        prompt.append("{\n");
        prompt.append("  \"questions\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"questionType\": \"1\",\n");
        prompt.append("      \"difficultyLevel\": \"3\",\n");
        prompt.append("      \"cognitiveLevel\": \"2\",\n");
        prompt.append("      \"questionContent\": \"题目内容\",\n");
        prompt.append("      \"options\": [\"选项A内容\", \"选项B内容\", \"选项C内容\", \"选项D内容\"],\n");
        prompt.append("      \"answer\": \"A\",\n");
        prompt.append("      \"answerAnalysis\": \"答案解析\",\n");
        prompt.append("      \"score\": 2,\n");
        prompt.append("      \"knowledgePoints\": \"知识点1,知识点2\"\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n\n");

        prompt.append("## 注意事项\n");
        prompt.append("1. 题目内容必须与课程和单元内容紧密相关\n");
        prompt.append("2. 必须严格按照指定的难度分布和认知层次分布生成题目，数量必须完全匹配\n");
        prompt.append("3. 单选题必须有且仅有4个选项，多选题必须有4-5个选项，判断题必须有2个选项\n");
        prompt.append("4. options字段必须是JSON数组，不要包含A/B/C/D前缀，前缀由系统自动添加\n");
        prompt.append("5. 单选题answer字段只填字母如\"A\"，不要填完整选项内容\n");
        prompt.append("6. 多选题answer字段填逗号分隔的字母如\"A,C\"或\"A,B,D\"\n");
        prompt.append("7. 判断题answer字段填\"正确\"或\"错误\"\n");
        prompt.append("8. 分值必须合理：单选题2分，多选题3分，判断题2分，填空题3分，简答题5分，计算题5-10分\n");
        prompt.append("9. 答案解析要详细清晰，帮助学生理解\n");
        prompt.append("10. 只输出JSON，不要添加任何其他文字说明或markdown标记\n");

        return prompt.toString();
    }

    private String formatQuestionTypes(List<String> questionTypes) {
        if (questionTypes == null || questionTypes.isEmpty()) {
            return "自动分配";
        }
        return questionTypes.stream()
                .map(t -> QUESTION_TYPE_MAP.getOrDefault(t, t))
                .reduce((a, b) -> a + "、" + b)
                .orElse("自动分配");
    }

    private String formatCognitiveLevels(List<String> cognitiveLevels) {
        if (cognitiveLevels == null || cognitiveLevels.isEmpty()) {
            return "自动分配";
        }
        return cognitiveLevels.stream()
                .map(l -> COGNITIVE_MAP.getOrDefault(l, l))
                .reduce((a, b) -> a + "、" + b)
                .orElse("自动分配");
    }

    private String formatDifficultyLevels(List<String> difficultyLevels) {
        if (difficultyLevels == null || difficultyLevels.isEmpty()) {
            return "自动分配";
        }
        return difficultyLevels.stream()
                .map(l -> DIFFICULTY_MAP.getOrDefault(l, l))
                .reduce((a, b) -> a + "、" + b)
                .orElse("自动分配");
    }

    private String formatDistribution(Map<String, Integer> distribution, Map<String, String> labelMap) {
        if (distribution == null || distribution.isEmpty()) {
            return "自动分配";
        }
        return distribution.entrySet().stream()
                .map(e -> labelMap.getOrDefault(e.getKey(), e.getKey()) + " " + e.getValue() + "题")
                .reduce((a, b) -> a + "，" + b)
                .orElse("自动分配");
    }
}
