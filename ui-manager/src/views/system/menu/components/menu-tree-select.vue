<!--
  * 菜单 表单 树形下拉框
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-12 20:11:39
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :fieldNames="{ label: 'name', key: 'id', value: 'id' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择菜单"
    allow-clear
    treeNodeFilterProp="name"
    @change="treeSelectChange"
  />
</template>
<script setup>
  import { onMounted, ref } from 'vue';
  import { menuApi } from '/@/api/system/menu-api';

  const props = defineProps({
    value: [Number, String],
  });

  let treeData = ref([]);
  async function queryMenuTree() {
    let res = await menuApi.queryMenuTree();
    treeData.value = [res.data];
  }

  onMounted(queryMenuTree);

  const emit = defineEmits(['update:value']);
  function treeSelectChange(e) {
    emit('update:value', e);
  }

  defineExpose({
    queryMenuTree,
  });
</script>
