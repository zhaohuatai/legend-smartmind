import { postRequest } from '/@/lib/axios';

export const teacherApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/teacher/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/teacher/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/teacher/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/teacher/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/teacher/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/teacher/delete/${id}`);
  }
};

