<template>
  <a-modal
    :title="form.id ? '编辑字典数据' : '新建字典数据'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form :model="form" :rules="rules" ref="formRef" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="标签" name="dictLabel">
        <a-input v-model:value="form.dictLabel" placeholder="请输入标签" />
      </a-form-item>
      <a-form-item label="键值" name="dictValue">
        <a-input v-model:value="form.dictValue" placeholder="请输入键值" />
      </a-form-item>
      <a-form-item label="排序" name="dictSort">
        <a-input-number v-model:value="form.dictSort" :min="0" />
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
  import { dictApi } from '/@/api/system/dict-api.js';

  const emit = defineEmits(['reload']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const typeCode = ref('');

  const form = reactive({
    id: '',
    dictLabel: '',
    dictValue: '',
    dictSort: 1,
    status: '1',
    remark: '',
  });

  const rules = {
    dictLabel: [{ required: true, message: '请输入标签' }],
    dictValue: [{ required: true, message: '请输入键值' }],
    status: [{ required: true, message: '请选择状态' }],
  };

  function show(record, tCode) {
    visible.value = true;
    typeCode.value = tCode;
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
      dictLabel: '',
      dictValue: '',
      dictSort: 1,
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
      const params = { ...form, dictType: typeCode.value };
      if (form.id) {
        await dictApi.updateData(params);
        message.success('更新成功');
      } else {
        await dictApi.addData(params);
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
