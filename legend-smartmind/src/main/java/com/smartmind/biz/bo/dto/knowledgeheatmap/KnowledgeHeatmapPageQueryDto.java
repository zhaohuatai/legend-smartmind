package com.smartmind.biz.bo.dto.knowledgeheatmap;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "知识点热力图分页查询DTO")
public class KnowledgeHeatmapPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "单元ID")
	private java.lang.Long unitId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "知识点标签")
	private java.lang.String knowledgeTag;
	
	@Schema(description = "统计时间开始")
	private java.util.Date statisticsTimeStart;
	@Schema(description = "统计时间截止")
	private java.util.Date statisticsTimeEnd;
	


}
