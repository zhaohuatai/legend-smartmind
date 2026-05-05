import { postRequest } from '/@/lib/axios';

export const classActivityQuestionApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classactivityquestion/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classactivityquestion/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classactivityquestion/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classactivityquestion/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivityquestion/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classactivityquestion/delete/${id}`);
  }
};

// 导入参考表API用于 SelectVO 查询
import { smdClassActivityApi } from '/@/api/smartmind/smdClassActivity-api.js';
import { smdQuestionCategoryApi } from '/@/api/smartmind/smdQuestionCategory-api.js';
