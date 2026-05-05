<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑试卷题目关联' : '新建试卷题目关联'"
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
          <a-form-item label="试卷ID" name="paperId">
            <a-select 
              v-model:value="form.paperId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdExamPaper" 
              :loading="smdExamPaperLoading"
              @focus="remoteSelectSmdExamPaper('')"
              allowClear
            >
              <a-select-option v-for="item in smdExamPaperOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
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
              @search="remoteSelectSmdQuestionBank" 
              :loading="smdQuestionBankLoading"
              @focus="remoteSelectSmdQuestionBank('')"
              allowClear
            >
              <a-select-option v-for="item in smdQuestionBankOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
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
          <a-form-item label="所属板块" name="sectionName">
            <a-input v-model:value="form.sectionName" placeholder="请输入所属板块"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
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
  import { examPaperQuestionApi } from '/@/api/smartmind/examPaperQuestion-api.js';
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
    paperId: undefined,
    questionId: undefined,
    questionType: undefined,
    sectionName: undefined,
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
    paperId: [{ required: true, message: '请输入试卷ID' }],
    questionId: [{ required: true, message: '请输入题目ID' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdExamPaperOptions = ref([]);
  const smdExamPaperLoading = ref(false);
  const smdQuestionBankOptions = ref([]);
  const smdQuestionBankLoading = ref(false);

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
        await examPaperQuestionApi.update(form);
        message.success('修改成功');
      } else {
        await examPaperQuestionApi.add(form);
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

  async function remoteSelectSmdExamPaper(keywords) {
    smdExamPaperLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdExamPaperApi.querySelectVo({ keywords });
      smdExamPaperOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdExamPaperLoading.value = false;
    }
  }
  async function remoteSelectSmdQuestionBank(keywords) {
    smdQuestionBankLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdQuestionBankApi.querySelectVo({ keywords });
      smdQuestionBankOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdQuestionBankLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
