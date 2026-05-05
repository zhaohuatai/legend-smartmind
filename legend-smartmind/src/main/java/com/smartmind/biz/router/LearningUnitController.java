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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.legend.framework.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.base.dao.mybatis.query.DtoToWrapperUtil;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.StatusListDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitPageQueryDto;
import com.smartmind.biz.bo.dto.learningunit.GenerateFromFrameworkRequest;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitCreateDto;
import com.smartmind.biz.bo.dto.learningunit.LearningUnitUpdateDto;
import com.smartmind.biz.bo.model.LearningUnit;

import com.smartmind.biz.service.ILearningUnitService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/learningunit")
public class LearningUnitController implements BaseController {

	
	@Autowired
	private ILearningUnitService learningUnitService;
	
	//@RequiresPermissions(value="learningUnit:loadDataSet",desc="学习单元列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody LearningUnitPageQueryDto pageQueryDto){
    	 ChainWrapper<LearningUnit> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, LearningUnit.class);
	     DataSetDto<LearningUnit> dataSet=  learningUnitService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="learningUnit:create",desc="学习单元添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody LearningUnitCreateDto learningUnitCreateDto){
		learningUnitService.createLearningUnit(learningUnitCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="learningUnit:load",desc="学习单元加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<LearningUnit> ops = learningUnitService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="learningUnit:update",desc="学习单元更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody LearningUnitUpdateDto learningUnitUpdateDto){
		learningUnitService.updateLearningUnit(learningUnitUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	@RequiresPermissions(value="learningUnit:delete",desc="学习单元删除")
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object delete(@PathVariable Long id){
		learningUnitService.deleteLearningUnit(id);
		return ajaxDoneSuccess("删除成功");
    }
    
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		learningUnitService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = learningUnitService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
    /**
     * 根据课程ID查询章节列表
     */
    @RequestMapping(value="/queryByCourseId/{courseId}", method = {RequestMethod.GET,RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object queryByCourseId(@PathVariable Long courseId) {
        List<LearningUnit> list = learningUnitService.queryByCourseId(courseId);
        return ajaxQuery(list);
    }

    /**
     * 获取课程知识结构（用于生成学习单元）
     */
    @RequestMapping(value="/queryKnowledgeFramework/{courseId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object queryKnowledgeFramework(@PathVariable Long courseId) {
            String markdown = learningUnitService.queryKnowledgeFramework(courseId);
            return ajaxQuery(markdown);
     
    }

    /**
     * 从知识结构生成学习单元
     */
    @RequestMapping(value="/createFromFramework", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createFromFramework(@RequestBody GenerateFromFrameworkRequest request) {
            Integer level = request.getLevel();
            if (level == null) {
                level = 3; // 默认三层
            }
            learningUnitService.createLearningUnitsFromMarkdown(request.getCourseId(), request.getMarkdownText(), level);
            return ajaxDoneSuccess("学习单元生成成功");
    }

   

    /**
     * 根据单元编码加载单元信息
     */
    @RequestMapping(value="/loadByUnitCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByUnitCode(@RequestParam String unitCode) {
        Optional<LearningUnit> ops = learningUnitService.loadByUniqueColumn("unit_code", unitCode);
        return ajaxQueryResult(ops);
    }

}
