/*
 * 登录用户
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:55:09
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import _ from 'lodash';
import { defineStore } from 'pinia';
import localKey from '/@/constants/local-storage-key-const';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
import { smartSentry } from '/@/lib/smart-sentry.js';
import { localRead, localSave, localRemove } from '/@/utils/local-util';


export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    //员工id
    employeeId: '',
    // 头像
    avatar: '',
    //登录名
    account: '',
    //姓名
    perName: '',
    //手机号
    phone: '',
    //部门id
    deptId: '',
    //部门名词
    departmentName: '',
    //是否需要修改密码
    needUpdatePwdFlag: false,
    //是否为超级管理员
    administratorFlag: true,
    //上次登录ip
    lastLoginIp: '',
    //上次登录ip地区
    lastLoginIpRegion: '',
    //上次登录 设备
    lastLoginUserAgent: '',
    //上次登录时间
    lastLoginTime: '',
    //左侧菜单树形结构
    menuTree: [],
    //存在页面路由的菜单集合
    menuRouterList: [],
    //是否完成menuRouter初始化
    menuRouterInitFlag: false,
    //父类菜单集合
    menuParentIdListMap: new Map(),
    // 功能点集合
    pointsList: [],
    // 标签页
    tagNav: null,
    // 缓存
    keepAliveIncludes: [],
    // 未读消息数量
    unreadMessageCount: 0,
    // 待办工作数
    toBeDoneCount: 0,
  }),
  getters: {
    getToken(state) {
      if (state.token) {
        return state.token;
      }
      return localRead(localKey.USER_TOKEN);
    },
    getNeedUpdatePwdFlag(state){
      return state.needUpdatePwdFlag;
    },
    //是否初始化了 路由
    getMenuRouterInitFlag(state) {
      return state.menuRouterInitFlag;
    },
    //菜单树
    getMenuTree(state) {
      return state.menuTree;
    },
    //菜单的路由
    getMenuRouterList(state) {
      return state.menuRouterList;
    },
    //菜单的父级id
    getMenuParentIdListMap(state) {
      return state.menuParentIdListMap;
    },
    //功能点
    getPointList(state) {
      if (_.isEmpty(state.pointsList)) {
        let localUserPoints = localRead(localKey.USER_POINTS) || '';
        state.pointsList = localUserPoints ? JSON.parse(localUserPoints) : [];
      }
      return state.pointsList;
    },
    //标签页
    getTagNav(state) {
      if (_.isNull(state.tagNav)) {
        let localTagNav = localRead(localKey.USER_TAG_NAV) || '';
        state.tagNav = localTagNav ? JSON.parse(localTagNav) : [];
      }
      let tagNavList = _.cloneDeep(state.tagNav) || [];
      tagNavList.unshift({
        name: HOME_PAGE_NAME,
        title: '首页',
      });
      return tagNavList;
    },
  },

  actions: {
    logout() {
      this.$reset();
      this.tagNav = [];
      this.unreadMessageCount = 0;
      localRemove(localKey.USER_TOKEN);
      localRemove('CSRF_TOKEN');
      localRemove(localKey.USER_POINTS);
      localRemove(localKey.USER_TAG_NAV);
      localRemove(localKey.APP_CONFIG);
      localRemove(localKey.HOME_QUICK_ENTRY);
      localRemove(localKey.NOTICE_READ);
      localRemove(localKey.TO_BE_DONE);
    },
    // 查询未读消息数量
    async queryUnreadMessageCount() {
      this.unreadMessageCount = 0;
    },
    async queryToBeDoneList() {
      try {
        let localToBeDoneList = localRead(localKey.TO_BE_DONE);
        if (localToBeDoneList) {
          this.toBeDoneCount = JSON.parse(localToBeDoneList).filter((e) => !e.doneFlag).length;
        }
      } catch (err) {
        smartSentry.captureError(err);
      }
    },
    //设置登录信息
    setUserLoginInfo(data) {
      // 用户基本信息
      this.employeeId = data.employeeId;
      this.avatar = data.avatar;
      this.account = data.account;
      this.perName = data.perName;
      this.phone = data.phone;
      this.deptId = data.deptId;
      this.departmentName = data.departmentName;
      this.needUpdatePwdFlag = data.needUpdatePwdFlag;
      this.administratorFlag = data.administratorFlag;
      this.lastLoginIp = data.lastLoginIp;
      this.lastLoginIpRegion = data.lastLoginIpRegion;
      this.lastLoginUserAgent = data.lastLoginUserAgent;
      this.lastLoginTime = data.lastLoginTime;

      //菜单权限
      if (data.menuList) {
        // 判断是否已经是树形结构
        let isTree = data.menuList.length > 0 && (data.menuList[0].children || data.menuList[0].parentId === '0' || data.menuList[0].parentId === 0);
        
        let menuList = data.menuList;
        if (isTree) {
            // 如果是树形结构，扁平化获取路由列表等
            const flatten = (list) => {
                let res = [];
                list.forEach(item => {
                    res.push(item);
                    if (item.children) {
                        res = res.concat(flatten(item.children));
                    }
                });
                return res;
            };
            let flatList = flatten(menuList);
            
            // 过滤隐藏菜单用于左侧展示
            const filterVisible = (nodes) => {
                return nodes.filter(node => {
                    if (node.hidden === true) return false;
                    // 保留子节点
                    if (node.children) {
                        // 浅拷贝避免修改原始数据
                        node = {...node, children: filterVisible(node.children)};
                    }
                    return true;
                }).map(node => {
                    // 确保 children 存在
                     if (node.children) {
                        return {...node};
                     }
                     return node;
                });
            };
            
            this.menuTree = filterVisible(menuList);
            
            // 拥有路由的菜单
            this.menuRouterList = flatList.filter((e) => e.path || e.frameUrl);
            
            // 父级菜单集合 (用于面包屑等)
            this.menuParentIdListMap = buildMenuParentIdListMap(this.menuTree);
            
            // 功能点 (假设仍在 menuList 中，或者如果结构改变可能需要调整)
            // 如果 menuType 存在则使用，否则可能无法获取功能点
            this.pointsList = flatList.filter((menu) => menu.menuType === MENU_TYPE_ENUM.POINTS.value);
            
        } else {
            // 旧逻辑：扁平列表转树
            this.menuTree = buildMenuTree(data.menuList);
            this.menuRouterList = data.menuList.filter((e) => e.path || e.frameUrl);
            this.menuParentIdListMap = buildMenuParentIdListMap(this.menuTree);
            this.pointsList = data.menuList.filter((menu) => menu.menuType === MENU_TYPE_ENUM.POINTS.value && menu.hide != 1 && menu.status == 1);
        }
      }

      // 获取用户未读消息
      this.queryUnreadMessageCount();
      // 获取待办工作数
      this.queryToBeDoneList();
    },

    //设置标签页
    setTagNav(route, from) {
      if (_.isNull(this.tagNav)) {
        let localTagNav = localRead(localKey.USER_TAG_NAV) || '';
        this.tagNav = localTagNav ? JSON.parse(localTagNav) : [];
      }
      // name唯一标识
      let name = route.name;
      if (!name || name === HOME_PAGE_NAME || name === '403' || name === '404') {
        return;
      }
      let findTag = (this.tagNav || []).find((e) => e.name === name);
      if (findTag) {
        // @ts-ignore
        findTag.fromName = from.name;
        findTag.fromQuery = from.query;
        findTag.query = route.query;
      } else {
        // @ts-ignore
        this.tagNav.push({
          // @ts-ignore
          name: name,
          // @ts-ignore
          title: route.meta.title,
          query: route.query,
          icon: route.meta.icon,
          // @ts-ignore
          fromName: from.name,
          fromQuery: from.query,
        });
      }
      localSave(localKey.USER_TAG_NAV, JSON.stringify(this.tagNav));
    },
    //关闭标签页
    closeTagNav(name, closeAll) {
      if (_.isEmpty(this.getTagNav)) return;
      if (closeAll && !name) {
        this.tagNav = [];
        this.clearKeepAliveIncludes();
      } else {
        let findIndex = (this.tagNav || []).findIndex((e) => e.name === name);
        if (closeAll) {
          if (findIndex === -1) {
            this.tagNav = [];
            this.clearKeepAliveIncludes();
          } else {
            let tagNavElement = (this.tagNav || [])[findIndex];
            this.tagNav = [tagNavElement];
            this.clearKeepAliveIncludes(tagNavElement.name);
          }
        } else {
          (this.tagNav || []).splice(findIndex, 1);
          this.deleteKeepAliveIncludes(name);
        }
      }
      localSave(localKey.USER_TAG_NAV, JSON.stringify(this.tagNav));
    },
    //关闭页面
    closePage(route, router, path) {
      if (!this.getTagNav || _.isEmpty(this.getTagNav)) return;
      if (path) {
        router.push({ path });
      } else {
        // 寻找tagNav
        let index = this.getTagNav.findIndex((e) => e.name === route.name);
        if (index === -1) {
          router.push({ name: HOME_PAGE_NAME });
        } else {
          let tagNav = this.getTagNav[index];
          if (tagNav.fromName && this.getTagNav.some((e) => e.name === tagNav.fromName)) {
            router.push({ name: tagNav.fromName, query: tagNav.fromQuery });
          } else {
            // 查询左侧tag
            let leftTagNav = this.getTagNav[index - 1];
            router.push({ name: leftTagNav.name, query: leftTagNav.query });
          }
        }
      }
      this.closeTagNav(route.name, false);
    },
    // 加入缓存
    pushKeepAliveIncludes(val) {
      if (!val) {
        return;
      }
      if (!this.keepAliveIncludes) {
        this.keepAliveIncludes = [];
      }
      if (this.keepAliveIncludes.length < 30) {
        let number = this.keepAliveIncludes.findIndex((e) => e === val);
        if (number === -1) {
          this.keepAliveIncludes.push(val);
        }
      }
    },
    // 删除缓存
    deleteKeepAliveIncludes(val) {
      if (!this.keepAliveIncludes || !val) {
        return;
      }
      let number = this.keepAliveIncludes.findIndex((e) => e === val);
      if (number !== -1) {
        this.keepAliveIncludes.splice(number, 1);
      }
    },
    // 清空缓存
    clearKeepAliveIncludes(val) {
      if (!val || !this.keepAliveIncludes.includes(val)) {
        this.keepAliveIncludes = [];
        return;
      }
      this.keepAliveIncludes = [val];
    },
  },
});

/**
 * 构建菜单父级集合
 */
function buildMenuParentIdListMap(menuTree) {
  let menuParentIdListMap = new Map();
  recursiveBuildMenuParentIdListMap(menuTree, [], menuParentIdListMap);
  return menuParentIdListMap;
}

function recursiveBuildMenuParentIdListMap(menuList, parentMenuList, menuParentIdListMap) {
  for (const e of menuList) {
    // 顶级parentMenuList清空
    if (e.parentId === 0) {
      parentMenuList = [];
    }
    let menuIdStr = e.id.toString();
    let cloneParentMenuList = _.cloneDeep(parentMenuList);
    if (!_.isEmpty(e.children) && e.name) {
      // 递归
      cloneParentMenuList.push({ name: menuIdStr, title: e.meta?.title || e.name });
      recursiveBuildMenuParentIdListMap(e.children, cloneParentMenuList, menuParentIdListMap);
    } else {
      menuParentIdListMap.set(menuIdStr, cloneParentMenuList);
    }
  }
}

/**
 * 构建菜单树
 *
 * @param  menuList
 * @returns
 */
function buildMenuTree(menuList) {
  //1 获取所有 有效的 目录和菜单
  let catalogAndMenuList = menuList.filter((menu) => menu.menuType !== MENU_TYPE_ENUM.POINTS.value && menu.hide != 1 && menu.status == 1);

  //2 获取顶级目录
  let topCatalogList = catalogAndMenuList.filter((menu) => menu.parentId === 0);
  for (const topCatalog of topCatalogList) {
    buildMenuChildren(topCatalog, catalogAndMenuList);
  }
  return topCatalogList;
}

function buildMenuChildren(menu, allMenuList) {
  let children = allMenuList.filter((e) => e.parentId === menu.id);
  if (children.length === 0) {
    return;
  }
  menu.children = children;
  for (const item of children) {
    buildMenuChildren(item, allMenuList);
  }
}
