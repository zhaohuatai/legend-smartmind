import { postRequest } from '/@/lib/axios';

export const classCourseApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classcourse/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classcourse/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classcourse/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classcourse/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classcourse/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classcourse/delete/${id}`);
  }
};


