import { postRequest, getRequest, request } from '/@/lib/axios';

export const coursewareApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/courseware/queryPage', param);
  },

  // 查询全部
  queryAll: () => {
    return postRequest('/courseware/queryAll');
  },

  // 根据ID查询
  getDetail: (id) => {
    return getRequest(`/courseware/getDetail/${id}`);
  },

  // 添加
  add: (param) => {
    return postRequest('/courseware/add', param);
  },

  // 更新
  update: (param) => {
    return request({
      url: '/courseware/update',
      method: 'put',
      data: param,
    });
  },

  // 删除
  delete: (id) => {
    return request({
      url: `/courseware/delete/${id}`,
      method: 'delete',
    });
  },

  // 批量删除
  batchDelete: (idList) => {
    return request({
      url: '/courseware/batchDelete',
      method: 'delete',
      data: idList,
    });
  },

  // 根据单元编码模糊查询
  queryByUnitCodeLike: (unitCode) => {
    return getRequest(`/courseware/queryByUnitCodeLike/${unitCode}`);
  },

  // 根据课程ID查询
  queryByCourseId: (courseId) => {
    return getRequest(`/courseware/queryByCourseId/${courseId}`);
  },

  // 根据单元ID查询
  queryByUnitId: (unitId) => {
    return getRequest(`/courseware/queryByUnitId/${unitId}`);
  },
};
