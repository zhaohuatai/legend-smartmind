package com.smartmind.biz.router;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanPageQueryDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanCreateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanUpdateDto;
import com.smartmind.biz.bo.dto.teachingplan.TeachingPlanContentUpdateDto;
import com.smartmind.biz.bo.model.TeachingPlan;

import com.smartmind.biz.service.ITeachingPlanService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/teachingplan")
public class TeachingPlanController implements BaseController {

	
	@Autowired
	private ITeachingPlanService teachingPlanService;
	
	//@RequiresPermissions(value="teachingPlan:loadDataSet",desc="教案列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody TeachingPlanPageQueryDto pageQueryDto){
    	 ChainWrapper<TeachingPlan> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, TeachingPlan.class);
	     DataSetDto<TeachingPlan> dataSet=  teachingPlanService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="teachingPlan:create",desc="教案添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody TeachingPlanCreateDto teachingPlanCreateDto){
		
    	Long id= teachingPlanService.createTeachingPlan(teachingPlanCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxSuccess("数据操作成功",id);
    }
	
	//@RequiresPermissions(value="teachingPlan:load",desc="教案加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<TeachingPlan> ops = teachingPlanService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="teachingPlan:update",desc="教案更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody TeachingPlanUpdateDto teachingPlanUpdateDto){
		teachingPlanService.updateTeachingPlan(teachingPlanUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="teachingPlan:delete",desc="教案删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	teachingPlanService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="teachingPlan:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		teachingPlanService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = teachingPlanService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    @RequestMapping(value="/loadByCourseAndUnit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnit(@RequestParam Long courseId, @RequestParam(required = false) String unitCode){
		List<TeachingPlan> list = teachingPlanService.loadByCourseIdAndUnitCode(courseId, unitCode);
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/updateMarkdownContent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object updateMarkdownContent(@RequestBody TeachingPlanContentUpdateDto dto){
		teachingPlanService.updateMarkdownContent(dto);
		return ajaxDoneSuccess("保存成功");
    }
    
}
