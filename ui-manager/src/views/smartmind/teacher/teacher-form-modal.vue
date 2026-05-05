<template>
  <a-modal
    v-model:open="visible"
    :title="form.id ? '编辑教师' : '新建教师'"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="560px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <!-- 工号 -->
      <a-form-item label="工号" name="teacherNo">
        <a-input
          v-model:value="form.teacherNo"
          placeholder="请输入工号"
          :maxLength="50"
          show-count
          size="large"
          :disabled="!!form.id"
        >
          <template #prefix>
            <IdcardOutlined style="color: #667eea" />
          </template>
        </a-input>
        <div v-if="!form.id" style="color: #ff4d4f; font-size: 12px; margin-top: 4px;">
          ⚠️ 工号即登录账户，请仔细核对后填写，提交后不可修改
        </div>
        <div v-else style="color: #999; font-size: 12px; margin-top: 4px;">
          工号不可修改
        </div>
      </a-form-item>

      <!-- 姓名 -->
      <a-form-item label="姓名" name="teacherName">
        <a-input
          v-model:value="form.teacherName"
          placeholder="请输入教师姓名"
          :maxLength="100"
          show-count
          size="large"
        >
          <template #prefix>
            <UserOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 手机号 -->
      <a-form-item label="手机号" name="phone">
        <a-input
          v-model:value="form.phone"
          placeholder="请输入手机号"
          :maxLength="20"
          show-count
          size="large"
        >
          <template #prefix>
            <PhoneOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 邮箱 -->
      <a-form-item label="邮箱" name="email">
        <a-input
          v-model:value="form.email"
          placeholder="请输入邮箱地址"
          :maxLength="100"
          show-count
          size="large"
        >
          <template #prefix>
            <MailOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 职称 -->
      <a-form-item label="职称" name="title">
        <a-select
          v-model:value="form.title"
          placeholder="请选择职称"
          size="large"
          allowClear
        >
          <a-select-option
            v-for="item in dictStore.getDictData('teacher_title')"
            :key="item.dataValue"
            :value="item.dataValue"
          >
            {{ item.dataLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!-- 状态 -->
      <a-form-item label="状态" name="status">
        <a-select
          v-model:value="form.status"
          placeholder="请选择状态"
          size="large"
        >
          <a-select-option value="1">
            <span class="status-option enabled">● 有效</span>
          </a-select-option>
          <a-select-option value="0">
            <span class="status-option disabled">● 失效</span>
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { teacherApi } from '/@/api/smartmind/teacher-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    IdcardOutlined,
    UserOutlined,
    PhoneOutlined,
    MailOutlined,
  } from '@ant-design/icons-vue';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  // 表单默认值 - 只保留指定字段
  const formDefault = {
    id: undefined,
    teacherNo: undefined,
    teacherName: undefined,
    phone: undefined,
    email: undefined,
    title: undefined,
    status: '1',
  };

  let form = reactive({ ...formDefault });

  // 表单校验规则
  const rules = {
    teacherNo: [
      { required: true, message: '请输入工号' },
      { max: 50, message: '工号最多50个字符' }
    ],
    teacherName: [
      { required: true, message: '请输入姓名' },
      { max: 100, message: '姓名最多100个字符' }
    ],
    phone: [
      { max: 20, message: '手机号最多20个字符' }
    ],
    email: [
      { max: 100, message: '邮箱最多100个字符' },
      { type: 'email', message: '请输入有效的邮箱地址' }
    ],
    status: [{ required: true, message: '请选择状态' }],
  };

  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      nextTick(() => {
        Object.assign(form, rowData);
      });
    }
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
    reset();
  }

  function reset() {
    Object.assign(form, formDefault);
    if (formRef.value) {
      formRef.value.resetFields();
    }
  }

  async function onSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      SmartLoading.show();

      if (form.id) {
        await teacherApi.update(form);
        message.success('修改成功');
      } else {
        await teacherApi.add(form);
        message.success('创建成功');
      }

      visible.value = false;
      emit('reloadList');
      reset();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      confirmLoading.value = false;
      SmartLoading.hide();
    }
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped>
  /* 状态下拉选项样式 */
  .status-option {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
  }

  .status-option.enabled {
    color: #059669;
  }

  .status-option.disabled {
    color: #dc2626;
  }
</style>
