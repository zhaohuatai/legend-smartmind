<!--
  * 传统菜单-递归菜单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-menu :open-keys="openKeys" v-model:selectedKeys="selectedKeys" class="smart-menu" mode="inline" :theme="theme" @openChange="onOpenChange">
    <template v-for="item in menuTree" :key="item.id.toString()">
      <template v-if="!item.hidden">
        <template v-if="$lodash.isEmpty(item.children)">
          <a-menu-item :key="item.id.toString()" @click="turnToPage(item)">
            <template #icon>
              <component :is="$antIcons[item.icon]" />
            </template>
            {{ item.name }}
          </a-menu-item>
        </template>
        <template v-else>
          <SubMenu :menu-info="item" :key="item.id.toString()" @turnToPage="turnToPage" />
        </template>
      </template>
    </template>
  </a-menu>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import SubMenu from './sub-menu.vue';
  import { router } from '/@/router/index';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';

  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
  const menuSingleExpandFlag = computed(() => useAppConfigStore().$state.menuSingleExpandFlag);

  const props = defineProps({
    collapsed: {
      type: Boolean,
      default: false,
    },
    showOneLevel: {
      type: Boolean,
      default: true,
    },
  });

  const menuTree = computed(() => useUserStore().getMenuTree || []);
  const rootSubmenuKeys = computed(() => menuTree.value.map((item) => item.id.toString()));

  //展开的菜单
  let currentRoute = useRoute();
  const selectedKeys = ref([]);
  const openKeys = ref([]);
  const hasExpandedDefault = ref(false);

  // 页面跳转
  function turnToPage(menu) {
    useUserStore().deleteKeepAliveIncludes(menu.id.toString());
    // 修复路径问题：确保路径以 / 开头，避免被识别为相对路径导致路径叠加
    let path = menu.path;

    // 使用正则判断是否为 http 或 https 链接
    const isExternal = /^(http|https):\/\//.test(path);
    if (path && !path.startsWith('/') && !isExternal) {
      path = '/' + path;
    }
    router.push({ path });
  }

  /**
   * SmartAdmin中 router的name 就是 后端存储menu的id
   * 所以此处可以直接监听路由，根据路由更新菜单的选中和展开
   */
  function updateOpenKeysAndSelectKeys() {
    // 更新选中
    selectedKeys.value = currentRoute.name ? [currentRoute.name.toString()] : [];

    /**
     * 更新展开（1、获取新展开的menu key集合；2、保留原有的openkeys，然后把新展开的与之合并）
     */
    //获取需要展开的menu key集合
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    let parentList = menuParentIdListMap.get(currentRoute.name) || [];

    // 如果是折叠菜单的话，则不需要设置openkey
    if (props.collapsed) {
      return;
    }

    let needOpenKeys = _.map(parentList, 'name').map((e) => e.toString());

    // 默认展开三层
    // if (!hasExpandedDefault.value && menuTree.value.length > 0) {
    //   const defaultKeys = [];
    //   menuTree.value.forEach((item) => {
    //     // 第一层
    //     if (item.children && item.children.length > 0) {
    //       defaultKeys.push(item.id.toString());
    //     }
    //   });
    //   needOpenKeys = _.union(needOpenKeys, defaultKeys);
    //   hasExpandedDefault.value = true;
    // }

    if (menuSingleExpandFlag.value) {
      openKeys.value = [...needOpenKeys];
    } else {
      // 使用lodash的union函数，进行 去重合并两个数组
      openKeys.value = _.union(openKeys.value, needOpenKeys);
    }
  }

  watch(
    currentRoute,
    () => {
      updateOpenKeysAndSelectKeys();
    },
    {
      immediate: true,
    }
  );

  watch(menuTree, () => {
    updateOpenKeysAndSelectKeys();
  });

  function onOpenChange(openKeysParams) {
    if (!menuSingleExpandFlag.value) {
      return;
    }
    const latestOpenKey = openKeysParams.find((key) => openKeys.value.indexOf(key) === -1);
    if (rootSubmenuKeys.value.indexOf(latestOpenKey) === -1) {
      openKeys.value = openKeysParams;
    } else {
      openKeys.value = latestOpenKey ? [latestOpenKey] : [];
    }
  }
  defineExpose({
    updateOpenKeysAndSelectKeys,
  });
</script>

<style lang="less" scoped>
  .smart-menu {
    position: relative;
  }
</style>
