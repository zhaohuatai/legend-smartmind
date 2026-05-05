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
@Table(name="smd_sign_in_record")
@TableFieldMode(FieldMode.camel_to_underscore)
public class SignInRecord extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "签到活动ID")
    private java.lang.Long signId;
    
 	@Schema(description = "签到名称")
    private java.lang.String signName;
    
 	@Schema(description = "学生ID")
    private java.lang.String studentId;
    
 	@Schema(description = "学号")
    private java.lang.String studentNo;
    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "班级名称")
    private java.lang.String className;
    
 	@Schema(description = "签到时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date signTime;
    
 	@Schema(description = "签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假")
    private java.lang.String signResult;
    
 	@Schema(description = "签到方式: 1-点击签到, 2-位置签到, 3-手势签到, 4-扫码签到")
    private java.lang.String signMethod;
    
 	@Schema(description = "签到位置")
    private java.lang.String location;
    
 	@Schema(description = "纬度")
    private java.math.BigDecimal latitude;
    
 	@Schema(description = "经度")
    private java.math.BigDecimal longitude;
    
 	@Schema(description = "IP地址")
    private java.lang.String ipAddress;
    
 	@Schema(description = "设备类型")
    private java.lang.String deviceType;
    
 	@Schema(description = "是否早退: 0-否, 1-是")
    private java.lang.String leaveEarly;
    
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
    
	

}
