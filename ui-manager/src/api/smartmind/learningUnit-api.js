import { postRequest } from '/@/lib/axios';

export const learningUnitApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/learningunit/loadDataSet', param);
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
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/learningunit/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/learningunit/delete/${id}`);
  },

  // 根据单元编码查询
  queryByUnitCode: (unitCode) => {
    return postRequest('/manage/smartmind/learningunit/queryByUnitCode', { unitCode });
  },

  // 根据课程ID查询单元列表
  queryByCourseId: (courseId) => {
    return postRequest('/manage/smartmind/learningunit/queryByCourseId', { courseId });
  }
};


