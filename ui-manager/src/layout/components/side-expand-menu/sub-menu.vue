<!--
  * 第二列菜单区域
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-sub-menu :key="menuInfo.id.toString()" v-bind="$attrs">
    <template #title>
      <span>
        <component :is="$antIcons[menuInfo.meta?.icon || menuInfo.iconCls]" />
        <span>{{ menuInfo.meta?.title || menuInfo.name }}</span>
      </span>
    </template>
    <template v-for="item in menuInfo.children" :key="item.id">
      <template v-if="!item.hidden">
        <template v-if="!item.children || item.children.length === 0">
          <a-menu-item :key="item.id.toString()" @click="turnToPage(item)">
            <template #icon>
              <component :is="$antIcons[item.meta?.icon || item.iconCls]" />
            </template>
            {{ item.meta?.title || item.name }}
          </a-menu-item>
        </template>
        <template v-else>
          <sub-menu :menu-info="item" :key="item.id" @turnToPage="turnToPage" />
        </template>
      </template>
    </template>
  </a-sub-menu>
</template>
<script setup>
  let props = defineProps({
    menuInfo: Object,
  });
  const emits = defineEmits(['turnToPage']);
  const turnToPage = (route) => {
    emits('turnToPage', route);
  };
</script>
<style scoped lang="less">
  :deep(.ant-menu-item-selected) {
    border-right: 3px !important;
  }
</style>
