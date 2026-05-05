package com.smartmind;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/** 自动化渗透测试系统主入口 */
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages= { "org.legend","com.smartmind","com.smartmind.biz","com.smartmind.rag","com.smartmind.ai","com.jxms"})
public class SmartMindApplication {
     
  public static void main(String[] args) {   
	        
	  ConfigurableApplicationContext cc=SpringApplication.run(SmartMindApplication.class, args);
//	  List.of( cc.getBeanDefinitionNames()).forEach(v->System.out.println(v));
  }
}
