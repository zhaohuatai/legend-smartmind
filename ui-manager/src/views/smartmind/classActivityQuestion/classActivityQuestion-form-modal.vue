<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑活动题目关联' : '新建活动题目关联'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="680px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row>
        <a-col :span="12">
          <a-form-item label="活动ID" name="activityId">
            <a-select 
              v-model:value="form.activityId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdClassActivity" 
              :loading="smdClassActivityLoading"
              @focus="remoteSelectSmdClassActivity('')"
              allowClear
            >
              <a-select-option v-for="item in smdClassActivityOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="题目ID" name="questionId">
            <a-select 
              v-model:value="form.questionId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdQuestionCategory" 
              :loading="smdQuestionCategoryLoading"
              @focus="remoteSelectSmdQuestionCategory('')"
              allowClear
            >
              <a-select-option v-for="item in smdQuestionCategoryOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="题目类型" name="questionType">
            <a-select v-model:value="form.questionType" placeholder="请选择题目类型" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('question_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="题目内容(冗余存储)" name="questionContent">
            <a-textarea v-model:value="form.questionContent" :rows="3" placeholder="请输入题目内容(冗余存储)" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { classActivityQuestionApi } from '/@/api/smartmind/classActivityQuestion-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const formDefault = {
    id: undefined,
    activityId: undefined,
    questionId: undefined,
    questionType: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    questionContent: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    activityId: [{ required: true, message: '请输入活动ID' }],
    questionId: [{ required: true, message: '请输入题目ID' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdClassActivityOptions = ref([]);
  const smdClassActivityLoading = ref(false);
  const smdQuestionCategoryOptions = ref([]);
  const smdQuestionCategoryLoading = ref(false);

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
        await classActivityQuestionApi.update(form);
        message.success('修改成功');
      } else {
        await classActivityQuestionApi.add(form);
        message.success('添加成功');
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

  async function remoteSelectSmdClassActivity(keywords) {
    smdClassActivityLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdClassActivityApi.querySelectVo({ keywords });
      smdClassActivityOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdClassActivityLoading.value = false;
    }
  }
  async function remoteSelectSmdQuestionCategory(keywords) {
    smdQuestionCategoryLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdQuestionCategoryApi.querySelectVo({ keywords });
      smdQuestionCategoryOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdQuestionCategoryLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
