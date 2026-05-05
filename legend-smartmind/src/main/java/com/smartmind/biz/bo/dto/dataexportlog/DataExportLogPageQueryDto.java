package com.smartmind.biz.bo.dto.dataexportlog;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "数据导出日志分页查询DTO")
public class DataExportLogPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "导出名称")
	private java.lang.String exportName;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "单元ID")
	private java.lang.Long unitId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "文件名")
	private java.lang.String fileName;
	
	@Schema(description = "导出时间开始")
	private java.util.Date exportTimeStart;
	@Schema(description = "导出时间截止")
	private java.util.Date exportTimeEnd;
	


}
