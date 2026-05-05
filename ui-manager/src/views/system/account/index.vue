<template>
  <div class="account-container">
    <!--菜单列-->
    <div class="account-menu-list">
      <a-menu v-model:selectedKeys="selectedKeys" mode="inline" @click="selectMenu($event.key)">
        <a-menu-item v-for="item in menuList" :key="item.id">
          <span v-if="item.id === 'message'">
            {{ item.name }}
            <a-badge :count="unreadMessageCount" style="margin-left: 10px" />
          </span>
          <span v-if="item.id !== 'message'">{{ item.name }} </span>
        </a-menu-item>
      </a-menu>
    </div>
    <!--内容区域-->
    <div class="account-content">
      <component :is="selectedMenu.components" />
    </div>
  </div>
</template>
<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import _ from 'lodash';
  import { ACCOUNT_MENU } from '/@/views/system/account/account-menu.js';
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user.js';
  import { theme as antDesignTheme } from 'ant-design-vue';

  // 菜单展示
  let menuList = computed(() => {
    return _.values(ACCOUNT_MENU);
  });
  // 选中的菜单
  let selectedMenu = ref({ id: 0 });
  let selectedKeys = ref([]);
  watch(
    () => selectedMenu.value,
    (newQuery, oldQuery) => {
      selectedKeys.value = _.isEmpty(selectedMenu.value) ? [] : [selectedMenu.value.id];
    }
  );

  function selectMenu(id) {
    selectedMenu.value = menuList.value.find((e) => e.id === id);
  }

  // ------------------------- 未读消息数量  -------------------------
  const unreadMessageCount = computed(() => {
    return useUserStore().unreadMessageCount;
  });

  // ------------------------- 绑定路由参数  -------------------------
  const route = useRoute();
  onMounted(() => {
    if (_.isEmpty(menuList.value)) {
      return;
    }
    let id;
    if (route.query.id) {
      id = route.query.id;
    } else {
      let firstMenu = menuList.value[0];
      id = firstMenu.id;
    }
    selectMenu(id);
  });

  watch(
    () => route.query,
    (newQuery, oldQuery) => {
      let id;
      if (route.query.id) {
        id = route.query.id;
      } else {
        let firstMenu = menuList.value[0];
        id = firstMenu.id;
      }
      selectMenu(id);
    }
  );
  const { useToken } = antDesignTheme;
  const { token } = useToken();
</script>
<style lang="less" scoped>
  @color-bg-container: v-bind('token.colorBgContainer');

  .account-container {
    display: flex;
    height: 100%;

    .account-menu-list {
      width: 180px;
      height: calc(100% - 100);
    }

    .account-content {
      flex: 1;
      margin-left: 10px;
      padding: 20px;
      border-radius: 8px;
      background-color: @color-bg-container;
    }
  }
</style>
