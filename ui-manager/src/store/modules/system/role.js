/*
 * 角色
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:54:39
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import _ from 'lodash';
import { defineStore } from 'pinia';

export const useRoleStore = defineStore({
  id: 'role',
  state: () => ({
    checkedData: [],
    treeMap: new Map(),
  }),

  actions: {
    // 初始化权限树选中数据
    initCheckedData(data) {
      this.checkedData = [...new Set(data)];
    },
    // 选中
    addCheckedData(data) {
      if (this.checkedData.some((e) => e === data)) {
        return;
      }
      this.checkedData.push(data);
    },
    // 选中本级以及子级
    addCheckedDataAndChildren(data) {
      let findIndex = this.checkedData.findIndex((val) => val === data.id);
      if (data.id && findIndex === -1) {
        this.addCheckedData(data.id);
      }
      if (data.children) {
        data.children.forEach((item) => {
          this.addCheckedDataAndChildren(item);
        });
      }
    },
    // 取消选中
    deleteCheckedData(index) {
      this.checkedData.splice(index, 1);
    },
    // 取消选中本级以及子级
    deleteCheckedDataAndChildren(data) {
      let findIndex = this.checkedData.findIndex((val) => val === data.id);
      if (findIndex !== -1) {
        this.deleteCheckedData(findIndex);
      }
      if (data.children) {
        data.children.forEach((item) => {
          this.deleteCheckedDataAndChildren(item);
        });
      }
    },
    // 初始化权限树对象
    initTreeMap(tree) {
      for (let treeElement of tree) {
        if (!treeElement.id) {
          continue;
        }
        this.treeMap.set(treeElement.id, treeElement);
        if (treeElement.children && !_.isEmpty(treeElement.children)) {
          this.initTreeMap(treeElement.children);
        }
      }
    },
    // 选中上一级
    selectUpperLevel(module) {
      // 拿到上级key
      let parentId = module.parentId;
      if (!parentId) {
        return;
      }
      // 从权限树对象 获取该父级对象
      let parentModule = this.treeMap.get(parentId);
      if (!parentModule) {
        return;
      }
      // 选中父级
      let parentIndex = this.checkedData.findIndex((e) => parentModule.id === e);
      if (parentModule.id && parentIndex === -1) {
        this.addCheckedData(parentModule.id);
      }
      // 如果上级还有上级 则进行递归
      if (parentModule.parentId) {
        this.selectUpperLevel(parentModule);
      }
    },
  },
});
