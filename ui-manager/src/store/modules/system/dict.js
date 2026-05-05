import { defineStore } from 'pinia';
import { DICT_SPLIT } from '/@/constants/support/dict-const.js';
import _ from 'lodash';
import { dictApi } from '/@/api/support/dict-api.js';
import { smartSentry } from '/@/lib/smart-sentry.js';

export const useDictStore = defineStore({
  id: 'dict',
  state: () => ({
    // 字典code集合
    dictList: [],
    // 字典集合
    dictMap: new Map(),
  }),

  actions: {
    getDictList(){
      return this.dictList;
    },
    // 获取字典数据
    getDictData(dictCode) {
      if (!dictCode) {
        return [];
      }
      let dictDataList = this.dictMap.get(dictCode);
      return dictDataList ? dictDataList : [];
    },

    // 获取字典的值名称
    getDataLabels(dictCode, dataValue) {
      if (_.isNil(dataValue) || _.isNaN(dataValue)) {
        return '';
      }

      let dict = this.getDictData(dictCode);
      if (dict.length === 0) {
        return '';
      }

      // 是数字的话，需要特殊处理
      if (_.isNumber(dataValue)) {
        let target = _.find(dict, { dataValue: String(dataValue) });
        return target ? target.dataLabel : '';
      }

      let valueArray = dataValue.split(DICT_SPLIT);
      let result = [];
      for (let item of valueArray) {
        let target = _.find(dict, { dataValue: item });
        if (target) {
          result.push(target.dataLabel);
        }
      }
      return result.join(DICT_SPLIT);
    },
    // 刷新字典
    async refreshData(){
      try{
        const dictRes = await dictApi.getAllDictData();
        this.initData(dictRes.data);
      }catch (e){
        smartSentry.captureError(e);
      }
    },
    // 初始化字典
    initData(dictDataMap) {
      this.dictMap.clear();
      this.dictList = [];

      if (!dictDataMap || typeof dictDataMap !== 'object') {
        return;
      }

      for (const [dictCode, list] of Object.entries(dictDataMap)) {
        const frontendList = list.map(item => ({
          dataLabel: item.dictLabel,
          dataValue: item.dictValue,
        }));

        this.dictMap.set(dictCode, frontendList);
        // 后端接口未返回 dictName，暂时用 dictCode 代替
        this.dictList.push({ dictCode: dictCode, dictName: dictCode });
      }
    },
  },
});
