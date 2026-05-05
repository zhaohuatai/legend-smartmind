<template>
  <a-modal
    v-model:open="visible"
    :title="form.id ? '编辑课程' : '新建课程'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="600px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <!-- 课程名称 -->
      <a-form-item label="课程名称" name="courseName">
        <a-input
          v-model:value="form.courseName"
          placeholder="请输入课程名称"
          :maxLength="100"
          show-count
          size="large"
        >
          <template #prefix>
            <BookOutlined style="color: #667eea" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 学期 -->
      <a-form-item label="学期" name="semester">
        <a-select
          v-model:value="form.semester"
          placeholder="请选择学期"
          allowClear
          size="large"
          :prefix="clockIcon"
        >
          <a-select-option
            v-for="item in dictStore.getDictData('semester')"
            :key="item.dataValue"
            :value="item.dataValue"
          >
            {{ item.dataLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <!-- 学科类型（暂时隐藏）
      <a-form-item label="学科类型" name="subjectType">
        <a-select
          v-model:value="form.subjectType"
          placeholder="请选择学科"
          allowClear
          size="large"
          :prefix="tagIcon"
        >
          <a-select-option
            v-for="item in dictStore.getDictData('subject_type')"
            :key="item.dataValue"
            :value="item.dataValue"
          >
            {{ item.dataLabel }}
          </a-select-option>
        </a-select>
      </a-form-item>
      -->

      <!-- 关联班级 -->
      <a-form-item label="关联班级">
        <div class="class-select-section">
          <div class="grade-filter">
            <span class="filter-label">年级筛选：</span>
            <a-select
              v-model:value="selectedGrade"
              placeholder="全部年级"
              allowClear
              style="width: 140px"
              @change="onGradeChange"
            >
              <a-select-option v-for="item in dictStore.getDictData('grade_level')" :key="item.dataValue" :value="item.dataValue">
                {{ item.dataLabel }}
              </a-select-option>
            </a-select>
            <span class="class-count" v-if="filteredClassOptions.length > 0">共 {{ filteredClassOptions.length }} 个班级</span>
          </div>
          <div class="class-config-list">
            <div 
              v-for="cls in filteredClassOptions" 
              :key="cls.value" 
              class="class-config-item"
              :class="{ selected: selectedClassIds.includes(cls.value) }"
            >
              <div class="class-header">
                <a-checkbox 
                  :checked="selectedClassIds.includes(cls.value)"
                  @change="(e) => toggleClass(cls.value, e.target.checked)"
                >
                  {{ cls.label }}
                </a-checkbox>
              </div>
            </div>
          </div>
          <div class="selected-count" v-if="selectedClassIds.length > 0">
            已选择 {{ selectedClassIds.length }} 个班级
          </div>
        </div>
      </a-form-item>

      <!-- 状态 -->
      <a-form-item label="课程状态" name="status">
        <a-radio-group v-model:value="form.status" button-style="solid" size="large">
          <a-radio-button value="0">草稿</a-radio-button>
          <a-radio-button value="1">已发布</a-radio-button>
          <a-radio-button value="2">已归档</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <!-- 课程描述 -->
      <a-form-item label="课程描述" name="courseDesc">
        <a-textarea
          v-model:value="form.courseDesc"
          placeholder="请输入课程描述（选填）"
          :rows="5"
          :maxLength="500"
          show-count
          size="large"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick, h } from 'vue';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { classCourseApi } from '/@/api/smartmind/class-course-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    BookOutlined,
    CalendarOutlined,
    ClockCircleOutlined,
    TeamOutlined,
    TagOutlined
  } from '@ant-design/icons-vue';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  // 图标渲染函数
  const calendarIcon = () => h(CalendarOutlined, { style: { color: '#667eea' } });
  const clockIcon = () => h(ClockCircleOutlined, { style: { color: '#667eea' } });
  const teamIcon = () => h(TeamOutlined, { style: { color: '#667eea' } });
  const tagIcon = () => h(TagOutlined, { style: { color: '#667eea' } });

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  
  // 班级相关
  const classOptions = ref([]);
  const filteredClassOptions = ref([]);
  const selectedClassIds = ref([]);
  const selectedGrade = ref(undefined);
  const classLoading = ref(false);

  const formDefault = {
    id: undefined,
    courseCode: undefined,
    courseName: undefined,
    subjectType: undefined,
    gradeLevel: undefined,
    semester: undefined,
    schoolYear: undefined,
    textbookInfo: undefined,
    coverImage: undefined,
    status: '0',
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    courseDesc: undefined,
    courseObjectives: undefined,
    knowledgeFramework: undefined,
    referenceMaterials: undefined,
    className: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    courseName: [
      { required: true, message: '请输入课程名称' },
      { max: 100, message: '课程名称最多100个字符' }
    ],
    semester: [{ required: true, message: '请选择学期' }],
    // subjectType: [{ required: true, message: '请选择学科' }],
    status: [{ required: true, message: '请选择状态' }],
  };

  // 加载班级列表
  async function loadClassOptions() {
    classLoading.value = true;
    try {
      const res = await clazzApi.selectVo();
      classOptions.value = res.data || [];
      filteredClassOptions.value = [...classOptions.value];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      classLoading.value = false;
    }
  }

  // 年级改变
  async function onGradeChange(grade) {
    selectedGrade.value = grade;
    if (grade) {
      try {
        const res = await clazzApi.queryByGrade(grade);
        const rows = res.data?.rows || res.data?.list || [];
        filteredClassOptions.value = rows.map(item => ({
          value: item.id,
          label: item.className,
        }));
      } catch (e) {
        smartSentry.captureError(e);
      }
    } else {
      filteredClassOptions.value = [...classOptions.value];
    }
  }

  // 切换班级选择
  function toggleClass(classId, checked) {
    if (checked) {
      if (!selectedClassIds.value.includes(classId)) {
        selectedClassIds.value.push(classId);
      }
    } else {
      const index = selectedClassIds.value.indexOf(classId);
      if (index > -1) {
        selectedClassIds.value.splice(index, 1);
      }
    }
  }

  // 加载课程已关联的班级
  async function loadRelatedClasses(courseId) {
    try {
      const res = await classCourseApi.queryClassIds(courseId);
      selectedClassIds.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function showModal(rowData) {
    Object.assign(form, formDefault);
    selectedClassIds.value = [];
    loadClassOptions();
    
    if (rowData) {
      nextTick(() => {
        Object.assign(form, rowData);
        // 加载已关联的班级
        if (rowData.id) {
          loadRelatedClasses(rowData.id);
        }
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
    selectedClassIds.value = [];
    if (formRef.value) {
      formRef.value.resetFields();
    }
  }

  async function onSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      SmartLoading.show();

      const submitData = {
        ...form,
        classIds: selectedClassIds.value
      };

      let courseId = form.id;
      if (form.id) {
        await courseApi.update(submitData);
        message.success('修改成功');
      } else {
        const res = await courseApi.add(submitData);
        courseId = res.data?.id;
        message.success('创建成功');
      }
      
      emit('reloadList');
      onClose();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      confirmLoading.value = false;
      SmartLoading.hide();
    }
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped>
  .class-select-section {
    margin-bottom: 16px;
  }

  .grade-filter {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    padding: 12px;
    background: #fff;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
  }

  .grade-filter .filter-label {
    font-size: 14px;
    color: #4b5563;
    margin-right: 8px;
  }

  .grade-filter .class-count {
    font-size: 13px;
    color: #6b7280;
    margin-left: 12px;
  }

  .class-config-list {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    max-height: 240px;
    overflow-y: auto;
    padding: 4px;
  }

  .class-config-item {
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 12px 16px;
    min-width: 140px;
    flex: 0 0 calc(33.333% - 8px);
    transition: all 0.2s;
    cursor: pointer;
  }

  .class-config-item.selected {
    border-color: #667eea;
    background: rgba(102, 126, 234, 0.05);
  }

  .class-header {
    font-weight: 500;
  }

  .class-header :deep(.ant-checkbox-wrapper) {
    display: flex;
    align-items: center;
  }

  .selected-count {
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed #e5e7eb;
    font-size: 13px;
    color: #667eea;
    font-weight: 500;
  }
</style>
