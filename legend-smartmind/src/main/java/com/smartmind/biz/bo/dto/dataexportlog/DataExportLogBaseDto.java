package com.smartmind.biz.bo.dto.dataexportlog;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class DataExportLogBaseDto{

	
	public DataExportLogBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="导出名称不能为空")
	@jakarta.validation.constraints.NotNull(message="导出名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="导出名称长度不能大于200")
	@Schema(description = "导出名称")
 	private java.lang.String exportName;
 
	@jakarta.validation.constraints.NotBlank(message="导出类型: 1-原始数据, 2-统计报表, 3-图表不能为空")
	@jakarta.validation.constraints.NotNull(message="导出类型: 1-原始数据, 2-统计报表, 3-图表不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="导出类型: 1-原始数据, 2-统计报表, 3-图表长度不能大于10")
	@Schema(description = "导出类型: 1-原始数据, 2-统计报表, 3-图表")
 	private java.lang.String exportType;
 
	@jakarta.validation.constraints.NotBlank(message="数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁不能为空")
	@jakarta.validation.constraints.NotNull(message="数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁长度不能大于10")
	@Schema(description = "数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁")
 	private java.lang.String dataType;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@Schema(description = "单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="文件名长度不能大于200")
	@Schema(description = "文件名")
 	private java.lang.String fileName;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="文件下载地址长度不能大于500")
	@Schema(description = "文件下载地址")
 	private java.lang.String fileUrl;
 

	@Schema(description = "文件大小(字节)")
 	private java.lang.Long fileSize;
 

	@Schema(description = "记录数")
 	private java.lang.Integer recordCount;
 

	@jakarta.validation.constraints.NotNull(message="导出时间不能为空")

	@Schema(description = "导出时间")
 	private java.util.Date exportTime;
 

	@Schema(description = "过期时间")
 	private java.util.Date expireTime;
 

	@Schema(description = "下载次数")
 	private java.lang.Integer downloadCount;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-生成中, 1-已完成, 2-已过期不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-生成中, 1-已完成, 2-已过期不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-生成中, 1-已完成, 2-已过期长度不能大于2")
	@Schema(description = "状态: 0-生成中, 1-已完成, 2-已过期")
 	private java.lang.String status;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="导出参数长度不能大于1,073,741,824")
	@Schema(description = "导出参数")
 	private java.lang.String exportParams;
 
	

}
