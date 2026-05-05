<template>
  <a-modal
    v-model:open="visible"
    :title="form.id ? '编辑班级' : '创建新班级'"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="560px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <!-- 班级名称 -->
      <a-form-item label="班级名称" name="className">
        <a-input
          v-model:value="form.className"
          placeholder="请输入班级名称，如：一年级3班"
          :maxLength="100"
          show-count
          size="large"
        >
          <template #prefix>
            <TeamOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 年级 -->
      <a-form-item label="年级" name="gradeLevel">
        <a-select
          v-model:value="form.gradeLevel"
          placeholder="请选择年级"
          size="large"
          allowClear
        >
          <a-select-option
            v-for="item in dictStore.getDictData('grade_level')"
            :key="item.dataValue"
            :value="item.dataValue"
          >
            {{ item.dataLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!-- 状态 -->
      <a-form-item label="班级状态" name="status">
        <a-select
          v-model:value="form.status"
          placeholder="请选择班级状态"
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

      <!-- 班级描述 -->
      <a-form-item label="班级描述" name="remark">
        <a-textarea
          v-model:value="form.remark"
          placeholder="请输入班级描述（选填）"
          :rows="5"
          :maxLength="500"
          show-count
          size="large"
        />
      </a-form-item>

      <!-- 底部提示 -->
      <a-alert
        v-if="!form.id"
        message="班级编码将由系统根据年级自动生成，无需手动填写"
        type="info"
        show-icon
        :show-description="false"
        style="margin-top: 8px"
      />
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick, h } from 'vue';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    TeamOutlined
  } from '@ant-design/icons-vue';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const formDefault = {
    id: undefined,
    className: undefined,
    gradeLevel: undefined,
    remark: undefined,
    status: '1',
  };
  let form = reactive({ ...formDefault });

  const rules = {
    className: [
      { required: true, message: '请输入班级名称' },
      { max: 100, message: '班级名称最多100个字符' }
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
        await clazzApi.update(form);
        message.success('修改成功');
      } else {
        await clazzApi.add(form);
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

    &.enabled {
      color: #059669;
    }

    &.disabled {
      color: #dc2626;
    }
  }
</style>
