<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑学情报告' : '新建学情报告'"
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
          <a-form-item label="报告名称" name="reportName">
            <a-input v-model:value="form.reportName" placeholder="请输入报告名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告" name="reportType">
            <a-select v-model:value="form.reportType" placeholder="请选择报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('report_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="活动ID: 单活动/作业报告时关联" name="activityId">
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
        <a-col :span="6">
          <a-form-item label="活动名称" name="activityName">
            <a-input v-model:value="form.activityName" placeholder="请输入活动名称"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
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
      </a-row>
      <a-row>
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
        <a-col :span="6">
          <a-form-item label="班级名称" name="className">
            <a-input v-model:value="form.className" placeholder="请输入班级名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
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
        <a-col :span="6">
          <a-form-item label="学生姓名" name="studentName">
            <a-input v-model:value="form.studentName" placeholder="请输入学生姓名"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="统计开始日期" name="startDate">
            <a-date-picker v-model:value="form.startDate" style="width: 100%" placeholder="请选择统计开始日期" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="统计结束日期" name="endDate">
            <a-date-picker v-model:value="form.endDate" style="width: 100%" placeholder="请选择统计结束日期" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="生成时间" name="generateTime">
            <a-date-picker v-model:value="form.generateTime" show-time style="width: 100%" placeholder="请选择生成时间" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态: 0-草稿, 1-已生成" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-草稿, 1-已生成" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
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
          <a-form-item label="优势知识点" name="strengthPoints">
            <a-textarea v-model:value="form.strengthPoints" :rows="3" placeholder="请输入优势知识点" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="薄弱知识点" name="weaknessPoints">
            <a-textarea v-model:value="form.weaknessPoints" :rows="3" placeholder="请输入薄弱知识点" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="改进建议" name="improvementSuggestions">
            <a-textarea v-model:value="form.improvementSuggestions" :rows="3" placeholder="请输入改进建议" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="学习趋势数据" name="learningTrend">
            <a-textarea v-model:value="form.learningTrend" :rows="3" placeholder="请输入学习趋势数据" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="活动详情数据" name="activityDetails">
            <a-textarea v-model:value="form.activityDetails" :rows="3" placeholder="请输入活动详情数据" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="AI总结评语" name="aiSummary">
            <a-textarea v-model:value="form.aiSummary" :rows="3" placeholder="请输入AI总结评语" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { learningReportApi } from '/@/api/smartmind/learningReport-api.js';
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
    reportName: undefined,
    reportType: undefined,
    activityId: undefined,
    activityName: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    classId: undefined,
    className: undefined,
    studentId: undefined,
    studentName: undefined,
    startDate: undefined,
    endDate: undefined,
    generateTime: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    strengthPoints: undefined,
    weaknessPoints: undefined,
    improvementSuggestions: undefined,
    learningTrend: undefined,
    activityDetails: undefined,
    aiSummary: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    reportName: [{ required: true, message: '请输入报告名称' }],
    reportType: [{ required: true, message: '请输入报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    startDate: [{ required: true, message: '请输入统计开始日期' }],
    endDate: [{ required: true, message: '请输入统计结束日期' }],
    generateTime: [{ required: true, message: '请输入生成时间' }],
    status: [{ required: true, message: '请输入状态: 0-草稿, 1-已生成' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdClassActivityOptions = ref([]);
  const smdClassActivityLoading = ref(false);
  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);
  const smdClazzOptions = ref([]);
  const smdClazzLoading = ref(false);
  const smdStudentOptions = ref([]);
  const smdStudentLoading = ref(false);

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
        await learningReportApi.update(form);
        message.success('修改成功');
      } else {
        await learningReportApi.add(form);
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

  defineExpose({
    showModal,
  });
</script>
