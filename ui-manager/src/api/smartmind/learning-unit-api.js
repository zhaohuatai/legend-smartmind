import { postRequest, getRequest } from '/@/lib/axios';

export const learningUnitApi = {
  // 根据课程ID查询章节列表 (GET + Path参数)
  queryByCourseId: (courseId) => {
    return getRequest(`/manage/smartmind/learningunit/queryByCourseId/${courseId}`);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/learningunit/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/learningunit/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/learningunit/update', param);
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/learningunit/delete/${id}`);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/learningunit/setStatus', { id, status });
  },
  // 获取课程知识结构（用于生成学习单元）
  queryKnowledgeFramework: (courseId) => {
    return getRequest(`/manage/smartmind/learningunit/queryKnowledgeFramework/${courseId}`);
  },
  // 从知识结构生成学习单元
  createFromFramework: (courseId, markdownText, level) => {
    return postRequest('/manage/smartmind/learningunit/createFromFramework', { courseId, markdownText, level });
  },
};
