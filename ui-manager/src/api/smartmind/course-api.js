import { postRequest } from '/@/lib/axios';

export const courseApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/course/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/course/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/course/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/course/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/course/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/course/delete/${id}`);
  },
  // 更新知识框架
  updateKnowledgeFramework: (param) => {
    return postRequest('/manage/smartmind/course/updateKnowledgeFramework', param);
  }
};

