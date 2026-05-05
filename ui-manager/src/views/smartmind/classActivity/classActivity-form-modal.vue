<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑课堂活动' : '新建课堂活动'"
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
          <a-form-item label="活动编码" name="activityCode">
            <a-input v-model:value="form.activityCode" placeholder="请输入活动编码"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="活动名称" name="activityName">
            <a-input v-model:value="form.activityName" placeholder="请输入活动名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="活动类型: 1-课后作业, 2-随堂测, 3-课堂练习, 4-互动问答, 5-小组讨论, 6-课堂投票, 9-其他" name="activityType">
            <a-select v-model:value="form.activityType" placeholder="请选择活动类型: 1-课后作业, 2-随堂测, 3-课堂练习, 4-互动问答, 5-小组讨论, 6-课堂投票, 9-其他" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('activity_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
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
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
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
          <a-form-item label="班级ID" name="classId">
            <a-select 
              v-model:value="form.classId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdClazz" 
              :loading="smdClazzLoading"
              @focus="remoteSelectSmdClazz('')"
              allowClear
            >
              <a-select-option v-for="item in smdClazzOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="班级名称" name="className">
            <a-input v-model:value="form.className" placeholder="请输入班级名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="发布教师ID" name="teacherId">
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
        <a-col :span="6">
          <a-form-item label="教师姓名" name="teacherName">
            <a-input v-model:value="form.teacherName" placeholder="请输入教师姓名"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="开始时间" name="startTime">
            <a-date-picker v-model:value="form.startTime" show-time style="width: 100%" placeholder="请选择开始时间" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="结束时间" name="endTime">
            <a-date-picker v-model:value="form.endTime" show-time style="width: 100%" placeholder="请选择结束时间" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="允许重试: 0-否, 1-是" name="allowRetry">
            <a-select v-model:value="form.allowRetry" placeholder="请选择允许重试: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="提交后显示答案: 0-否, 1-是" name="showAnswerAfterSubmit">
            <a-select v-model:value="form.showAnswerAfterSubmit" placeholder="请选择提交后显示答案: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="截止后显示答案: 0-否, 1-是" name="showAnswerAfterDeadline">
            <a-select v-model:value="form.showAnswerAfterDeadline" placeholder="请选择截止后显示答案: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="启用AI分析: 0-否, 1-是" name="aiAnalysisEnabled">
            <a-select v-model:value="form.aiAnalysisEnabled" placeholder="请选择启用AI分析: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="活动状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消" name="activityStatus">
            <a-select v-model:value="form.activityStatus" placeholder="请选择活动状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('activity_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
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
          <a-form-item label="AI分析结果摘要" name="aiAnalysisResult">
            <a-textarea v-model:value="form.aiAnalysisResult" :rows="3" placeholder="请输入AI分析结果摘要" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { classActivityApi } from '/@/api/smartmind/classActivity-api.js';
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
    activityCode: undefined,
    activityName: undefined,
    activityType: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    classId: undefined,
    className: undefined,
    teacherId: undefined,
    teacherName: undefined,
    startTime: undefined,
    endTime: undefined,
    allowRetry: undefined,
    showAnswerAfterSubmit: undefined,
    showAnswerAfterDeadline: undefined,
    aiAnalysisEnabled: undefined,
    activityStatus: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    aiAnalysisResult: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    activityCode: [{ required: true, message: '请输入活动编码' }],
    activityName: [{ required: true, message: '请输入活动名称' }],
    activityType: [{ required: true, message: '请输入活动类型: 1-课后作业, 2-随堂测, 3-课堂练习, 4-互动问答, 5-小组讨论, 6-课堂投票, 9-其他' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    unitId: [{ required: true, message: '请输入单元ID' }],
    classId: [{ required: true, message: '请输入班级ID' }],
    teacherId: [{ required: true, message: '请输入发布教师ID' }],
    startTime: [{ required: true, message: '请输入开始时间' }],
    activityStatus: [{ required: true, message: '请输入活动状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);
  const smdClazzOptions = ref([]);
  const smdClazzLoading = ref(false);
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
        await classActivityApi.update(form);
        message.success('修改成功');
      } else {
        await classActivityApi.add(form);
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
  async function remoteSelectSmdClazz(keywords) {
    smdClazzLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdClazzApi.querySelectVo({ keywords });
      smdClazzOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdClazzLoading.value = false;
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
