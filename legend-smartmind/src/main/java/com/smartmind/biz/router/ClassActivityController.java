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
import com.smartmind.biz.bo.dto.classactivity.ClassActivityPageQueryDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityCreateDto;
import com.smartmind.biz.bo.dto.classactivity.ClassActivityUpdateDto;
import com.smartmind.biz.bo.model.ClassActivity;

import com.smartmind.biz.service.IClassActivityService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/classactivity")
public class ClassActivityController implements BaseController {

	
	@Autowired
	private IClassActivityService classActivityService;
	
	//@RequiresPermissions(value="classActivity:loadDataSet",desc="课堂活动列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ClassActivityPageQueryDto pageQueryDto){
    	 ChainWrapper<ClassActivity> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ClassActivity.class);
	     DataSetDto<ClassActivity> dataSet=  classActivityService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="classActivity:create",desc="课堂活动添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ClassActivityCreateDto classActivityCreateDto){
		classActivityService.createClassActivity(classActivityCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivity:load",desc="课堂活动加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ClassActivity> ops = classActivityService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="classActivity:update",desc="课堂活动更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ClassActivityUpdateDto classActivityUpdateDto){
		classActivityService.updateClassActivity(classActivityUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivity:delete",desc="课堂活动删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	classActivityService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		classActivityService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = classActivityService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    @RequestMapping(value="/queryBySessionId/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object queryBySessionId(@PathVariable Long sessionId){
		List<ClassActivity> list = classActivityService.queryBySessionId(sessionId);
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/loadForUpdateById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadForUpdateById(@PathVariable Long id){
    	ClassActivityUpdateDto dto = classActivityService.loadforUpdateById(id);
		return ajaxQuery(dto);
    }
    
    @RequestMapping(value="/startActivity/{activityId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object startActivity(@PathVariable Long activityId){
		classActivityService.startActivity(activityId, SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("活动已开始");
    }
    
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object delete(@PathVariable Long id){
		classActivityService.deleteActivity(id);
		return ajaxDoneSuccess("删除成功");
    }
    
}
