import { postRequest } from '/@/lib/axios';

export const classActivityAnswerApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classactivityanswer/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classactivityanswer/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classactivityanswer/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classactivityanswer/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivityanswer/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classactivityanswer/delete/${id}`);
  }
};

// 导入参考表API用于 SelectVO 查询
import { smdClassActivitySubmitApi } from '/@/api/smartmind/smdClassActivitySubmit-api.js';
import { smdClassActivityApi } from '/@/api/smartmind/smdClassActivity-api.js';
import { smdQuestionCategoryApi } from '/@/api/smartmind/smdQuestionCategory-api.js';
import { smdStudentApi } from '/@/api/smartmind/smdStudent-api.js';
