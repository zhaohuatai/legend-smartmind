import { postRequest } from '/@/lib/axios';

export const classCourseApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classcourse/loadDataSet', param);
  },

  // 创建
  create: (param) => {
    return postRequest('/manage/smartmind/classcourse/create', param);
  },

  // 根据ID查询
  queryById: (id) => {
    return postRequest(`/manage/smartmind/classcourse/loadById/${id}`);
  },

  // 更新
  update: (param) => {
    return postRequest('/manage/smartmind/classcourse/update', param);
  },

  // 修改状态
  setStatus: (param) => {
    return postRequest('/manage/smartmind/classcourse/setStatus', param);
  },

  // 查询课程关联的班级ID列表
  queryClassIds: (courseId) => {
    return postRequest(`/manage/smartmind/classcourse/queryClassIds/${courseId}`);
  },

  // 查询课程关联的班级完整信息
  queryCourseClass: (courseId) => {
    return postRequest(`/manage/smartmind/classcourse/queryCourseClass/${courseId}`);
  },

  // 保存课程班级关联
  saveRelation: (param) => {
    return postRequest('/manage/smartmind/classcourse/saveRelation', param);
  },
};
