import { postRequest } from '/@/lib/axios';

export const examPaperApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/exampaper/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/exampaper/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/exampaper/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/exampaper/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/exampaper/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/exampaper/delete/${id}`);
  }
};
