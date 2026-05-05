<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑教学文件' : '新建教学文件'"
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
          <a-form-item label="文件名称" name="fileName">
            <a-input v-model:value="form.fileName" placeholder="请输入文件名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他" name="fileType">
            <a-select v-model:value="form.fileType" placeholder="请选择文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('file_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="文件地址" name="fileUrl">
            <a-textarea v-model:value="form.fileUrl" :rows="3" placeholder="请输入文件地址" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="关联课程ID" name="courseId">
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
          <a-form-item label="关联单元ID" name="unitId">
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
          <a-form-item label="上传类型: 1-本地上传, 2-资源库引用" name="uploadType">
            <a-select v-model:value="form.uploadType" placeholder="请选择上传类型: 1-本地上传, 2-资源库引用" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('upload_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="状态: 0-失效, 1-有效" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-失效, 1-有效" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
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
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="AI提取的内容文本" name="aiExtractedContent">
            <a-textarea v-model:value="form.aiExtractedContent" :rows="3" placeholder="请输入AI提取的内容文本" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { teachingFileApi } from '/@/api/smartmind/teachingFile-api.js';
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
    fileName: undefined,
    fileType: undefined,
    fileUrl: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    uploadType: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    aiExtractedContent: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    fileName: [{ required: true, message: '请输入文件名称' }],
    fileType: [{ required: true, message: '请输入文件类型: 1-PPT, 2-PDF, 3-Word, 4-图片, 5-视频, 6-音频, 9-其他' }],
    fileUrl: [{ required: true, message: '请输入文件地址' }],
    status: [{ required: true, message: '请输入状态: 0-失效, 1-有效' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

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
        await teachingFileApi.update(form);
        message.success('修改成功');
      } else {
        await teachingFileApi.add(form);
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

  defineExpose({
    showModal,
  });
</script>
