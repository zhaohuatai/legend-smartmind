package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.dto.topic.TopicGenerationRequestDto;
import com.smartmind.biz.bo.dto.topic.TopicGenerationResultDto;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TopicGenerationAgent {

    private final ReactAgent agent;
    private final ObjectMapper objectMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    private static final Map<String, String> TOPIC_TYPE_MAP = Map.of(
            "1", "开放讨论", "2", "辩论赛", "3", "案例分析", "4", "小组研讨", "5", "头脑风暴"
    );

    public TopicGenerationAgent(ChatModel chatModel, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        if (chatModel != null) {
            this.agent = ReactAgent.builder()
                    .name("TopicGenerationAgent")
                    .model(chatModel)
                    .outputType(TopicGenerationResultDto.class)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    public TopicGenerationResultDto generate(TopicGenerationRequestDto requestDto) {
        if (agent == null) {
            log.warn("Agent not available, cannot generate topic");
            return new TopicGenerationResultDto();
        }

        Course course = courseService.loadById(requestDto.getCourseId()).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", requestDto.getUnitCode()).orElse(null);

        String knowledgeContext = queryKnowledgeBase(course.getId(), unit.getUnitCode());

        String prompt = buildPrompt(course, unit, requestDto, knowledgeContext);
        log.info("AI generate topic prompt：{}", prompt);

        try {
            AssistantMessage result = agent.call(prompt);
            log.info("AI generate topic response : {}", result.getText());

            TopicGenerationResultDto topicResult = objectMapper.readValue(
                    result.getText(), TopicGenerationResultDto.class);
            return topicResult != null ? topicResult : new TopicGenerationResultDto();
        } catch (Exception e) {
            log.error("Failed to generate topic", e);
            return new TopicGenerationResultDto();
        }
    }

    public String queryKnowledgeBase(Long courseId, String unitCode) {
        return "";
    }

    private String buildPrompt(Course course, LearningUnit unit, TopicGenerationRequestDto requestDto, String knowledgeContext) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("你是一位经验丰富的教育专家和课堂活动设计专家，请根据以下教学内容，生成一个高质量的课堂讨论活动话题。\n\n");

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

        prompt.append("## 话题生成要求\n");
        prompt.append("- 话题类型: ").append(TOPIC_TYPE_MAP.getOrDefault(requestDto.getTopicType(), "自动分配")).append("\n");
        prompt.append("\n");

        if (requestDto.getEstimatedDuration() != null) {
            prompt.append("- 预计讨论时长: ").append(requestDto.getEstimatedDuration()).append("分钟\n");
        }
        if (requestDto.getGroupSize() != null) {
            prompt.append("- 建议小组人数: ").append(requestDto.getGroupSize()).append("人\n");
        }
        prompt.append("\n");

        if (requestDto.getContentDescription() != null && !requestDto.getContentDescription().isEmpty()) {
            prompt.append("## 内容说明\n").append(requestDto.getContentDescription()).append("\n\n");
        }

        prompt.append("## 输出格式要求\n");
        prompt.append("请严格按照以下JSON格式输出，不要添加任何其他内容：\n");
        prompt.append("{\n");
        prompt.append("  \"topicType\": \"").append(requestDto.getTopicType()).append("\",\n");
        prompt.append("  \"topicName\": \"话题名称\",\n");
        prompt.append("  \"topicContent\": \"话题内容/讨论问题\",\n");
        prompt.append("  \"estimatedDuration\": ").append(requestDto.getEstimatedDuration() != null ? requestDto.getEstimatedDuration() : 15).append(",\n");
        prompt.append("  \"groupSize\": ").append(requestDto.getGroupSize() != null ? requestDto.getGroupSize() : 4).append(",\n");
        prompt.append("  \"knowledgePoints\": \"知识点1,知识点2\",\n");
        prompt.append("  \"backgroundMaterial\": \"背景材料\",\n");
        prompt.append("  \"guidanceTips\": \"教师引导提示\",\n");
        prompt.append("  \"expectedAnswers\": \"预期答案要点\",\n");
        prompt.append("  \"evaluationCriteria\": \"评价标准\"\n");
        prompt.append("}\n\n");

        prompt.append("## 注意事项\n");
        prompt.append("1. 话题内容必须与课程和单元内容紧密相关\n");
        prompt.append("2. 话题名称要简洁有吸引力，能够激发学生讨论兴趣\n");
        prompt.append("3. 话题内容要具体明确，让学生知道要讨论什么\n");
        if ("1".equals(requestDto.getTopicType())) {
            prompt.append("4. 开放讨论类话题应没有固定答案，鼓励多元思考\n");
        } else if ("2".equals(requestDto.getTopicType())) {
            prompt.append("4. 辩论赛类话题应有明确的正反方观点\n");
        } else if ("3".equals(requestDto.getTopicType())) {
            prompt.append("4. 案例分析类话题应提供具体的案例背景\n");
        } else if ("4".equals(requestDto.getTopicType())) {
            prompt.append("4. 小组研讨类话题应适合小组合作完成\n");
        } else if ("5".equals(requestDto.getTopicType())) {
            prompt.append("4. 头脑风暴类话题应能激发创意和想象力\n");
        }
        prompt.append("5. 只输出JSON，不要添加任何其他文字说明或markdown标记\n");

        return prompt.toString();
    }
}
