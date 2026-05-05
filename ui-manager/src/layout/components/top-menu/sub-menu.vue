<!--
  * 顶部菜单-递归菜单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-sub-menu :key="menuInfo.id">
    <template #icon>
      <component :is="$antIcons[menuInfo.meta?.icon || menuInfo.iconCls]" />
    </template>
    <template #title>{{ menuInfo.meta?.title || menuInfo.name }}</template>
    <template v-for="item in menuInfo.children" :key="item.id">
      <template v-if="!item.hidden">
        <template v-if="$lodash.isEmpty(item.children)">
          <a-menu-item :key="item.id" @click="turnToPage(item)">
            <template #icon>
              <component :is="$antIcons[item.meta?.icon || item.iconCls]" />
            </template>
            {{ item.meta?.title || item.name }}
          </a-menu-item>
        </template>
        <template v-else>
          <SubMenu :menu-info="item" :key="item.id" @turnToPage="turnToPage" />
        </template>
      </template>
    </template>
  </a-sub-menu>
</template>
<script setup>
  const props = defineProps({
    menuInfo: {
      type: Object,
      default: () => ({}),
    },
  });

  const emits = defineEmits(['turnToPage']);
  const turnToPage = (menu) => {
    emits('turnToPage', menu);
  };
</script>
