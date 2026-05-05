import { postRequest } from '/@/lib/axios';

export const studentApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/student/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/student/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/student/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/student/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/student/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/student/delete/${id}`);
  },
  // AI解析文本
  parseText: (param) => {
    return postRequest('/manage/smartmind/student/parseText', param);
  },
  // 批量导入
  batchImport: (param) => {
    return postRequest('/manage/smartmind/student/batchImport', param);
  },
};
