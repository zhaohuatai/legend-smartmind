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
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuidePageQueryDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideCreateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideUpdateDto;
import com.smartmind.biz.bo.dto.experimentguide.ExperimentGuideContentUpdateDto;
import com.smartmind.biz.bo.model.ExperimentGuide;

import com.smartmind.biz.service.IExperimentGuideService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/experimentGuide")
public class ExperimentGuideController implements BaseController {

	
	@Autowired
	private IExperimentGuideService experimentGuideService;
	
	//@RequiresPermissions(value="experimentGuide:loadDataSet",desc="实验指导书列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ExperimentGuidePageQueryDto pageQueryDto){
    	 ChainWrapper<ExperimentGuide> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ExperimentGuide.class);
	     DataSetDto<ExperimentGuide> dataSet=  experimentGuideService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="experimentGuide:create",desc="实验指导书添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ExperimentGuideCreateDto experimentGuideCreateDto){
		Long id = experimentGuideService.createExperimentGuide(experimentGuideCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxSuccess("数据操作成功", id);
    }
	
	//@RequiresPermissions(value="experimentGuide:load",desc="实验指导书加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ExperimentGuide> ops = experimentGuideService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="experimentGuide:update",desc="实验指导书更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ExperimentGuideUpdateDto experimentGuideUpdateDto){
		experimentGuideService.updateExperimentGuide(experimentGuideUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="experimentGuide:delete",desc="实验指导书删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	experimentGuideService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="experimentGuide:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		experimentGuideService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = experimentGuideService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    @RequestMapping(value="/loadByCourseAndUnit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnit(@RequestParam Long courseId, @RequestParam(required = false) String unitCode){
		List<ExperimentGuide> list = experimentGuideService.loadByCourseIdAndUnitCode(courseId, unitCode);
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/loadByCourseAndUnitCodes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnitCodes(@RequestBody ExperimentGuidePageQueryDto queryDto){
		List<ExperimentGuide> list = experimentGuideService.loadByCourseIdAndUnitCodes(queryDto.getCourseId(), queryDto.getUnitCodes());
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/updateMainContent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object updateMainContent(@RequestBody ExperimentGuideContentUpdateDto dto){
		experimentGuideService.updateMainContent(dto);
		return ajaxDoneSuccess("保存成功");
    }
    
}
