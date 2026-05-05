package com.smartmind.biz.bo.dto.teachingfile;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class TeachingFileBaseDto{

	
	public TeachingFileBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="文件名称不能为空")
	@jakarta.validation.constraints.NotNull(message="文件名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="文件名称长度不能大于200")
	@Schema(description = "文件名称")
 	private java.lang.String fileName;
 
	@jakarta.validation.constraints.NotBlank(message="文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他不能为空")
	@jakarta.validation.constraints.NotNull(message="文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他长度不能大于10")
	@Schema(description = "文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他")
 	private java.lang.String fileType;
 
	@jakarta.validation.constraints.NotBlank(message="文件地址不能为空")
	@jakarta.validation.constraints.NotNull(message="文件地址不能为空")
	@jakarta.validation.constraints.Size(min=0,max=500,message="文件地址长度不能大于500")
	@Schema(description = "文件地址")
 	private java.lang.String fileUrl;
 

	@Schema(description = "文件大小(字节)")
 	private java.lang.Long fileSize;
 

	@Schema(description = "关联课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@Schema(description = "关联单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="上传类型: 1-本地上传, 2-资源库引用长度不能大于10")
	@Schema(description = "上传类型: 1-本地上传, 2-资源库引用")
 	private java.lang.String uploadType;
 

	@Schema(description = "使用次数")
 	private java.lang.Integer usageCount;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-失效, 1-有效长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效")
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI提取的内容文本长度不能大于65,535")
	@Schema(description = "AI提取的内容文本")
 	private java.lang.String aiExtractedContent;
 
	

}
