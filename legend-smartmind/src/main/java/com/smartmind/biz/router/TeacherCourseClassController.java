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
import org.legend.framework.base.dao.mybatis.query.DtoToWrapperUtil;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.StatusListDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassPageQueryDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchCreateDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassBatchDto;
import com.smartmind.biz.bo.dto.teachercourseclass.TeacherCourseClassUpdateDto;
import com.smartmind.biz.bo.model.TeacherCourseClass;

import com.smartmind.biz.service.ITeacherCourseClassService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/teachercourseclass")
public class TeacherCourseClassController implements BaseController {

	
	@Autowired
	private ITeacherCourseClassService teacherCourseClassService;
	
	//@RequiresPermissions(value="teacherCourseClass:loadDataSet",desc="教师课程班级关联列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody TeacherCourseClassPageQueryDto pageQueryDto){
    	 ChainWrapper<TeacherCourseClass> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, TeacherCourseClass.class);
	     DataSetDto<TeacherCourseClass> dataSet=  teacherCourseClassService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="teacherCourseClass:create",desc="教师课程班级关联添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody TeacherCourseClassCreateDto teacherCourseClassCreateDto){
		teacherCourseClassService.createTeacherCourseClass(teacherCourseClassCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }

	//@RequiresPermissions(value="teacherCourseClass:create",desc="教师课程班级关联批量添加（根据课程自动关联班级）")
    @RequestMapping(value="/createByCourse", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object createByCourse(@RequestBody TeacherCourseClassBatchCreateDto batchCreateDto){
		teacherCourseClassService.createByCourse(batchCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }

	//@RequiresPermissions(value="teacherCourseClass:create",desc="教师课程班级关联批量添加")
    @RequestMapping(value="/createBatch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object createBatch(@RequestBody TeacherCourseClassBatchDto batchDto){
		teacherCourseClassService.createBatch(batchDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="teacherCourseClass:load",desc="教师课程班级关联加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<TeacherCourseClass> ops = teacherCourseClassService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="teacherCourseClass:update",desc="教师课程班级关联更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody TeacherCourseClassUpdateDto teacherCourseClassUpdateDto){
		teacherCourseClassService.updateTeacherCourseClass(teacherCourseClassUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="teacherCourseClass:delete",desc="教师课程班级关联删除")
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object delete(@PathVariable Long id){
		teacherCourseClassService.deleteById(id);
		return ajaxDoneSuccess("数据操作成功");
    }
    
    @RequiresPermissions(value="teacherCourseClass:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		teacherCourseClassService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = teacherCourseClassService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
