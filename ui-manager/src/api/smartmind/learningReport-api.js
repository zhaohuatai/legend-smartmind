import { postRequest } from '/@/lib/axios';

export const learningReportApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/learningreport/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/learningreport/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/learningreport/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/learningreport/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/learningreport/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/learningreport/delete/${id}`);
  }
};
