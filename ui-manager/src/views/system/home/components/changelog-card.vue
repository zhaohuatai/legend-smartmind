<!--
  * 更新日志
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <default-home-card extra="更多" icon="FlagOutlined" title="更新日志" @extraClick="onMore">
    <div class="empty-text">暂无数据</div>
  </default-home-card>
</template>
<script setup>
  import { useRouter } from 'vue-router';
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';

  const router = useRouter();

  // 查看更多
  function onMore() {
    router.push({
      path: '/support/change-log/change-log-list',
    });
  }
</script>
<style lang="less" scoped>
  .empty-text {
    height: 150px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #666;
  }
</style>
