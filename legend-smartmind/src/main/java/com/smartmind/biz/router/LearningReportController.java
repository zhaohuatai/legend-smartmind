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
import com.smartmind.biz.bo.dto.learningreport.LearningReportPageQueryDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportCreateDto;
import com.smartmind.biz.bo.dto.learningreport.LearningReportUpdateDto;
import com.smartmind.biz.bo.model.LearningReport;

import com.smartmind.biz.service.ILearningReportService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/learningreport")
public class LearningReportController implements BaseController {

	
	@Autowired
	private ILearningReportService learningReportService;
	
	//@RequiresPermissions(value="learningReport:loadDataSet",desc="学情报告列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody LearningReportPageQueryDto pageQueryDto){
    	 ChainWrapper<LearningReport> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, LearningReport.class);
	     DataSetDto<LearningReport> dataSet=  learningReportService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="learningReport:create",desc="学情报告添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody LearningReportCreateDto learningReportCreateDto){
		learningReportService.createLearningReport(learningReportCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="learningReport:load",desc="学情报告加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<LearningReport> ops = learningReportService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="learningReport:update",desc="学情报告更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody LearningReportUpdateDto learningReportUpdateDto){
		learningReportService.updateLearningReport(learningReportUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="learningReport:delete",desc="学情报告删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	learningReportService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="learningReport:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		learningReportService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = learningReportService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
