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
import com.smartmind.biz.bo.dto.courseware.CoursewarePageQueryDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareCreateDto;
import com.smartmind.biz.bo.dto.courseware.CoursewareUpdateDto;
import com.smartmind.biz.bo.model.Courseware;

import com.smartmind.biz.service.ICoursewareService;
import org.legend.framework.core.data.SelectVo;
import org.springframework.web.bind.annotation.RequestParam;
@Controller 
@RequestMapping("/manage/smartmind/courseware")
public class CoursewareController implements BaseController {

	
	@Autowired
	private ICoursewareService coursewareService;
	
	//@RequiresPermissions(value="courseware:loadDataSet",desc="课件管理列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody CoursewarePageQueryDto pageQueryDto){
    	 ChainWrapper<Courseware> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, Courseware.class);
	     DataSetDto<Courseware> dataSet=  coursewareService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="courseware:create",desc="课件管理添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody CoursewareCreateDto coursewareCreateDto){
		coursewareService.createCourseware(coursewareCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="courseware:load",desc="课件管理加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<Courseware> ops = coursewareService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="courseware:update",desc="课件管理更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody CoursewareUpdateDto coursewareUpdateDto){
		coursewareService.updateCourseware(coursewareUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="courseware:delete",desc="课件管理删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	coursewareService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="courseware:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		coursewareService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = coursewareService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}

    @RequestMapping(value="/loadByCourseAndUnit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnit(@RequestParam Long courseId, @RequestParam(required = false) String unitCode){
		List<Courseware> list = coursewareService.loadByCourseIdAndUnitCode(courseId, unitCode);
		return ajaxQuery(list);
    }

    @RequestMapping(value="/batchSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object batchSave(@RequestBody List<Courseware> coursewareList){
		coursewareService.batchSave(coursewareList, SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("保存成功");
    }

}
