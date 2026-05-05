import { postRequest } from '/@/lib/axios';

export const smdQuestionBankApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/question/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/question/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/question/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/question/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/question/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/question/delete/${id}`);
  },
  // 查询SelectVo列表
  selectVo: (keywords) => {
    return postRequest('/manage/smartmind/question/loadSelectVo', { keywords });
  },
};
