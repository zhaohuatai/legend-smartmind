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
import com.smartmind.biz.bo.dto.question.QuestionPageQueryDto;
import com.smartmind.biz.bo.dto.question.QuestionCreateDto;
import com.smartmind.biz.bo.dto.question.QuestionUpdateDto;
import com.smartmind.biz.bo.dto.question.QuestionBatchCreateDto;
import com.smartmind.biz.bo.model.Question;

import com.smartmind.biz.service.IQuestionService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/question")
public class QuestionController implements BaseController {

	
	@Autowired
	private IQuestionService questionService;
	
	//@RequiresPermissions(value="question:loadDataSet",desc="题目列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody QuestionPageQueryDto pageQueryDto){
    	 ChainWrapper<Question> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, Question.class);
	     DataSetDto<Question> dataSet=  questionService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="question:create",desc="题目添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody QuestionCreateDto questionCreateDto){
		Long id = questionService.createQuestion(questionCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxSuccess("数据操作成功", id);
    }
	
	//@RequiresPermissions(value="question:load",desc="题目加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<Question> ops = questionService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="question:update",desc="题目更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody QuestionUpdateDto questionUpdateDto){
		questionService.updateQuestion(questionUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="question:delete",desc="题目删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	questionService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="question:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		questionService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    
    @RequestMapping(value="/loadByCourseAndUnit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadByCourseAndUnit(@RequestBody QuestionPageQueryDto queryDto){
		List<Question> list = questionService.loadByCourseAndUnit(queryDto.getCourseId(), queryDto.getUnitCode());
		return ajaxQueryResult(list);
    }

    @RequestMapping(value="/loadByCourseAndUnitCodes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadByCourseAndUnitCodes(@RequestBody QuestionPageQueryDto queryDto){
		List<Question> list = questionService.loadByCourseAndUnitCodes(queryDto.getCourseId(), queryDto.getUnitCodes());
		return ajaxQueryResult(list);
    }

    @RequestMapping(value="/batchCreate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object batchCreate(@RequestBody QuestionBatchCreateDto batchCreateDto){
		int count = questionService.batchCreateQuestions(batchCreateDto.getQuestions(), SecurityUtil.getSimpleUser());
		return ajaxSuccess("批量创建成功，共创建" + count + "道题目", count);
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = questionService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
