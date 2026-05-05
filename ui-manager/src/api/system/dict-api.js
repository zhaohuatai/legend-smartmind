import { postRequest } from '/@/lib/axios';

export const dictApi = {
  // --- 字典类型 ---
  // 分页查询字典类型
  queryTypePage: (param) => {
    return postRequest('/manage/system/dict/loadDictTypeDataSet', param);
  },
  // 获取字典类型详情
  getTypeDetail: (id) => {
    return postRequest(`/manage/system/dict/loadDicTypeById/${id}`);
  },
  // 添加字典类型
  addType: (param) => {
    return postRequest('/manage/system/dict/createDictType', param);
  },
  // 更新字典类型
  updateType: (param) => {
    return postRequest('/manage/system/dict/updateDictType', param);
  },
  // 更新字典类型状态
  updateTypeStatus: (id, status) => {
    return postRequest('/manage/system/dict/setDicTypeStatus', { id, status });
  },

  // --- 字典数据 ---
  // 分页查询字典数据
  queryDataPage: (param) => {
    return postRequest('/manage/system/dict/loadDicDataDataSet', param);
  },
  // 获取字典数据详情
  getDataDetail: (id) => {
    return postRequest(`/manage/system/dict/loadDictData/${id}`);
  },
  // 添加字典数据
  addData: (param) => {
    return postRequest('/manage/system/dict/createDictData', param);
  },
  // 更新字典数据
  updateData: (param) => {
    return postRequest('/manage/system/dict/updateDictData', param);
  },
  // 更新字典数据状态
  updateDataStatus: (id, status) => {
    return postRequest('/manage/system/dict/setDicDataStatus', { id, status });
  }
};
