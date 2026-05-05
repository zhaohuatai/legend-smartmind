import { postRequest } from '/@/lib/axios';

export const examPaperQuestionApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/exampaperquestion/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/exampaperquestion/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/exampaperquestion/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/exampaperquestion/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/exampaperquestion/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/exampaperquestion/delete/${id}`);
  }
};

// 导入参考表API用于 SelectVO 查询
import { smdExamPaperApi } from '/@/api/smartmind/smdExamPaper-api.js';
import { smdQuestionBankApi } from '/@/api/smartmind/smdQuestionBank-api.js';
