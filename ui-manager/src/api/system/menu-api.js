import { postRequest, getRequest } from '/@/lib/axios';

export const menuApi = {
  // 菜单树
  queryMenuTree: (tenantId) => {
    return postRequest('/manage/system/authMenu/loadAuthMenuTree', { tenantId });
  },
  // 菜单列表
  queryMenuList: (tenantId) => {
    return postRequest('/manage/system/authMenu/loadAuthMenuList', { tenantId });
  },
  // 添加菜单
  addMenu: (param) => {
    return postRequest('/manage/system/authMenu/createMenu', param);
  },
  // 更新菜单
  updateMenu: (param) => {
    return postRequest('/manage/system/authMenu/updateMenu', param);
  },
  // 获取详情
  getDetail: (id) => {
    return postRequest(`/manage/system/authMenu/loadMenu/${id}`);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/system/authMenu/setMenuStatus', { id, status });
  },
  // 批量删除
  batchDeleteMenu: (idList) => {
      return postRequest('/manage/system/authMenu/batchDelete', { idList });
  },
  // 删除
  deleteMenu: (id) => {
      return postRequest(`/manage/system/authMenu/delete/${id}`);
  }
};
