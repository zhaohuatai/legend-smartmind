/*
 * 路由
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:52:04
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import nProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { nextTick } from 'vue';
import { createRouter, createWebHashHistory } from 'vue-router';
import { routerArray } from './routers';
import { PAGE_PATH_404, PAGE_PATH_LOGIN, HOME_PAGE_PATH } from '/@/constants/common-const';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
import SmartLayout from '../layout/index.vue';
import { useUserStore } from '/@/store/modules/system/user';
import { localRead } from '/@/utils/local-util';
import _ from 'lodash';
import LocalStorageKeyConst from '/@/constants/local-storage-key-const.js';

export const router = createRouter({
  history: createWebHashHistory(),
  routes: routerArray,
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// ----------------------- 路由加载前 -----------------------
router.beforeEach(async (to, from, next) => {
  // 进度条开启
  nProgress.start();

  // 公共页面，任何时候都可以跳转
  if (to.path === PAGE_PATH_404) {
    next();
    return;
  }

  // 验证登录
  const token = localRead(LocalStorageKeyConst.USER_TOKEN);
  const csrfToken = localRead('CSRF_TOKEN');
  if (!token && !csrfToken) {
    useUserStore().logout();
    if (to.path === PAGE_PATH_LOGIN) {
      next();
    } else {
      next({ path: PAGE_PATH_LOGIN });
    }
    return;
  }

  // 登录页，则跳转到首页
  if (to.path === PAGE_PATH_LOGIN) {
    next({ path: HOME_PAGE_PATH });
    return;
  }

  // 首页（ 需要登录 ，但不需要验证权限）
  if (to.path === HOME_PAGE_PATH) {
    next();
    return;
  }

  // 下载路由对应的 页面组件，并修改组件的Name，如果修改过，则不需要修改
  let toRouterInfo = routerMap.get(to.name);
  if (toRouterInfo && _.isFunction(toRouterInfo.component) && toRouterInfo.meta.renameComponentFlag === false) {
    // 因为组件component 为 lazy load是个方法，所以可以直接执行 component()方法
    toRouterInfo.component().then((val) => {
      // 修改组件的name
      val.default.name = to.meta.componentName;
      // 记录已经修改过 组件的name
      toRouterInfo.meta.renameComponentFlag = true;
    });
  }

  // 设置tagNav
  useUserStore().setTagNav(to, from);

  // 设置keepAlive
  if (to.meta.keepAlive) {
    nextTick(() => {
      useUserStore().pushKeepAliveIncludes(to.meta.componentName);
    });
  }

  next();
});

// ----------------------- 路由加载后 -----------------------
router.afterEach(() => {
  nProgress.done();
});

// ----------------------- 构建router对象 -----------------------
const routerMap = new Map();

function resolveMenuTree(menuResponseData) {
  if (Array.isArray(menuResponseData)) {
    return menuResponseData;
  }
  if (menuResponseData && Array.isArray(menuResponseData.data)) {
    return menuResponseData.data;
  }
  if (menuResponseData && Array.isArray(menuResponseData.menuList)) {
    return menuResponseData.menuList;
  }
  return [];
}

function ensureLeadingSlash(val) {
  if (!val || typeof val !== 'string') {
    return '';
  }
  return val.startsWith('/') ? val : `/${val}`;
}

function normalizeComponentPath(component) {
  if (!component || typeof component !== 'string') {
    return '';
  }
  if (component === 'Layout') {
    return '';
  }
  let c = component;
  if (c.startsWith('/@/views')) {
    c = c.replace('/@/views', '');
  } else if (c.startsWith('@/views')) {
    c = c.replace('@/views', '');
  } else if (c.startsWith('views/')) {
    c = c.replace('views', '');
  }
  return ensureLeadingSlash(c);
}

// ----------------------- 菜单 adapter -----------------------
/**
 * 将 菜单树 转换为 适配 smart-admin 的菜单树
 */
function adaptMenuNodeTreeToSmartAdminMenuTree(menuTree) {
  if (!menuTree) return [];
  return menuTree.map(item => {
    let newItem = { ...item };
    // title
    if (item.meta && item.meta.title) {
        newItem.name = item.meta.title;
        newItem.title = item.meta.title;
    }
    // icon
    if (item.meta && item.meta.icon) {
        newItem.iconCls = item.meta.icon;
        newItem.icon = item.meta.icon;
    }
    // hidden
    if (item.hidden !== undefined) {
        newItem.show = !item.hidden;
        newItem.hide = item.hidden ? 1 : 0;
    }
    // keepAlive
    if (item.meta && item.meta.noCache !== undefined) {
        newItem.keepAlive = !item.meta.noCache;
    }

    if (item.children) {
        newItem.children = adaptMenuNodeTreeToSmartAdminMenuTree(item.children);
    }
    return newItem;
  });
}

/**
 * 扁平化菜单树
 */
function flattenMenuTree(menuTree) {
  let res = [];
  menuTree.forEach(item => {
    res.push(item);
    if (item.children) {
      res = res.concat(flattenMenuTree(item.children));
    }
  });
  return res;
}

export function normalizeMenuList(menuResponseData) {
  const tree = resolveMenuTree(menuResponseData);
  const adaptedTree = adaptMenuNodeTreeToSmartAdminMenuTree(tree);
  return adaptedTree;
}

export function buildRoutes(menuRouterList) {
  let menuList = menuRouterList ? flattenMenuTree(menuRouterList) : useUserStore().getMenuRouterList || [];
  /**
   * 1、构建整个路由信息
   * 2、添加到路由里
   */
  const routerList = [];
  // 获取所有vue组件引用地址 用于构建路由
  const modules = import.meta.glob('../views/**/**.vue');
  // 获取所有vue组件 用于注入name属性 name属性用于keep-alive

  //1、构建整个路由信息
  for (const e of menuList) {
    if (!e.id) {
      continue;
    }
    if (!e.path) {
      continue;
    }
    if (e.deletedFlag && e.deletedFlag === true) {
      continue;
    }
    let route = {
      path: e.path.startsWith('/') ? e.path : `/${e.path}`,
      // 使用【id】作为name唯一标识
      name: e.id.toString(),
      meta: {
        // 数据库菜单(页面)id
        id: e.id.toString(),
        // 组件名称
        componentName: e.id.toString(),
        // 菜单展示
        title: e.name,
        // 菜单图标展示
        icon: e.iconCls,
        // 是否在菜单隐藏
        hideInMenu: !e.show,
        // 页面是否keep-alive缓存
        keepAlive: e.keepAlive,
        // 是否为外链
        frameFlag: e.frameFlag,
        // 外链地址
        frameUrl: e.frameUrl,
        // 是否 rename了组件的名字
        renameComponentFlag: false,
      },
    };

    if (e.frameFlag) {
      route.component = () => import('../components/framework/iframe/iframe-index.vue');
    } else {
        const componentStr = e.component ? (e.component.startsWith('/') ? e.component : '/' + e.component) : '';
        if (e.component === 'Layout') {
          route.component = SmartLayout;
        } else {
          const relativePath = `../views${componentStr}`;
          route.component = modules[relativePath];
          
          // 兼容处理：尝试自动补全 .vue 后缀
          if (!route.component) {
            route.component = modules[`${relativePath}.vue`];
          }
          
          // 兼容处理：尝试自动补全 /index.vue (用于目录形式的组件)
          if (!route.component) {
            route.component = modules[`${relativePath}/index.vue`];
          }
          
          // 兼容处理：menu-list.vue -> index.vue (代码迁移兼容)
          if (!route.component && relativePath.includes('menu-list')) {
           const indexPath = relativePath.replace('menu-list', 'index');
           route.component = modules[indexPath] || modules[`${indexPath}.vue`];
        }
      }
      
      // 兼容处理：如果找不到组件，则默认指向 404
      if (!route.component) {
        route.component = modules['../views/system/40X/404.vue'] || (() => Promise.resolve({ default: { render: () => h('div', '404 Component Not Found') } }));
      }
    }
    routerList.push(route);
    routerMap.set(e.id.toString(), route);
  }

  //2、添加到路由里
  router.addRoute({
    path: '/',
    meta: {},
    component: SmartLayout,
    children: routerList,
  });
}
