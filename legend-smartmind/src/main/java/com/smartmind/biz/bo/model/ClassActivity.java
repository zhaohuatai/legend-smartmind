package com.smartmind.biz.bo.model;
import org.legend.framework.core.data.IBaseModel;
import org.legend.framework.base.dao.mybatis.annotation.PKId;
import org.legend.framework.base.dao.mybatis.annotation.TableFieldMode;
import org.legend.framework.base.dao.mybatis.enums.FieldMode;
import org.legend.framework.base.dao.mybatis.enums.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@ToString(callSuper = true)
@Table(name="smd_class_activity")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ClassActivity extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "活动编码")
    private java.lang.String activityCode;
    
 	@Schema(description = "活动名称")
    private java.lang.String activityName;
    
 	@Schema(description = "所属课堂ID")
    private java.lang.Long sessionId;
    
 	@Schema(description = "所属课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "上课班级ID")
    private java.lang.Long clazzId;
    
 	@Schema(description = "班级名称")
    private java.lang.String clazzName;
    
 	@Schema(description = "活动类型: 1-随堂测试, 2-随堂练习, 3-课堂实验, 4-话题讨论")
    private java.lang.String activityType;
    
 	@Schema(description = "活动开始时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date startTime;
    
 	@Schema(description = "活动截止时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date endTime;
    
 	@Schema(description = "活动总分")
    private java.math.BigDecimal score;
    
 	@Schema(description = "活动时长(分钟)")
    private java.lang.Integer duration;
    
 	@Schema(description = "状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
    private java.lang.String status;
    
 	@Schema(description = "排序")
    private java.lang.Integer sortOrder;
    
 	@Schema(description = "创建时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    
 	@Schema(description = "更新时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    
 	@Schema(description = "创建人")
    private java.lang.String createBy;
    
 	@Schema(description = "更新人")
    private java.lang.String updateBy;
    
 	@Schema(description = "备注")
    private java.lang.String remark;
    
 	@Schema(description = "活动内容/描述")
    private java.lang.String activityContent;
    
 	@Schema(description = "已提交学生数")
    private java.lang.Integer submitStuCount;
    
 	@Schema(description = "班级总人数")
    private java.lang.Integer totalStuCount;
    
 	@Schema(description = "平均分")
    private java.math.BigDecimal avgScore;
    
	

}
