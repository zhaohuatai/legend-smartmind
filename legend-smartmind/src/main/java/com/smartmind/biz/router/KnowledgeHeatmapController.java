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
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapPageQueryDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapCreateDto;
import com.smartmind.biz.bo.dto.knowledgeheatmap.KnowledgeHeatmapUpdateDto;
import com.smartmind.biz.bo.model.KnowledgeHeatmap;

import com.smartmind.biz.service.IKnowledgeHeatmapService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/knowledgeheatmap")
public class KnowledgeHeatmapController implements BaseController {

	
	@Autowired
	private IKnowledgeHeatmapService knowledgeHeatmapService;
	
	//@RequiresPermissions(value="knowledgeHeatmap:loadDataSet",desc="知识点热力图列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody KnowledgeHeatmapPageQueryDto pageQueryDto){
    	 ChainWrapper<KnowledgeHeatmap> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, KnowledgeHeatmap.class);
	     DataSetDto<KnowledgeHeatmap> dataSet=  knowledgeHeatmapService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="knowledgeHeatmap:create",desc="知识点热力图添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody KnowledgeHeatmapCreateDto knowledgeHeatmapCreateDto){
		knowledgeHeatmapService.createKnowledgeHeatmap(knowledgeHeatmapCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="knowledgeHeatmap:load",desc="知识点热力图加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<KnowledgeHeatmap> ops = knowledgeHeatmapService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="knowledgeHeatmap:update",desc="知识点热力图更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody KnowledgeHeatmapUpdateDto knowledgeHeatmapUpdateDto){
		knowledgeHeatmapService.updateKnowledgeHeatmap(knowledgeHeatmapUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="knowledgeHeatmap:delete",desc="知识点热力图删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	knowledgeHeatmapService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="knowledgeHeatmap:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		knowledgeHeatmapService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = knowledgeHeatmapService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
