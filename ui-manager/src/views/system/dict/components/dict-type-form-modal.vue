<template>
  <a-modal
    :title="form.id ? '编辑字典类型' : '新建字典类型'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="类型名称" name="dictName">
        <a-input v-model:value="form.dictName" placeholder="请输入类型名称" />
      </a-form-item>
      <a-form-item label="类型编码" name="dictType">
        <a-input v-model:value="form.dictType" placeholder="请输入类型编码" />
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
  import { dictApi } from '/@/api/system/dict-api.js';

  const emit = defineEmits(['reload']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: '',
    dictName: '',
    dictType: '',
    remark: '',
  });

  const rules = {
    dictName: [{ required: true, message: '请输入类型名称' }],
    dictType: [{ required: true, message: '请输入类型编码' }],
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
      dictName: '',
      dictType: '',
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
        await dictApi.updateType(form);
        message.success('更新成功');
      } else {
        await dictApi.addType(form);
        message.success('添加成功');
      }
      visible.value = false;
      emit('reload');
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
