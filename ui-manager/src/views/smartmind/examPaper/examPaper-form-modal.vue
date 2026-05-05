<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑试卷' : '新建试卷'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="1020px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row>
        <a-col :span="8">
          <a-form-item label="试卷编码" name="paperCode">
            <a-input v-model:value="form.paperCode" placeholder="请输入试卷编码"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="试卷名称" name="paperName">
            <a-input v-model:value="form.paperName" placeholder="请输入试卷名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他" name="paperType">
            <a-select v-model:value="form.paperType" placeholder="请选择试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('paper_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="所属课程ID" name="courseId">
            <a-select 
              v-model:value="form.courseId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdCourse" 
              :loading="smdCourseLoading"
              @focus="remoteSelectSmdCourse('')"
              allowClear
            >
              <a-select-option v-for="item in smdCourseOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="所属单元ID" name="unitId">
            <a-select 
              v-model:value="form.unitId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdLearningUnit" 
              :loading="smdLearningUnitLoading"
              @focus="remoteSelectSmdLearningUnit('')"
              allowClear
            >
              <a-select-option v-for="item in smdLearningUnitOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="单元名称" name="unitName">
            <a-input v-model:value="form.unitName" placeholder="请输入单元名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="关联教案ID" name="planId">
            <a-select 
              v-model:value="form.planId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdTeachingPlan" 
              :loading="smdTeachingPlanLoading"
              @focus="remoteSelectSmdTeachingPlan('')"
              allowClear
            >
              <a-select-option v-for="item in smdTeachingPlanOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="AI生成模式" name="aiGenerateMode">
            <a-select v-model:value="form.aiGenerateMode" placeholder="请选择AI生成模式" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('ai_generate_mode')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="来源类型" name="sourceType">
            <a-select v-model:value="form.sourceType" placeholder="请选择来源类型" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('source_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="状态: 0-草稿, 1-已发布, 2-已归档" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-草稿, 1-已发布, 2-已归档" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('paper_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="难度分布设置" name="difficultyDistribution">
            <a-textarea v-model:value="form.difficultyDistribution" :rows="3" placeholder="请输入难度分布设置" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="知识点覆盖范围" name="knowledgeCoverage">
            <a-textarea v-model:value="form.knowledgeCoverage" :rows="3" placeholder="请输入知识点覆盖范围" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="AI组卷提示词" name="aiPrompt">
            <a-textarea v-model:value="form.aiPrompt" :rows="3" placeholder="请输入AI组卷提示词" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="AI试卷评估" name="aiEvaluation">
            <a-textarea v-model:value="form.aiEvaluation" :rows="3" placeholder="请输入AI试卷评估" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { examPaperApi } from '/@/api/smartmind/examPaper-api.js';
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
    paperCode: undefined,
    paperName: undefined,
    paperType: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    planId: undefined,
    aiGenerateMode: undefined,
    sourceType: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    difficultyDistribution: undefined,
    knowledgeCoverage: undefined,
    aiPrompt: undefined,
    aiEvaluation: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    paperCode: [{ required: true, message: '请输入试卷编码' }],
    paperName: [{ required: true, message: '请输入试卷名称' }],
    paperType: [{ required: true, message: '请输入试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他' }],
    courseId: [{ required: true, message: '请输入所属课程ID' }],
    status: [{ required: true, message: '请输入状态: 0-草稿, 1-已发布, 2-已归档' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);
  const smdTeachingPlanOptions = ref([]);
  const smdTeachingPlanLoading = ref(false);

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
        await examPaperApi.update(form);
        message.success('修改成功');
      } else {
        await examPaperApi.add(form);
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

  async function remoteSelectSmdCourse(keywords) {
    smdCourseLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdCourseApi.querySelectVo({ keywords });
      smdCourseOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdCourseLoading.value = false;
    }
  }
  async function remoteSelectSmdLearningUnit(keywords) {
    smdLearningUnitLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdLearningUnitApi.querySelectVo({ keywords });
      smdLearningUnitOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdLearningUnitLoading.value = false;
    }
  }
  async function remoteSelectSmdTeachingPlan(keywords) {
    smdTeachingPlanLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdTeachingPlanApi.querySelectVo({ keywords });
      smdTeachingPlanOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdTeachingPlanLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
