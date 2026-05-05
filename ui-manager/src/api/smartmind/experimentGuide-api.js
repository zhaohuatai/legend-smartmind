import { postRequest, getRequest } from '/@/lib/axios';

export const experimentGuideApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/experimentGuide/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/experimentGuide/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/experimentGuide/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/experimentGuide/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/experimentGuide/setStatus', { id, status });
  },
  // 根据课程和单元编码列表查询
  loadByCourseAndUnitCodes: (param) => {
    return postRequest('/manage/smartmind/experimentGuide/loadByCourseAndUnitCodes', param);
  }
};
