package com.smartmind.biz.router;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.legend.framework.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.StatusListDto;
import com.smartmind.biz.bo.dto.student.StudentPageQueryDto;
import com.smartmind.biz.bo.dto.student.StudentCreateDto;
import com.smartmind.biz.bo.dto.student.StudentUpdateDto;
import com.smartmind.biz.bo.model.Student;
import com.smartmind.biz.bo.vo.StudentListVo;

import com.smartmind.biz.service.IStudentService;
import com.smartmind.biz.service.IClazzService;
import com.smartmind.biz.dao.StudentMapper;
import com.smartmind.biz.dao.StudentListVoMapper;
import com.smartmind.ai.service.StudentParseService;
import com.smartmind.biz.bo.dto.student.StudentParseTextDto;
import com.smartmind.biz.bo.vo.StudentParseResultVo;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.core.util.ZStrUtil;
@Controller 
@RequestMapping("/manage/smartmind/student")
public class StudentController implements BaseController {

	
	@Autowired
	private IStudentService studentService;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private StudentListVoMapper studentListVoMapper;
	
	@Autowired
	private StudentParseService studentParseService;
	
	@Autowired
	private IClazzService clazzService;

	
	//@RequiresPermissions(value="student:loadDataSet",desc="学生信息列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody StudentPageQueryDto pageQueryDto){
    	 // 使用 ChainWrapper 进行关联查询（参照示例写法）
    	 ChainWrapper<StudentListVo> qw = ChainWrapper.create(StudentListVo.class)
    	 	.select(
    	 		"s.id AS id",
    	 		"s.student_no AS studentNo",
    	 		"s.student_name AS studentName",
    	 		"s.gender AS gender",
    	 		"s.phone AS phone",
    	 		"s.status AS status",
    	 		"s.create_time AS createTime",
    	 		"c.id AS classId",
    	 		"c.class_name AS className",
    	 		"c.class_code AS classCode"
    	 	)
    	 	.from("smd_student AS s")
    	 	.leftJoin("smd_class_student AS cs", "s.id = cs.student_id")
    	 	.leftJoin("smd_clazz AS c", "cs.class_id = c.id");
    	 
    	 // 添加查询条件
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getStudentName())) {
    	 	 qw.where("s.student_name", "LIKE", "%" + pageQueryDto.getStudentName() + "%");
    	 }
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getStudentNo())) {
    	 	 qw.where("s.student_no", "LIKE", "%" + pageQueryDto.getStudentNo() + "%");
    	 }
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getPhone())) {
    	 	 qw.where("s.phone", "LIKE", "%" + pageQueryDto.getPhone() + "%");
    	 }
    	 if (pageQueryDto.getClassId() != null) {
    	 	 qw.where("c.id", "=", pageQueryDto.getClassId());
    	 }
    	 
    	 // 排序和分页
    	 qw.orderBy("s.create_time", false);
    	 qw.limit(pageQueryDto.getPageSize());
    	 qw.offset((pageQueryDto.getPageNum() - 1) * pageQueryDto.getPageSize());
    	 
    	 List<StudentListVo> list = studentListVoMapper.selectByQuery(qw);
    	 
    	 // 查询总数
    	 ChainWrapper<StudentListVo> countQw = ChainWrapper.create(StudentListVo.class)
    	 	.from("smd_student AS s")
    	 	.leftJoin("smd_class_student AS cs", "s.id = cs.student_id")
    	 	.leftJoin("smd_clazz AS c", "cs.class_id = c.id");
    	 
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getStudentName())) {
    	 	 countQw.where("s.student_name", "LIKE", "%" + pageQueryDto.getStudentName() + "%");
    	 }
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getStudentNo())) {
    	 	 countQw.where("s.student_no", "LIKE", "%" + pageQueryDto.getStudentNo() + "%");
    	 }
    	 if (ZStrUtil.isNotEmpty(pageQueryDto.getPhone())) {
    	 	 countQw.where("s.phone", "LIKE", "%" + pageQueryDto.getPhone() + "%");
    	 }
    	 if (pageQueryDto.getClassId() != null) {
    	 	 countQw.where("c.id", "=", pageQueryDto.getClassId());
    	 }
    	 
    	 Long total = studentListVoMapper.countByQuery(countQw);
    	 
    	 DataSetDto<StudentListVo> dataSet =DataSetDto.newDS(total, list);
    	 
	     return ajaxQueryResult(dataSet);
    }
	
	//@RequiresPermissions(value="student:create",desc="学生信息添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody StudentCreateDto studentCreateDto){
		studentService.createStudent(studentCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="student:load",desc="学生信息加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable String id){
		Optional<Student> ops = studentService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="student:update",desc="学生信息更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody StudentUpdateDto studentUpdateDto){
		studentService.updateStudent(studentUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
    @RequiresPermissions(value="student:load",desc="学生信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		studentService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
	
	/**
	 * AI解析文本中的学生信息
	 */
	@RequestMapping(value="/parseText", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object parseText(@RequestBody StudentParseTextDto parseTextDto){
		ZBeanUtil.validateBean(parseTextDto);
		
		// 获取所有班级用于AI匹配
		List<com.smartmind.biz.bo.model.Clazz> allClasses = clazzService.loadAll(null);
		
		// 调用AI服务解析文本（传入班级列表用于智能匹配）
		List<StudentParseResultVo> result = studentParseService.parseStudentInfo(parseTextDto.getText(), allClasses);
		
		return ajaxQueryResult(result);
    }
	
	/**
	 * 批量导入学生信息
	 */
	@RequestMapping(value="/batchImport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object batchImport(@RequestBody List<StudentCreateDto> students){
		if (students == null || students.isEmpty()) {
			return ajaxError("学生列表不能为空");
		}
		
		int size = studentService.batchCreate(students);
		
		return ajaxDoneSuccess("成功导入 " + size + " 名学生");
    }
    
}
