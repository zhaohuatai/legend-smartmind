<template>
  <a-modal
    :open="visible"
    title="选择班级"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="600px"
    :confirmLoading="confirmLoading"
  >
    <div class="class-select-container">
      <div class="select-hint">
        <InfoCircleOutlined />
        <span>请选择要关联的班级（可多选）</span>
      </div>
      
      <!-- 年级筛选 -->
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
      
      <!-- 班级复选列表 -->
      <div class="class-checkbox-wrapper">
        <a-checkbox-group v-model:value="selectedClassIds" style="width: 100%">
          <div class="class-checkbox-grid">
            <a-checkbox 
              v-for="item in filteredClassOptions" 
              :key="item.value" 
              :value="item.value"
              class="class-checkbox-card"
            >
              {{ item.label }}
            </a-checkbox>
          </div>
        </a-checkbox-group>
        <div v-if="filteredClassOptions.length === 0" class="empty-text">暂无班级</div>
      </div>
      
      <!-- 已选计数 -->
      <div class="selected-info" v-if="selectedClassIds.length > 0">
        已选择 <span class="count">{{ selectedClassIds.length }}</span> 个班级
      </div>
    </div>
  </a-modal>
</template>

<script setup>
  import { ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { InfoCircleOutlined } from '@ant-design/icons-vue';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { classCourseApi } from '/@/api/smartmind/class-course-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const dictStore = useDictStore();
  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const loading = ref(false);
  const courseId = ref(null);
  const selectedClassIds = ref([]);
  const classOptions = ref([]);
  const filteredClassOptions = ref([]);
  const selectedGrade = ref(undefined);

  // 加载班级列表
  async function loadClassOptions() {
    loading.value = true;
    try {
      const res = await clazzApi.selectVo();
      classOptions.value = res.data || [];
      filteredClassOptions.value = [...classOptions.value];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      loading.value = false;
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

  // 加载课程已关联的班级
  async function loadSelectedClass() {
    if (!courseId.value) return;
    try {
      const res = await classCourseApi.queryClassIds(courseId.value);
      selectedClassIds.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function showModal(id) {
    courseId.value = id;
    selectedClassIds.value = [];
    selectedGrade.value = undefined;
    visible.value = true;
    loadClassOptions();
    loadSelectedClass();
  }

  function onClose() {
    visible.value = false;
    courseId.value = null;
    selectedClassIds.value = [];
    selectedGrade.value = undefined;
  }

  async function onSubmit() {
    if (!courseId.value) {
      message.error('课程ID不能为空');
      return;
    }
    confirmLoading.value = true;
    try {
      await classCourseApi.saveRelation({
        courseId: courseId.value,
        classIds: selectedClassIds.value
      });
      message.success('保存成功');
      onClose();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      confirmLoading.value = false;
    }
  }

  defineExpose({
    showModal
  });
</script>

<style scoped lang="less">
  .class-select-container {
    padding: 10px 0;

    .select-hint {
      margin-bottom: 16px;
      color: #64748b;
      font-size: 14px;
      display: flex;
      align-items: center;
      gap: 8px;

      :deep(.anticon) {
        color: #0d9488;
      }
    }

    .grade-filter {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      padding-bottom: 16px;
      border-bottom: 1px dashed #e8e8e8;

      .filter-label {
        font-size: 14px;
        color: #666;
        margin-right: 8px;
      }

      .class-count {
        margin-left: 12px;
        font-size: 13px;
        color: #999;
      }
    }

    .class-checkbox-wrapper {
      max-height: 300px;
      overflow-y: auto;
      padding-right: 4px;
    }

    .class-checkbox-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }

    .class-checkbox-card {
      padding: 8px 14px;
      background: #f5f5f5;
      border: 1px solid #e8e8e8;
      border-radius: 6px;
      margin: 0;
      transition: all 0.2s;

      &:hover {
        border-color: #667eea;
        background: rgba(102, 126, 234, 0.08);
      }

      &.ant-checkbox-wrapper-checked {
        background: rgba(102, 126, 234, 0.12);
        border-color: #667eea;
      }

      :deep(.ant-checkbox) {
        margin-right: 6px;
      }
    }

    .empty-text {
      text-align: center;
      color: #999;
      padding: 40px 0;
      font-size: 14px;
    }

    .selected-info {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px dashed #e8e8e8;
      font-size: 14px;
      color: #666;
      text-align: center;

      .count {
        color: #667eea;
        font-weight: 600;
        font-size: 16px;
      }
    }
  }
</style>
