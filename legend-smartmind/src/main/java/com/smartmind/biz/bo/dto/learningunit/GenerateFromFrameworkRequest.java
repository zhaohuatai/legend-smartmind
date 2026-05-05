package com.smartmind.biz.bo.dto.learningunit;

import lombok.Data;

/**
 * 从知识结构生成请求DTO
 */
@Data
public  class GenerateFromFrameworkRequest {
    private Long courseId;
    private String markdownText;
    /**
     * 生成章节层级: 2-两层(大单元、子单元), 3-三层(大单元、子单元、课时)
     */
    private Integer level;
}