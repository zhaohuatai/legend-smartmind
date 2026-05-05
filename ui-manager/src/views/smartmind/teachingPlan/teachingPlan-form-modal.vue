<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑教案' : '新建教案'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="1360px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row>
        <a-col :span="6">
          <a-form-item label="教案编码" name="planCode">
            <a-input v-model:value="form.planCode" placeholder="请输入教案编码"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="教案名称" name="planName">
            <a-input v-model:value="form.planName" placeholder="请输入教案名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="课程ID" name="courseId">
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
        <a-col :span="6">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="单元ID" name="unitId">
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
        <a-col :span="6">
          <a-form-item label="单元名称" name="unitName">
            <a-input v-model:value="form.unitName" placeholder="请输入单元名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="课时主题" name="lessonTitle">
            <a-input v-model:value="form.lessonTitle" placeholder="请输入课时主题"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="设计教师ID" name="teacherId">
            <a-select 
              v-model:value="form.teacherId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdTeacher" 
              :loading="smdTeacherLoading"
              @focus="remoteSelectSmdTeacher('')"
              allowClear
            >
              <a-select-option v-for="item in smdTeacherOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="教师姓名" name="teacherName">
            <a-input v-model:value="form.teacherName" placeholder="请输入教师姓名"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI生成模式: 0-手动创建, 1-AI辅助生成, 2-AI自动推荐" name="aiGenerateMode">
            <a-select v-model:value="form.aiGenerateMode" placeholder="请选择AI生成模式: 0-手动创建, 1-AI辅助生成, 2-AI自动推荐" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('ai_generate_mode')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态: 0-草稿, 1-已完成, 2-已发布" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-草稿, 1-已完成, 2-已发布" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('plan_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="教学目标(知识/能力/素养)" name="teachingObjectives">
            <a-textarea v-model:value="form.teachingObjectives" :rows="3" placeholder="请输入教学目标(知识/能力/素养)" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="教学重难点" name="keyPoints">
            <a-textarea v-model:value="form.keyPoints" :rows="3" placeholder="请输入教学重难点" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="教学方法" name="teachingMethods">
            <a-textarea v-model:value="form.teachingMethods" :rows="3" placeholder="请输入教学方法" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="教学工具/媒体" name="teachingTools">
            <a-textarea v-model:value="form.teachingTools" :rows="3" placeholder="请输入教学工具/媒体" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI生成提示词" name="aiPrompt">
            <a-textarea v-model:value="form.aiPrompt" :rows="3" placeholder="请输入AI生成提示词" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI优化建议" name="aiSuggestions">
            <a-textarea v-model:value="form.aiSuggestions" :rows="3" placeholder="请输入AI优化建议" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { teachingPlanApi } from '/@/api/smartmind/teachingPlan-api.js';
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
    planCode: undefined,
    planName: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    lessonTitle: undefined,
    teacherId: undefined,
    teacherName: undefined,
    aiGenerateMode: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    teachingObjectives: undefined,
    keyPoints: undefined,
    teachingMethods: undefined,
    teachingTools: undefined,
    aiPrompt: undefined,
    aiSuggestions: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    planCode: [{ required: true, message: '请输入教案编码' }],
    planName: [{ required: true, message: '请输入教案名称' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    unitId: [{ required: true, message: '请输入单元ID' }],
    lessonTitle: [{ required: true, message: '请输入课时主题' }],
    teacherId: [{ required: true, message: '请输入设计教师ID' }],
    status: [{ required: true, message: '请输入状态: 0-草稿, 1-已完成, 2-已发布' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);
  const smdTeacherOptions = ref([]);
  const smdTeacherLoading = ref(false);

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
        await teachingPlanApi.update(form);
        message.success('修改成功');
      } else {
        await teachingPlanApi.add(form);
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
  async function remoteSelectSmdTeacher(keywords) {
    smdTeacherLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdTeacherApi.querySelectVo({ keywords });
      smdTeacherOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdTeacherLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
