package com.smartmind.biz.dao;
import java.util.List;
import com.smartmind.biz.bo.model.ExamPaperQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.legend.framework.core.consts.DaoConst;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionPageQueryDto;
@Mapper
public interface ExamPaperQuestionMapper extends BaseMapper<ExamPaperQuestion>{
	
}
