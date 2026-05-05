import { postRequest } from '/@/lib/axios';

export const signInActivityApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/signinactivity/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/signinactivity/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/signinactivity/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/signinactivity/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/signinactivity/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/signinactivity/delete/${id}`);
  }
};

// 导入参考表API用于 SelectVO 查询
import { courseApi } from '/@/api/smartmind/course-api.js';
import { clazzApi } from '/@/api/smartmind/clazz-api.js';
import { teacherApi } from '/@/api/smartmind/teacher-api.js';
