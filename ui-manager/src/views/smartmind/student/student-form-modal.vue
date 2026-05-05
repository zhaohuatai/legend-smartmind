<template>
  <a-modal
    v-model:open="visible"
    :title="form.id ? '编辑学生' : '新建学生'"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="560px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <!-- 姓名 -->
      <a-form-item label="姓名" name="studentName">
        <a-input
          v-model:value="form.studentName"
          placeholder="请输入学生姓名"
          :maxLength="50"
          show-count
          size="large"
        >
          <template #prefix>
            <UserOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 学号 -->
      <a-form-item label="学号" name="studentNo">
        <a-input
          v-model:value="form.studentNo"
          placeholder="请输入学号（将作为登录账户）"
          :maxLength="50"
          show-count
          size="large"
          :disabled="!!form.id"
        >
          <template #prefix>
            <NumberOutlined style="color: #667eea" />
          </template>
        </a-input>
        <div v-if="!form.id" style="color: #ff4d4f; font-size: 12px; margin-top: 4px;">
          ⚠️ 学号即登录账户，请仔细核对后填写，提交后不可修改
        </div>
        <div v-else style="color: #999; font-size: 12px; margin-top: 4px;">
          学号不可修改
        </div>
      </a-form-item>

      <!-- 班级 -->
      <a-form-item label="班级" name="classId">
        <a-select
          v-model:value="form.classId"
          placeholder="请选择班级"
          allowClear
          size="large"
          :loading="classLoading"
        >
          <a-select-option
            v-for="item in classOptions"
            :key="item.value"
            :value="item.value"
          >
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!-- 电话与状态 -->
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="电话" name="phone">
            <a-input
              v-model:value="form.phone"
              placeholder="请输入联系电话"
              :maxLength="20"
              size="large"
            >
              <template #prefix>
                <PhoneOutlined style="color: #667eea" />
              </template>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-select
              v-model:value="form.status"
              placeholder="请选择状态"
              size="large"
            >
              <a-select-option value="0">在读</a-select-option>
              <a-select-option value="1">休学</a-select-option>
              <a-select-option value="2">退学</a-select-option>
              <a-select-option value="3">毕业</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 性别 -->
      <a-form-item label="性别" name="gender">
        <a-radio-group v-model:value="form.gender" button-style="solid" size="large">
          <a-radio-button value="1">男</a-radio-button>
          <a-radio-button value="0">女</a-radio-button>
          <a-radio-button value="2">保密</a-radio-button>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { studentApi } from '/@/api/smartmind/student-api.js';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    UserOutlined,
    NumberOutlined,
    PhoneOutlined
  } from '@ant-design/icons-vue';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const classOptions = ref([]);
  const classLoading = ref(false);

  const formDefault = {
    id: undefined,
    studentNo: undefined,
    studentName: undefined,
    gender: '1',
    phone: undefined,
    classId: undefined,
    status: '0',
  };
  let form = reactive({ ...formDefault });

  const rules = {
    studentName: [
      { required: true, message: '请输入学生姓名' },
      { max: 50, message: '姓名最多50个字符' }
    ],
    studentNo: [
      { required: true, message: '请输入学号' },
      { max: 50, message: '学号最多50个字符' }
    ],
    classId: [
      { required: true, message: '请选择班级' }
    ],
  };

  // 加载班级选项
  async function loadClassOptions() {
    classLoading.value = true;
    try {
      const res = await clazzApi.selectVo();
      classOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      classLoading.value = false;
    }
  }

  function showModal(rowData) {
    Object.assign(form, formDefault);
    loadClassOptions();
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
        await studentApi.update(form);
        message.success('修改成功');
      } else {
        await studentApi.add(form);
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
/* 原生AntD样式，无需额外自定义 */
</style>
