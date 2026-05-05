package com.smartmind.ai.api;

import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.smartmind.ai.agent.CourseOutlineAgent;
import com.smartmind.ai.agent.PPTGenerationAgent;
import com.smartmind.ai.agent.TeachingPlanAgent;
import com.smartmind.ai.agent.TeachingPlanBaseInfoAgent;
import com.smartmind.ai.agent.ExperimentGuideAgent;
import com.smartmind.ai.agent.ExperimentGuideBaseInfoAgent;
import com.smartmind.ai.agent.QuestionGenerationAgent;
import com.smartmind.ai.agent.TopicGenerationAgent;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanBaseInfoDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanBaseInfoRequestDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideBaseInfoDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideBaseInfoRequestDto;
import com.smartmind.biz.bo.dto.question.QuestionGenerationRequestDto;
import com.smartmind.biz.bo.dto.topic.TopicGenerationRequestDto;
import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.TeachingPlan;
import com.smartmind.biz.service.ICourseService;

import org.legend.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

/**
 * AI对话控制器
 * 提供大模型交互的流式接口
 */
@Controller
@RequestMapping("/api/smartmind/ai")
public class AiChatController implements BaseController  {

    @Autowired
    private CourseOutlineAgent courseOutlineAgent;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private PPTGenerationAgent pptGenerationAgent;

    @Autowired
    private TeachingPlanAgent teachingPlanAgent;

    @Autowired
    private TeachingPlanBaseInfoAgent teachingPlanBaseInfoAgent;

    @Autowired
    private ExperimentGuideAgent experimentGuideAgent;

    @Autowired
    private ExperimentGuideBaseInfoAgent experimentGuideBaseInfoAgent;

    @Autowired
    private QuestionGenerationAgent questionGenerationAgent;

    @Autowired
    private TopicGenerationAgent topicGenerationAgent;

    /**
     * 流式生成课程大纲
     *
     * @param prompt   用户输入的描述/要求
     * @param courseId 课程ID
     * @return Flux<String> 流式大纲内容
     * @throws GraphRunnerException 
     */
    @GetMapping(value = "/stream/outline", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamGenerateOutline(
            @RequestParam String prompt,
            @RequestParam Long courseId) throws GraphRunnerException {

        // 查询课程信息
        Course course = courseService.loadById(courseId).orElse(null);

        if (course == null) {
            return Flux.just("错误：课程不存在", "[DONE]");
        }

        // 调用智能体生成大纲（流式输出）
        // Spring 会自动处理 SSE 格式，无需手动添加 data: 前缀
        return courseOutlineAgent.generateOutline(
                course.getCourseName(),
                course.getSubjectType(),
                course.getGradeLevel(),
                course.getCourseDesc(),
                prompt
        );
    }

    /**
     * 流式生成 PPT 课件
     *
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户输入的需求
     * @return Flux<String> 流式 PPT Markdown 内容
     * @throws GraphRunnerException 
     */
    @GetMapping(value = "/stream/ppt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @ResponseBody Flux<String> streamGeneratePPT(
            @RequestParam Long courseId,
            @RequestParam String unitCode,
            @RequestParam(required = false) String userPrompt) throws GraphRunnerException {

        return pptGenerationAgent.generatePPT(courseId, unitCode, userPrompt);
    }

    /**
     * 生成教案基础信息（结构化输出）
     *
     * @param requestDto 请求参数
     * @return TeachingPlanBaseInfoDto 教案基础信息
     */
    @PostMapping(value = "/teaching-plan/base-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object generateTeachingPlanBaseInfo(@RequestBody TeachingPlanBaseInfoRequestDto requestDto) {
        return ajaxQueryResult(teachingPlanBaseInfoAgent.generate(requestDto));
    }

    /**
     * 流式生成教案
     *
     * @param planId     教案ID
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户输入的需求
     * @return Flux<String> 流式教案 Markdown 内容
     * @throws GraphRunnerException 
     */
    @GetMapping(value = "/stream/teaching-plan", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @ResponseBody Flux<String> streamGenerateTeachingPlan(
            @RequestParam Long planId,
            @RequestParam Long courseId,
            @RequestParam String unitCode,
            @RequestParam(required = false) String userPrompt) throws GraphRunnerException {

        return teachingPlanAgent.generateTeachingPlan(planId, courseId, unitCode, userPrompt);
    }

    /**
     * 生成实验指导书基础信息（结构化输出）
     *
     * @param requestDto 请求参数
     * @return ExperimentGuideBaseInfoDto 实验指导书基础信息
     */
    @PostMapping(value = "/experiment-guide/base-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object generateExperimentGuideBaseInfo(@RequestBody ExperimentGuideBaseInfoRequestDto requestDto) {
        return ajaxQueryResult(experimentGuideBaseInfoAgent.generate(requestDto));
    }

    /**
     * 流式生成实验指导书
     *
     * @param guideId    指导书ID
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户输入的需求
     * @return Flux<String> 流式实验指导书 Markdown 内容
     * @throws GraphRunnerException 
     */
    @GetMapping(value = "/stream/experiment-guide", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public @ResponseBody Flux<String> streamGenerateExperimentGuide(
            @RequestParam Long guideId,
            @RequestParam Long courseId,
            @RequestParam String unitCode,
            @RequestParam(required = false) String userPrompt) throws GraphRunnerException {

        return experimentGuideAgent.generateExperimentGuide(guideId, courseId, unitCode, userPrompt);
    }

    /**
     * AI出题（结构化输出）
     *
     * @param requestDto 请求参数
     * @return 生成的题目列表
     */
    @PostMapping(value = "/question/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object generateQuestions(@RequestBody QuestionGenerationRequestDto requestDto) {
        return ajaxQueryResult(questionGenerationAgent.generate(requestDto).getQuestions());
    }

    /**
     * AI生成讨论话题（结构化输出）
     *
     * @param requestDto 请求参数
     * @return 生成的话题列表
     */
    @PostMapping(value = "/topic/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object generateTopics(@RequestBody TopicGenerationRequestDto requestDto) {
        return ajaxQueryResult(topicGenerationAgent.generate(requestDto));
    }
}
