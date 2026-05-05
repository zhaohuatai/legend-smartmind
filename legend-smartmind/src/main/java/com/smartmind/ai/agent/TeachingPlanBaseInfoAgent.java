package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanBaseInfoDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanBaseInfoRequestDto;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeachingPlanBaseInfoAgent {

    private final ReactAgent agent;
    private final ObjectMapper objectMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    public TeachingPlanBaseInfoAgent(ChatModel chatModel, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        if (chatModel != null) {
            this.agent = ReactAgent.builder()
                    .name("TeachingPlanBaseInfoAgent")
                    .model(chatModel)
                    .outputType(TeachingPlanBaseInfoDto.class)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    public TeachingPlanBaseInfoDto generate(TeachingPlanBaseInfoRequestDto requestDto) {
        if (agent == null) {
            log.warn("Agent not available, cannot generate base info");
            return new TeachingPlanBaseInfoDto();
        }

        Course course = courseService.loadById(requestDto.getCourseId()).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", requestDto.getUnitCode()).orElse(null);

        String prompt = buildPrompt(course, unit, requestDto);
        log.debug("AI generate base info prompt: {}", prompt);

        try {
            AssistantMessage result = agent.call(prompt);
            log.debug("AI generate base info response: {}", result.getText());

            TeachingPlanBaseInfoDto dto = objectMapper.readValue(result.getText(), TeachingPlanBaseInfoDto.class);
            return dto != null ? dto : new TeachingPlanBaseInfoDto();
        } catch (Exception e) {
            log.error("Failed to generate teaching plan base info", e);
            return new TeachingPlanBaseInfoDto();
        }
    }

    private String buildPrompt(Course course, LearningUnit unit, TeachingPlanBaseInfoRequestDto requestDto) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请根据以下教学内容，生成教案基础信息。\n\n");

        prompt.append("## 教案名称\n");
        if (requestDto.getPlanName() != null) {
            prompt.append("- 教案名称: ").append(requestDto.getPlanName()).append("\n");
        }
        prompt.append("\n");

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
            if (unit.getUnitCode() != null) {
                prompt.append("- 单元编码: ").append(unit.getUnitCode()).append("\n");
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

        if (requestDto.getLessonSession() != null) {
            prompt.append("## 课时信息\n");
            prompt.append("- 第几次课: ").append(requestDto.getLessonSession()).append("\n\n");
        }

        if (requestDto.getContentDescription() != null && !requestDto.getContentDescription().isEmpty()) {
            prompt.append("## 教案内容说明\n").append(requestDto.getContentDescription()).append("\n\n");
        }

        prompt.append("请根据以上信息，生成教案基础信息，包括：课时主题、授课内容简介、预计课时数、知识目标、能力目标、素养目标、教学重点、教学难点、教学方法列表、教学工具/媒体、教学主要内容、教学反思。");

        return prompt.toString();
    }
}
