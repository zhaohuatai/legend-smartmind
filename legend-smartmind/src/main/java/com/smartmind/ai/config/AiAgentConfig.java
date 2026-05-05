package com.smartmind.ai.config;

import com.smartmind.ai.agent.CourseOutlineAgent;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI智能体配置类
 */
@Configuration
public class AiAgentConfig {

    @Autowired(required = false)
    private ChatModel chatModel;

    @Bean
    public CourseOutlineAgent courseOutlineAgent() {
        return new CourseOutlineAgent(chatModel);
    }
}
