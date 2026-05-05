import { postRequest } from '/@/lib/axios';

export const departmentApi = {
  // 部门树形列表
  queryDeptTreeList: (tenantId) => {
    return postRequest('/manage/system/dept/loadDeptTreeList', { tenantId });
  },
  // 部门树
  queryDeptTree: (tenantId) => {
    return postRequest('/manage/system/dept/loadDeptTree', { tenantId });
  },
  // 详情
  getDetail: (id) => {
    return postRequest('/manage/system/dept/loadDept', { id });
  },
  // 添加
  addDepartment: (param) => {
    return postRequest('/manage/system/dept/createDept', param);
  },
  // 更新
  updateDepartment: (param) => {
    return postRequest('/manage/system/dept/updateDept', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/system/dept/setDeptStataus', { id, status });
  },
  // 添加用户到部门
  addUserToDept: (deptId, userIds) => {
    return postRequest('/manage/system/dept/addUserToDept', { deptId, userIds });
  },
  // 从部门移除用户
  removeUserFromDept: (deptId, userIds) => {
    return postRequest('/manage/system/dept/removeUserFromDept', { deptId, userIds });
  },
  // 删除
  deleteDepartment: (id) => {
      return postRequest(`/manage/system/dept/delete/${id}`);
  }
};
