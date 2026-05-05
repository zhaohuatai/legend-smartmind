<template>
  <a-modal
    :title="form.id ? '编辑权限' : '新建权限'"
    :width="600"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="权限名称" name="name">
        <a-input v-model:value="form.name" placeholder="请输入权限名称" />
      </a-form-item>
      <a-form-item label="权限编码" name="code">
        <a-input v-model:value="form.code" placeholder="请输入权限编码" />
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
  import { permissionApi } from '/@/api/system/permission-api.js';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: '',
    name: '',
    code: '',
    status: '1',
    remark: '',
  });

  const rules = {
    name: [{ required: true, message: '请输入权限名称' }],
    code: [{ required: true, message: '请输入权限编码' }],
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
      code: '',
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
        await permissionApi.update(form);
        message.success('更新成功');
      } else {
        await permissionApi.add(form);
        message.success('添加成功');
      }
      visible.value = false;
      emit('reloadList');
    } catch (e) {
      // console.error(e);
    } finally {
      confirmLoading.value = false;
    }
  }

  defineExpose({
    show,
  });
</script>
