import { postRequest } from '/@/lib/axios';

export const signInRecordApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/signinrecord/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/signinrecord/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/signinrecord/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/signinrecord/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/signinrecord/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/signinrecord/delete/${id}`);
  }
};
