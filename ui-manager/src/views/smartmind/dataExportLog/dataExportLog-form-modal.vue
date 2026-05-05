<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑数据导出日志' : '新建数据导出日志'"
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
          <a-form-item label="导出名称" name="exportName">
            <a-input v-model:value="form.exportName" placeholder="请输入导出名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="导出类型: 1-原始数据, 2-统计报表, 3-图表" name="exportType">
            <a-select v-model:value="form.exportType" placeholder="请选择导出类型: 1-原始数据, 2-统计报表, 3-图表" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('export_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁" name="dataType">
            <a-select v-model:value="form.dataType" placeholder="请选择数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('data_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
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
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="单元名称" name="unitName">
            <a-input v-model:value="form.unitName" placeholder="请输入单元名称"  />
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
          <a-form-item label="文件名" name="fileName">
            <a-input v-model:value="form.fileName" placeholder="请输入文件名"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="文件下载地址" name="fileUrl">
            <a-textarea v-model:value="form.fileUrl" :rows="3" placeholder="请输入文件下载地址" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="导出时间" name="exportTime">
            <a-date-picker v-model:value="form.exportTime" show-time style="width: 100%" placeholder="请选择导出时间" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="过期时间" name="expireTime">
            <a-date-picker v-model:value="form.expireTime" show-time style="width: 100%" placeholder="请选择过期时间" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态: 0-生成中, 1-已完成, 2-已过期" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-生成中, 1-已完成, 2-已过期" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('export_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
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
          <a-form-item label="导出参数" name="exportParams">
            <a-textarea v-model:value="form.exportParams" :rows="3" placeholder="请输入导出参数" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { dataExportLogApi } from '/@/api/smartmind/dataExportLog-api.js';
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
    exportName: undefined,
    exportType: undefined,
    dataType: undefined,
    courseId: undefined,
    courseName: undefined,
    classId: undefined,
    className: undefined,
    unitId: undefined,
    unitName: undefined,
    studentId: undefined,
    fileName: undefined,
    fileUrl: undefined,
    exportTime: undefined,
    expireTime: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    exportParams: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    exportName: [{ required: true, message: '请输入导出名称' }],
    exportType: [{ required: true, message: '请输入导出类型: 1-原始数据, 2-统计报表, 3-图表' }],
    dataType: [{ required: true, message: '请输入数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    exportTime: [{ required: true, message: '请输入导出时间' }],
    status: [{ required: true, message: '请输入状态: 0-生成中, 1-已完成, 2-已过期' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdClazzOptions = ref([]);
  const smdClazzLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);
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
        await dataExportLogApi.update(form);
        message.success('修改成功');
      } else {
        await dataExportLogApi.add(form);
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
