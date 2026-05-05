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
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRulePageQueryDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleCreateDto;
import com.smartmind.biz.bo.dto.templatequestionrule.TemplateQuestionRuleUpdateDto;
import com.smartmind.biz.bo.model.TemplateQuestionRule;

import com.smartmind.biz.service.ITemplateQuestionRuleService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/templatequestionrule")
public class TemplateQuestionRuleController implements BaseController {

	
	@Autowired
	private ITemplateQuestionRuleService templateQuestionRuleService;
	
	//@RequiresPermissions(value="templateQuestionRule:loadDataSet",desc="模板选题规则列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody TemplateQuestionRulePageQueryDto pageQueryDto){
    	 ChainWrapper<TemplateQuestionRule> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, TemplateQuestionRule.class);
	     DataSetDto<TemplateQuestionRule> dataSet=  templateQuestionRuleService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="templateQuestionRule:create",desc="模板选题规则添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody TemplateQuestionRuleCreateDto templateQuestionRuleCreateDto){
		templateQuestionRuleService.createTemplateQuestionRule(templateQuestionRuleCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="templateQuestionRule:load",desc="模板选题规则加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<TemplateQuestionRule> ops = templateQuestionRuleService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="templateQuestionRule:update",desc="模板选题规则更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody TemplateQuestionRuleUpdateDto templateQuestionRuleUpdateDto){
		templateQuestionRuleService.updateTemplateQuestionRule(templateQuestionRuleUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="templateQuestionRule:delete",desc="模板选题规则删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	templateQuestionRuleService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="templateQuestionRule:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		templateQuestionRuleService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = templateQuestionRuleService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
