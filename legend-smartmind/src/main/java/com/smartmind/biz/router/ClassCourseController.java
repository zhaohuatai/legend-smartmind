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
import com.smartmind.biz.bo.dto.classcourse.ClassCoursePageQueryDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseCreateDto;
import com.smartmind.biz.bo.dto.classcourse.ClassCourseUpdateDto;
import com.smartmind.biz.bo.dto.classcourse.CourseClassRelationDto;
import com.smartmind.biz.bo.model.ClassCourse;

import com.smartmind.biz.service.IClassCourseService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/classcourse")
public class ClassCourseController implements BaseController {

	
	@Autowired
	private IClassCourseService classCourseService;
	
	//@RequiresPermissions(value="classCourse:loadDataSet",desc="班级课程关联列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ClassCoursePageQueryDto pageQueryDto){
    	 ChainWrapper<ClassCourse> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ClassCourse.class);
	     DataSetDto<ClassCourse> dataSet=  classCourseService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="classCourse:create",desc="班级课程关联添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ClassCourseCreateDto classCourseCreateDto){
		classCourseService.createClassCourse(classCourseCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classCourse:load",desc="班级课程关联加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ClassCourse> ops = classCourseService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="classCourse:update",desc="班级课程关联更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ClassCourseUpdateDto classCourseUpdateDto){
		classCourseService.updateClassCourse(classCourseUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classCourse:delete",desc="班级课程关联删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	classCourseService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="classCourse:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		classCourseService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = classCourseService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}

    /**
     * 查询课程关联的班级ID列表
     */
    @RequestMapping(value="/queryClassIds/{courseId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object queryClassIds(@PathVariable Long courseId){
        List<Long> classIds = classCourseService.queryClassIdsByCourseId(courseId);
        return ajaxQueryResult(classIds);
    }
    
    @RequestMapping(value="/queryCourseClass/{courseId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object queryCourseClassByCourseId(@PathVariable Long courseId){
        List<ClassCourse> classIds = classCourseService.queryCourseClassByCourseId(courseId);
        return ajaxQueryResult(classIds);
    }

    /**
     * 保存课程班级关联
     */
    @RequestMapping(value="/saveRelation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object saveRelation(@RequestBody CourseClassRelationDto dto){
        classCourseService.saveCourseClassRelation(dto.getCourseId(), dto.getClassIds());
        return ajaxDoneSuccess("保存成功");
    }

  
}
