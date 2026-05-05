<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑活动提交记录' : '新建活动提交记录'"
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
        <a-col :span="6">
          <a-form-item label="活动名称" name="activityName">
            <a-input v-model:value="form.activityName" placeholder="请输入活动名称"  />
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
          <a-form-item label="学号" name="studentNo">
            <a-input v-model:value="form.studentNo" placeholder="请输入学号"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="学生姓名" name="studentName">
            <a-input v-model:value="form.studentName" placeholder="请输入学生姓名"  />
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
        <a-col :span="6">
          <a-form-item label="班级名称" name="className">
            <a-input v-model:value="form.className" placeholder="请输入班级名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="提交时间" name="submitTime">
            <a-date-picker v-model:value="form.submitTime" show-time style="width: 100%" placeholder="请选择提交时间" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="IP地址" name="ipAddress">
            <a-input v-model:value="form.ipAddress" placeholder="请输入IP地址"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="设备类型" name="deviceType">
            <a-input v-model:value="form.deviceType" placeholder="请输入设备类型"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="批改状态: 0-待批改, 1-AI批改中, 2-已批改, 3-教师复核" name="gradingStatus">
            <a-select v-model:value="form.gradingStatus" placeholder="请选择批改状态: 0-待批改, 1-AI批改中, 2-已批改, 3-教师复核" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('grading_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="批改时间" name="gradingTime">
            <a-date-picker v-model:value="form.gradingTime" show-time style="width: 100%" placeholder="请选择批改时间" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="批改人ID" name="gradedBy">
            <a-select 
              v-model:value="form.gradedBy" 
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
        <a-col :span="6">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI总评语" name="aiFeedback">
            <a-textarea v-model:value="form.aiFeedback" :rows="3" placeholder="请输入AI总评语" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="教师总评语" name="teacherFeedback">
            <a-textarea v-model:value="form.teacherFeedback" :rows="3" placeholder="请输入教师总评语" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { classActivitySubmitApi } from '/@/api/smartmind/classActivitySubmit-api.js';
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
    activityName: undefined,
    studentId: undefined,
    studentNo: undefined,
    studentName: undefined,
    classId: undefined,
    className: undefined,
    submitTime: undefined,
    ipAddress: undefined,
    deviceType: undefined,
    gradingStatus: undefined,
    gradingTime: undefined,
    gradedBy: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    aiFeedback: undefined,
    teacherFeedback: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    activityId: [{ required: true, message: '请输入活动ID' }],
    studentId: [{ required: true, message: '请输入学生ID' }],
    classId: [{ required: true, message: '请输入班级ID' }],
    submitTime: [{ required: true, message: '请输入提交时间' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdClassActivityOptions = ref([]);
  const smdClassActivityLoading = ref(false);
  const smdStudentOptions = ref([]);
  const smdStudentLoading = ref(false);
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
        await classActivitySubmitApi.update(form);
        message.success('修改成功');
      } else {
        await classActivitySubmitApi.add(form);
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
