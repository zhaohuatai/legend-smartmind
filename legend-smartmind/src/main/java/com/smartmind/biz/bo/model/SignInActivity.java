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
@Table(name="smd_sign_in_activity")
@TableFieldMode(FieldMode.camel_to_underscore)
public class SignInActivity extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "签到编码")
    private java.lang.String signCode;
    
 	@Schema(description = "签到名称")
    private java.lang.String signName;
    
 	@Schema(description = "签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到")
    private java.lang.String signType;
    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "班级名称")
    private java.lang.String className;
    
 	@Schema(description = "发布教师ID")
    private java.lang.String teacherId;
    
 	@Schema(description = "教师姓名")
    private java.lang.String teacherName;
    
 	@Schema(description = "开始时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date startTime;
    
 	@Schema(description = "结束时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date endTime;
    
 	@Schema(description = "签到时长(分钟)")
    private java.lang.Integer duration;
    
 	@Schema(description = "总人数")
    private java.lang.Integer totalStudents;
    
 	@Schema(description = "已签到人数")
    private java.lang.Integer signedCount;
    
 	@Schema(description = "迟到人数")
    private java.lang.Integer lateCount;
    
 	@Schema(description = "缺勤人数")
    private java.lang.Integer absentCount;
    
 	@Schema(description = "签到率(%)")
    private java.math.BigDecimal signRate;
    
 	@Schema(description = "签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
    private java.lang.String signStatus;
    
 	@Schema(description = "状态")
    private java.lang.String status;
    
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
    
 	@Schema(description = "签到配置(如手势密码、位置范围等)")
    private java.lang.String signConfig;
    
	

}
