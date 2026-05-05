<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑题目统计' : '新建题目统计'"
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
          <a-form-item label="提交记录ID" name="submitId">
            <a-select 
              v-model:value="form.submitId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdClassActivitySubmit" 
              :loading="smdClassActivitySubmitLoading"
              @focus="remoteSelectSmdClassActivitySubmit('')"
              allowClear
            >
              <a-select-option v-for="item in smdClassActivitySubmitOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
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
      </a-row>
      <a-row>
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
        <a-col :span="12">
          <a-form-item label="是否修改过答案: 0-否, 1-是" name="optionChanged">
            <a-select v-model:value="form.optionChanged" placeholder="请选择是否修改过答案: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="错误类型" name="commonMistakeType">
            <a-input v-model:value="form.commonMistakeType" placeholder="请输入错误类型"  />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="统计时间" name="statisticsTime">
            <a-date-picker v-model:value="form.statisticsTime" show-time style="width: 100%" placeholder="请选择统计时间" />
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
          <a-form-item label="选择答案" name="answerOptions">
            <a-textarea v-model:value="form.answerOptions" :rows="3" placeholder="请输入选择答案" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="AI题目分析" name="aiAnalysis">
            <a-textarea v-model:value="form.aiAnalysis" :rows="3" placeholder="请输入AI题目分析" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { activityQuestionStatisticsApi } from '/@/api/smartmind/activityQuestionStatistics-api.js';
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
    submitId: undefined,
    activityId: undefined,
    questionId: undefined,
    optionChanged: undefined,
    commonMistakeType: undefined,
    statisticsTime: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    answerOptions: undefined,
    aiAnalysis: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    submitId: [{ required: true, message: '请输入提交记录ID' }],
    activityId: [{ required: true, message: '请输入活动ID' }],
    questionId: [{ required: true, message: '请输入题目ID' }],
    statisticsTime: [{ required: true, message: '请输入统计时间' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdClassActivitySubmitOptions = ref([]);
  const smdClassActivitySubmitLoading = ref(false);
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
        await activityQuestionStatisticsApi.update(form);
        message.success('修改成功');
      } else {
        await activityQuestionStatisticsApi.add(form);
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

  async function remoteSelectSmdClassActivitySubmit(keywords) {
    smdClassActivitySubmitLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdClassActivitySubmitApi.querySelectVo({ keywords });
      smdClassActivitySubmitOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdClassActivitySubmitLoading.value = false;
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
