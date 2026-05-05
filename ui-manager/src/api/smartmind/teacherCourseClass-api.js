import { postRequest } from '/@/lib/axios';

export const teacherCourseClassApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/teachercourseclass/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/teachercourseclass/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/teachercourseclass/create', param);
  },
  // 根据课程添加关联（自动关联班级）
  addByCourse: (param) => {
    return postRequest('/manage/smartmind/teachercourseclass/createByCourse', param);
  },
  // 批量添加关联
  addBatch: (param) => {
    return postRequest('/manage/smartmind/teachercourseclass/createBatch', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/teachercourseclass/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/teachercourseclass/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/teachercourseclass/delete/${id}`);
  }
};
