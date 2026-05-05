<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑学习单元' : '新建学习单元'"
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
          <a-form-item label="单元编码" name="unitCode">
            <a-input v-model:value="form.unitCode" placeholder="请输入单元编码"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="单元名称" name="unitName">
            <a-input v-model:value="form.unitName" placeholder="请输入单元名称"  />
          </a-form-item>
        </a-col>
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
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="父单元ID(0表示根单元)" name="parentId">
            <a-select 
              v-model:value="form.parentId" 
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
          <a-form-item label="单元层级: 1-大单元, 2-子单元, 3-课时" name="unitLevel">
            <a-select v-model:value="form.unitLevel" placeholder="请选择单元层级: 1-大单元, 2-子单元, 3-课时" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('unit_level')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="计划开始日期" name="startDate">
            <a-date-picker v-model:value="form.startDate" style="width: 100%" placeholder="请选择计划开始日期" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="计划结束日期" name="endDate">
            <a-date-picker v-model:value="form.endDate" style="width: 100%" placeholder="请选择计划结束日期" />
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
          <a-form-item label="单元描述" name="unitDesc">
            <a-textarea v-model:value="form.unitDesc" :rows="3" placeholder="请输入单元描述" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="单元目标(知识、能力、素养)" name="unitObjectives">
            <a-textarea v-model:value="form.unitObjectives" :rows="3" placeholder="请输入单元目标(知识、能力、素养)" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="重难点分析" name="keyPoints">
            <a-textarea v-model:value="form.keyPoints" :rows="3" placeholder="请输入重难点分析" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="知识点标签列表" name="knowledgeTags">
            <a-textarea v-model:value="form.knowledgeTags" :rows="3" placeholder="请输入知识点标签列表" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="课标对应关系" name="standardMapping">
            <a-textarea v-model:value="form.standardMapping" :rows="3" placeholder="请输入课标对应关系" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="前置单元ID列表" name="prerequisiteUnits">
            <a-textarea v-model:value="form.prerequisiteUnits" :rows="3" placeholder="请输入前置单元ID列表" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { learningUnitApi } from '/@/api/smartmind/learningUnit-api.js';
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
    unitCode: undefined,
    unitName: undefined,
    courseId: undefined,
    courseName: undefined,
    parentId: undefined,
    unitLevel: undefined,
    startDate: undefined,
    endDate: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    unitDesc: undefined,
    unitObjectives: undefined,
    keyPoints: undefined,
    knowledgeTags: undefined,
    standardMapping: undefined,
    prerequisiteUnits: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    unitCode: [{ required: true, message: '请输入单元编码' }],
    unitName: [{ required: true, message: '请输入单元名称' }],
    courseId: [{ required: true, message: '请输入所属课程ID' }],
    unitLevel: [{ required: true, message: '请输入单元层级: 1-大单元, 2-子单元, 3-课时' }],
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
        await learningUnitApi.update(form);
        message.success('修改成功');
      } else {
        await learningUnitApi.add(form);
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
