<template>
  <a-modal
    v-model:visible="visible"
    :title="form.id ? '编辑课件' : '新增课件'"
    @ok="handleSubmit"
    @cancel="handleCancel"
    width="600px"
    :confirmLoading="submitLoading"
  >
    <a-form :model="form" :rules="rules" ref="formRef" layout="vertical">
      <a-form-item label="课件编码" name="coursewareCode">
        <a-input v-model:value="form.coursewareCode" placeholder="请输入课件编码" />
      </a-form-item>

      <a-form-item label="课件名称" name="coursewareName">
        <a-input v-model:value="form.coursewareName" placeholder="请输入课件名称" />
      </a-form-item>

      <a-form-item label="课件类型" name="coursewareType">
        <a-select v-model:value="form.coursewareType" placeholder="请选择课件类型">
          <a-select-option
            v-for="item in dictStore.getData('courseware_type')"
            :key="item.dictValue"
            :value="parseInt(item.dictValue)"
          >
            {{ item.dictLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="所属课程">
        <a-input v-model:value="form.courseName" disabled />
      </a-form-item>

      <a-form-item label="所属单元">
        <a-input v-model:value="form.unitName" disabled />
      </a-form-item>

      <a-form-item label="排序序号" name="sortOrder">
        <a-input-number v-model:value="form.sortOrder" :min="0" style="width: 100%" />
      </a-form-item>

      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="form.status">
          <a-radio value="1">有效</a-radio>
          <a-radio value="0">失效</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { coursewareApi } from '/@/api/smartmind/courseware-api.js';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const submitLoading = ref(false);
  const formRef = ref(null);

  const form = reactive({
    id: null,
    coursewareCode: '',
    coursewareName: '',
    coursewareType: 1,
    courseId: null,
    courseName: '',
    unitId: null,
    unitName: '',
    unitNo: '',
    markdownContent: '',
    sortOrder: 0,
    status: '1',
    remark: '',
  });

  const rules = {
    coursewareCode: [{ required: true, message: '请输入课件编码', trigger: 'blur' }],
    coursewareName: [{ required: true, message: '请输入课件名称', trigger: 'blur' }],
    coursewareType: [{ required: true, message: '请选择课件类型', trigger: 'change' }],
  };

  // 显示模态框
  function showModal(data = null) {
    visible.value = true;
    resetForm();

    if (data) {
      // 编辑模式或带默认值的新增
      Object.assign(form, {
        courseId: data.courseId,
        courseName: data.courseName,
        unitId: data.unitId,
        unitName: data.unitName,
        unitNo: data.unitNo,
      });

      if (data.id) {
        // 编辑模式
        Object.assign(form, data);
      } else {
        // 新增模式，生成默认编码
        form.coursewareCode = generateCode(data.unitNo);
        form.coursewareName = `${data.unitName} - 课件`;
      }
    }
  }

  // 生成编码
  function generateCode(unitNo) {
    const date = new Date();
    const timestamp = date.getFullYear().toString().slice(-2) +
      String(date.getMonth() + 1).padStart(2, '0') +
      String(date.getDate()).padStart(2, '0');
    return `CW${unitNo || ''}${timestamp}${Math.floor(Math.random() * 1000).toString().padStart(3, '0')}`;
  }

  // 重置表单
  function resetForm() {
    form.id = null;
    form.coursewareCode = '';
    form.coursewareName = '';
    form.coursewareType = 1;
    form.courseId = null;
    form.courseName = '';
    form.unitId = null;
    form.unitName = '';
    form.unitNo = '';
    form.markdownContent = '';
    form.sortOrder = 0;
    form.status = '1';
    form.remark = '';
  }

  // 提交表单
  async function handleSubmit() {
    try {
      await formRef.value.validate();
      submitLoading.value = true;

      if (form.id) {
        await coursewareApi.update(form);
        message.success('更新成功');
      } else {
        await coursewareApi.add(form);
        message.success('添加成功');
      }

      visible.value = false;
      emit('reloadList');
    } catch (e) {
      if (e.errorFields) {
        // 表单验证错误
        return;
      }
      smartSentry.captureError(e);
      message.error(e.message || '操作失败');
    } finally {
      submitLoading.value = false;
    }
  }

  // 取消
  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  defineExpose({
    showModal,
  });
</script>
