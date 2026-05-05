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
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourcePageQueryDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceUpdateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceBatchSaveDto;
import com.smartmind.biz.bo.model.ClassActivityResource;

import com.smartmind.biz.service.IClassActivityResourceService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/classactivityresource")
public class ClassActivityResourceController implements BaseController {

	
	@Autowired
	private IClassActivityResourceService classActivityResourceService;
	
	//@RequiresPermissions(value="classActivityResource:loadDataSet",desc="课堂活动资源关联列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ClassActivityResourcePageQueryDto pageQueryDto){
    	 ChainWrapper<ClassActivityResource> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ClassActivityResource.class);
	     DataSetDto<ClassActivityResource> dataSet=  classActivityResourceService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="classActivityResource:create",desc="课堂活动资源关联添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ClassActivityResourceCreateDto classActivityResourceCreateDto){
		classActivityResourceService.createClassActivityResource(classActivityResourceCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivityResource:load",desc="课堂活动资源关联加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ClassActivityResource> ops = classActivityResourceService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="classActivityResource:update",desc="课堂活动资源关联更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ClassActivityResourceUpdateDto classActivityResourceUpdateDto){
		classActivityResourceService.updateClassActivityResource(classActivityResourceUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivityResource:delete",desc="课堂活动资源关联删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	classActivityResourceService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="classActivityResource:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		classActivityResourceService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = classActivityResourceService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    @RequestMapping(value="/batchSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object batchSave(@RequestBody ClassActivityResourceBatchSaveDto batchSaveDto){
		int count = classActivityResourceService.batchSaveResources(batchSaveDto, SecurityUtil.getSimpleUser());
		return ajaxSuccess("保存成功，共保存" + count + "条资源", count);
    }
    
    @RequestMapping(value="/loadByActivityId/{activityId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByActivityId(@PathVariable Long activityId){
		List<ClassActivityResource> list = classActivityResourceService.loadByActivityId(activityId);
		return ajaxQuery(list);
    }
    
}
