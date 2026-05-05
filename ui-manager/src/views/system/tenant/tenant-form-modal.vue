<template>
  <a-modal
    :title="form.id ? '编辑租户' : '新建租户'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="租户名称" name="name">
        <a-input v-model:value="form.name" placeholder="请输入租户名称" />
      </a-form-item>
      <a-form-item label="联系人" name="contact">
        <a-input v-model:value="form.contact" placeholder="请输入联系人" />
      </a-form-item>
      <a-form-item label="联系电话" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入联系电话" />
      </a-form-item>
      <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="form.status">
              <a-radio value="1">正常</a-radio>
              <a-radio value="0">禁用</a-radio>
          </a-radio-group>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { tenantApi } from '/@/api/system/tenant-api.js';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: '',
    name: '',
    contact: '',
    phone: '',
    status: '1',
    remark: '',
  });

  const rules = {
    name: [{ required: true, message: '请输入租户名称' }],
    status: [{ required: true, message: '请选择状态' }],
  };

  function show(record) {
    visible.value = true;
    if (record) {
      nextTick(() => {
        Object.assign(form, record);
      });
    } else {
      reset();
    }
  }

  function onClose() {
    visible.value = false;
    reset();
  }

  function reset() {
    Object.assign(form, {
      id: '',
      name: '',
      contact: '',
      phone: '',
      status: '1',
      remark: '',
    });
    if (formRef.value) {
        formRef.value.resetFields();
    }
  }

  async function onSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      if (form.id) {
        await tenantApi.update(form);
        message.success('更新成功');
      } else {
        await tenantApi.add(form);
        message.success('添加成功');
      }
      visible.value = false;
      emit('reloadList');
    } catch (e) {
      // ignore
    } finally {
      confirmLoading.value = false;
    }
  }

  defineExpose({
    show,
  });
</script>
