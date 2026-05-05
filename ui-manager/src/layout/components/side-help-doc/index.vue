<!--
  *  帮助文档
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:40:16
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="help-doc-wrapper">
    <!-----头部---->
    <div class="help-doc-header">
      <strong>帮助文档</strong>
      <strong class="help-doc-close" @click="hideHelpDoc"><close-outlined /></strong>
    </div>

    <!-----联系客服区域---->
    <div class="help-doc-contact" @click="contactModal.show">
      <div class="help-doc-contact-left">
        <phone-outlined style="font-size: 23px; line-height: 50px; margin-top: 5px" />
      </div>
      <div class="help-doc-contact-right">
        <a>联系客服</a>
        <div class="help-doc-contac-time">9:00-17:00 5x7小时</div>
      </div>
    </div>

    <a-divider />

    <!-----文档列表---->
    <div class="help-doc-list">
      <div class="help-doc-item-all">
        <router-link tag="a" target="_blank" :to="{ path: '/help-doc/detail' }">系统帮助文档 >></router-link>
      </div>
    </div>

    <!-----联系客服---->
    <ContactModal ref="contactModal" />
  </div>
</template>
<script setup>
  import { ref } from 'vue';
  import ContactModal from './components/contact-modal.vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  function hideHelpDoc() {
    useAppConfigStore().hideHelpDoc();
  }

  // ------------------ 联系客服 --------------------------
  const contactModal = ref();
</script>

<style scoped lang="less">
  .help-doc-wrapper {
    border-left: 1px solid #ededed;
    height: 100vh;
    padding: 0 10px;

    .help-doc-header {
      line-height: @header-user-height;
      display: flex;
      justify-content: space-between;
      height: @header-user-height;
      border-bottom: 1px solid #f6f6f6;

      .help-doc-close {
        cursor: pointer;
      }
    }

    .help-doc-contact {
      height: 50px;
      display: flex;
      cursor: pointer;
      margin-top: 5px;
      justify-content: space-between;
      .help-doc-contact-left {
        width: 30px;
        margin-top: 10px;
      }
      .help-doc-contact-right {
        margin-top: 10px;
        width: calc(100% - 40px);
        .help-doc-contac-time {
          color: #888;
          font-size: 12px;
          margin-top: 10px;
        }
      }
    }

    .help-doc-list {
      .help-doc-item-all {
        margin-top: 10px;
        color: @primary-color;
      }
      .help-doc-item {
        margin-top: 20px;
        a {
          color: #444;
        }
        a:hover {
          color: @primary-color;
          text-decoration: underline;
        }
      }
    }
  }
</style>
