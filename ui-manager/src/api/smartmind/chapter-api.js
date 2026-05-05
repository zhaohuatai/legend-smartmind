import { postRequest, getRequest } from '/@/lib/axios';

export const chapterApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/chapter/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/chapter/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/chapter/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/chapter/update', param);
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/chapter/delete/${id}`);
  },
  // 根据课程ID查询章节列表
  queryByCourseId: (courseId) => {
    return getRequest(`/manage/smartmind/learningunit/queryByCourseId/${courseId}`);
  },
};
