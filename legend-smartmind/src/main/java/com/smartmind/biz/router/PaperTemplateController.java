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
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplatePageQueryDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateCreateDto;
import com.smartmind.biz.bo.dto.papertemplate.PaperTemplateUpdateDto;
import com.smartmind.biz.bo.model.PaperTemplate;

import com.smartmind.biz.service.IPaperTemplateService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/papertemplate")
public class PaperTemplateController implements BaseController {

	
	@Autowired
	private IPaperTemplateService paperTemplateService;
	
	//@RequiresPermissions(value="paperTemplate:loadDataSet",desc="试卷模板列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody PaperTemplatePageQueryDto pageQueryDto){
    	 ChainWrapper<PaperTemplate> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, PaperTemplate.class);
	     DataSetDto<PaperTemplate> dataSet=  paperTemplateService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="paperTemplate:create",desc="试卷模板添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody PaperTemplateCreateDto paperTemplateCreateDto){
		paperTemplateService.createPaperTemplate(paperTemplateCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="paperTemplate:load",desc="试卷模板加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<PaperTemplate> ops = paperTemplateService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="paperTemplate:update",desc="试卷模板更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody PaperTemplateUpdateDto paperTemplateUpdateDto){
		paperTemplateService.updatePaperTemplate(paperTemplateUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="paperTemplate:delete",desc="试卷模板删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	paperTemplateService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="paperTemplate:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		paperTemplateService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = paperTemplateService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
