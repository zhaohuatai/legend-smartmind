/*
 * 此文件是处理 菜单数据的类，主要用于：
 * 1、菜单树形表格的构造
 * 2、菜单的前端过滤
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-06-15 16:47:20
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import _ from 'lodash';
/**
 * 过滤菜单
 * @param {*} menuList
 * @param {*} queryForm
 * @returns
 */
export const filterMenuByQueryForm = (menuList, queryForm) => {
  if (!menuList || menuList.length === 0) {
    return [];
  }

  let filterResult = [];
  for (const menu of menuList) {
    if (isMenuExistKeywords(menu, queryForm.keywords) && isMenuExistMenuType(menu, queryForm.menuType) && isMenuExistMenuFlag(menu, queryForm)) {
      filterResult.push(menu);
    }
  }
  return filterResult;
};

/**
 * 构建菜单表格树形数据
 */
export const buildMenuTableTree = (menuList) => {
  let topMenuList = [];
  const menuIdSet = new Set();
  for (const menu of menuList) {
    menuIdSet.add(menu.id);
  }

  for (const menu of menuList) {
    const parentId = menu.parentId;
    // 不存在父节点，则为顶级菜单 (或 parentId 为 -2 / 0)
    if (!menuIdSet.has(parentId) || parentId == '-2' || parentId == '0') {
      // 避免重复添加 (如果有 -2 和 0 混用)
      if(!topMenuList.includes(menu)) {
         topMenuList.push(menu);
      }
    }
  }

  recursiveMenuTree(menuList, topMenuList);
  return topMenuList;
};

/**
 * 递归遍历菜单树形数据
 * @param {*} menuList
 * @param {*} parentArray
 */
function recursiveMenuTree(menuList, parentArray) {
  for (const parent of parentArray) {
    const children = menuList.filter((e) => e.parentId === parent.id);
    if (children.length > 0) {
      parent.children = children;
      recursiveMenuTree(menuList, parent.children);
    }
  }
}

/**
 * 过滤菜单状态
 * @param {*} menu
 * @param {*} queryForm
 * @returns
 */
function isMenuExistMenuFlag(menu, queryForm) {
  let frameFlagCondition = false;
  // frameFlag 后端暂无，假设默认为非外链或逻辑需调整
  if (!_.isNil(queryForm.frameFlag)) {
     // 临时处理：如果没有 frameFlag 字段，认为不匹配或默认为 0
    frameFlagCondition = !_.isNil(menu.frameFlag) && menu.frameFlag === (queryForm.frameFlag === 1);
  } else {
    frameFlagCondition = true;
  }

  let cacheFlagCondition = false;
  if (!_.isNil(queryForm.cacheFlag)) {
    cacheFlagCondition = !_.isNil(menu.cacheFlag) && menu.cacheFlag === (queryForm.cacheFlag === 1);
  } else {
    cacheFlagCondition = true;
  }

  let hideCondition = false;
  if (!_.isNil(queryForm.hide)) {
    hideCondition = menu.hide === queryForm.hide;
  } else {
    hideCondition = true;
  }

  let statusCondition = false;
  if (!_.isNil(queryForm.status)) {
     statusCondition = menu.status === queryForm.status;
  } else {
    statusCondition = true;
  }

  return frameFlagCondition && cacheFlagCondition && hideCondition && statusCondition;
}

function isMenuExistKeywords(menu, keywords) {
  if (!keywords) {
    return true;
  }
  const keywordsLowerCase = keywords.toLowerCase();
  return (
    (menu.name && menu.name.toLowerCase().indexOf(keywordsLowerCase) > -1) ||
    (menu.path && menu.path.toLowerCase().indexOf(keywordsLowerCase) > -1) ||
    (menu.component && menu.component.toLowerCase().indexOf(keywordsLowerCase) > -1) ||
    (menu.permName && menu.permName.toLowerCase().indexOf(keywordsLowerCase) > -1) ||
    (menu.permId && menu.permId.toLowerCase().indexOf(keywordsLowerCase) > -1)
  );
}

function isMenuExistMenuType(menu, menuType) {
  if (!menuType) {
    return true;
  }
  return menu.menuType === menuType;
}
