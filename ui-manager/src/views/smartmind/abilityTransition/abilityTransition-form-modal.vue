<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑能力跃迁' : '新建能力跃迁'"
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
          <a-form-item label="学生ID" name="studentId">
            <a-select 
              v-model:value="form.studentId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdStudent" 
              :loading="smdStudentLoading"
              @focus="remoteSelectSmdStudent('')"
              allowClear
            >
              <a-select-option v-for="item in smdStudentOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="学号" name="studentNo">
            <a-input v-model:value="form.studentNo" placeholder="请输入学号"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="学生姓名" name="studentName">
            <a-input v-model:value="form.studentName" placeholder="请输入学生姓名"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
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
        <a-col :span="8">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="起始单元ID" name="fromUnitId">
            <a-select 
              v-model:value="form.fromUnitId" 
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
          <a-form-item label="起始单元名称" name="fromUnitName">
            <a-input v-model:value="form.fromUnitName" placeholder="请输入起始单元名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="目标单元ID" name="toUnitId">
            <a-select 
              v-model:value="form.toUnitId" 
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
        <a-col :span="8">
          <a-form-item label="目标单元名称" name="toUnitName">
            <a-input v-model:value="form.toUnitName" placeholder="请输入目标单元名称"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="分析时间" name="analysisTime">
            <a-date-picker v-model:value="form.analysisTime" show-time style="width: 100%" placeholder="请选择分析时间" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
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
          <a-form-item label="能力维度变化" name="abilityDimensions">
            <a-textarea v-model:value="form.abilityDimensions" :rows="3" placeholder="请输入能力维度变化" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="可视化数据" name="visualizationData">
            <a-textarea v-model:value="form.visualizationData" :rows="3" placeholder="请输入可视化数据" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="AI分析评语" name="aiAnalysis">
            <a-textarea v-model:value="form.aiAnalysis" :rows="3" placeholder="请输入AI分析评语" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { abilityTransitionApi } from '/@/api/smartmind/abilityTransition-api.js';
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
    studentId: undefined,
    studentNo: undefined,
    studentName: undefined,
    courseId: undefined,
    courseName: undefined,
    fromUnitId: undefined,
    fromUnitName: undefined,
    toUnitId: undefined,
    toUnitName: undefined,
    analysisTime: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    abilityDimensions: undefined,
    visualizationData: undefined,
    aiAnalysis: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    studentId: [{ required: true, message: '请输入学生ID' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    fromUnitId: [{ required: true, message: '请输入起始单元ID' }],
    toUnitId: [{ required: true, message: '请输入目标单元ID' }],
    analysisTime: [{ required: true, message: '请输入分析时间' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdStudentOptions = ref([]);
  const smdStudentLoading = ref(false);
  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);

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
        await abilityTransitionApi.update(form);
        message.success('修改成功');
      } else {
        await abilityTransitionApi.add(form);
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

  async function remoteSelectSmdStudent(keywords) {
    smdStudentLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdStudentApi.querySelectVo({ keywords });
      smdStudentOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdStudentLoading.value = false;
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

  defineExpose({
    showModal,
  });
</script>
