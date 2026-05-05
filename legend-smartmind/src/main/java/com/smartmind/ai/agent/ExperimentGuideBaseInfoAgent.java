package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideBaseInfoDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideBaseInfoRequestDto;
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
public class ExperimentGuideBaseInfoAgent {

    private final ReactAgent agent;
    private final ObjectMapper objectMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    public ExperimentGuideBaseInfoAgent(ChatModel chatModel, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        if (chatModel != null) {
            this.agent = ReactAgent.builder()
                    .name("ExperimentGuideBaseInfoAgent")
                    .model(chatModel)
                    .outputType(ExperimentGuideBaseInfoDto.class)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    public ExperimentGuideBaseInfoDto generate(ExperimentGuideBaseInfoRequestDto requestDto) {
        if (agent == null) {
            log.warn("Agent not available, cannot generate base info");
            return new ExperimentGuideBaseInfoDto();
        }

        Course course = courseService.loadById(requestDto.getCourseId()).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", requestDto.getUnitCode()).orElse(null);

        String prompt = buildPrompt(course, unit, requestDto);
        log.info("AI generate base info prompt: {}", prompt);

        try {
            AssistantMessage result = agent.call(prompt);
            log.info("AI generate base info response: {}", result.getText());

            ExperimentGuideBaseInfoDto dto = objectMapper.readValue(result.getText(), ExperimentGuideBaseInfoDto.class);
            return dto != null ? dto : new ExperimentGuideBaseInfoDto();
        } catch (Exception e) {
            log.error("Failed to generate experiment guide base info", e);
            return new ExperimentGuideBaseInfoDto();
        }
    }

    private String buildPrompt(Course course, LearningUnit unit, ExperimentGuideBaseInfoRequestDto requestDto) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请根据以下教学内容，生成实验指导书基础信息。\n\n");

        prompt.append("## 指导书名称\n");
        if (requestDto.getGuideName() != null) {
            prompt.append("- 指导书名称: ").append(requestDto.getGuideName()).append("\n");
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

        if (requestDto.getExperimentSession() != null) {
            prompt.append("## 实验信息\n");
            prompt.append("- 第几次实验: ").append(requestDto.getExperimentSession()).append("\n\n");
        }

        if (requestDto.getContentDescription() != null && !requestDto.getContentDescription().isEmpty()) {
            prompt.append("## 实验指导书内容说明\n").append(requestDto.getContentDescription()).append("\n\n");
        }

        prompt.append("请根据以上信息，生成实验指导书基础信息，包括：实验主题、预计课时数（学时）、实验目的、实验原理、实验器材/设备、实验步骤、注意事项、实验报告要求。");

        return prompt.toString();
    }
}
