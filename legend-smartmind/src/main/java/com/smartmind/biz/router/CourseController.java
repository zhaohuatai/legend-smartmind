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
import com.smartmind.biz.bo.dto.course.CoursePageQueryDto;
import com.smartmind.biz.bo.dto.course.CourseCreateDto;
import com.smartmind.biz.bo.dto.course.CourseUpdateDto;
import com.smartmind.biz.bo.model.Course;

import com.smartmind.biz.service.ICourseService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/course")
public class CourseController implements BaseController {

	
	@Autowired
	private ICourseService courseService;
	
	//@RequiresPermissions(value="course:loadDataSet",desc="课程信息列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody CoursePageQueryDto pageQueryDto){
    	 ChainWrapper<Course> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, Course.class);
	     DataSetDto<Course> dataSet=  courseService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="course:create",desc="课程信息添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody CourseCreateDto courseCreateDto){
		courseService.createCourse(courseCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="course:load",desc="课程信息加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<Course> ops = courseService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="course:update",desc="课程信息更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody CourseUpdateDto courseUpdateDto){
		courseService.updateCourse(courseUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="course:delete",desc="课程信息删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	courseService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="course:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		courseService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = courseService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    /**
     * 更新课程知识框架
     * 
     * @param request 包含id和knowledgeFramework的请求体
     * @return 操作结果
     */
    @RequestMapping(value="/updateKnowledgeFramework", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object updateKnowledgeFramework(@RequestBody UpdateKnowledgeFrameworkRequest request) {
    	 courseService.updateKnowledgeFramework(request.getId(), request.getKnowledgeFramework());
         return ajaxDoneSuccess("保存成功");
    }

    /**
     * 更新知识框架请求DTO
     */
    public static class UpdateKnowledgeFrameworkRequest {
        private Long id;
        private String knowledgeFramework;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getKnowledgeFramework() { return knowledgeFramework; }
        public void setKnowledgeFramework(String knowledgeFramework) { this.knowledgeFramework = knowledgeFramework; }
    }
    
}
