<!--
  * 首页的 通知公告
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <default-home-card extra="更多" icon="SoundOutlined" title="通知公告" @extraClick="onMore">
    <div class="content-wrapper">
      <div class="empty-text">暂无数据</div>
    </div>
  </default-home-card>
</template>
<script setup>
  import { useRouter } from 'vue-router';
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';

  const props = defineProps({
    noticeTypeId: {
      type: Number,
      default: 1,
    },
  });

  // 查看更多
  function onMore() {
    router.push({
      path: '/oa/notice/notice-employee-list',
    });
  }

  const router = useRouter();
</script>
<style lang="less" scoped>
  @read-color: #666;
  .content-wrapper {
    height: 150px;
    overflow-y: hidden;
    overflow-x: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .empty-text {
    color: @read-color;
  }
</style>
