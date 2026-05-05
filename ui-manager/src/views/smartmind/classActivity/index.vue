<template>
  <div class="class-activity-page">
    <div class="page-header">
      <h2>课堂活动 - {{ courseName }}</h2>
      <div class="header-info">
        <span>班级：{{ className }}</span>
        <span class="divider">|</span>
        <span>课堂：{{ sessionName }}</span>
      </div>
    </div>

    <div class="activity-buttons">
      <a-button type="primary" size="large" @click="showQuizModal">
        <FileTextOutlined /> 随堂测试
      </a-button>
      <a-button type="primary" size="large" @click="showExerciseModal">
        <EditOutlined /> 随堂练习
      </a-button>
      <a-button type="primary" size="large" @click="showExperimentModal">
        <ExperimentOutlined /> 课堂实验
      </a-button>
      <a-button type="primary" size="large" @click="showDiscussionModal">
        <CommentOutlined /> 话题讨论
      </a-button>
    </div>

    <div class="resource-list-section">
      <h3>已关联资源</h3>
      <a-table 
        size="small" 
        :loading="resourceTableLoading" 
        bordered 
        :dataSource="resourceTableData" 
        :columns="resourceColumns" 
        rowKey="id" 
        :pagination="false"
      >
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'resourceType'">
            {{ getResourceTypeLabel(record.resourceType) }}
          </template>
        </template>
      </a-table>
    </div>

    <a-modal
      v-model:open="resourceModalVisible"
      :title="resourceModalTitle"
      width="900px"
      @ok="handleResourceSubmit"
      @cancel="handleResourceModalClose"
      :confirmLoading="resourceSubmitLoading"
    >
      <div class="resource-modal-content">
        <a-table
          size="small"
          :loading="resourceSelectLoading"
          bordered
          :dataSource="resourceSelectData"
          :columns="resourceSelectColumns"
          rowKey="id"
          :row-selection="{ selectedRowKeys: selectedResourceIds, onChange: onResourceSelectChange }"
          :pagination="{ pageSize: 10, showSizeChanger: true, showTotal: (total) => `共${total}条` }"
        >
          <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'questionType'">
            {{ dictStore.getDataLabels('question_type', record.questionType) }}
          </template>
          <template v-else-if="column.dataIndex === 'difficultyLevel'">
            {{ dictStore.getDataLabels('question_difficulty', record.difficultyLevel) }}
          </template>
        </template>
        </a-table>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import { 
  FileTextOutlined, 
  EditOutlined, 
  ExperimentOutlined, 
  CommentOutlined 
} from '@ant-design/icons-vue';
import { questionApi } from '/@/api/smartmind/question-api.js';
import { experimentGuideApi } from '/@/api/smartmind/experimentGuide-api.js';
import { discussionTopicApi } from '/@/api/smartmind/discussionTopic-api.js';
import { classActivityResourceApi } from '/@/api/smartmind/classActivityResource-api.js';
import { classActivityApi } from '/@/api/smartmind/classActivity-api.js';
import { smartSentry } from '/@/lib/smart-sentry';
import { useDictStore } from '/@/store/modules/system/dict.js';

const dictStore = useDictStore();
const route = useRoute();

const courseId = ref(route.query.courseId);
const classId = ref(route.query.classId);
const sessionId = ref(route.query.sessionId);
const courseName = ref(route.query.courseName || '');
const className = ref(route.query.className || '');
const sessionName = ref(route.query.sessionName || '');

const resourceTableLoading = ref(false);
const resourceTableData = ref([]);

const resourceColumns = ref([
  { title: '序号', dataIndex: 'seq', width: 60 },
  { title: '资源类型', dataIndex: 'resourceType', width: 120 },
  { title: '资源名称', dataIndex: 'resourceName', ellipsis: true },
  { title: '分数', dataIndex: 'score', width: 80 },
  { title: '排序', dataIndex: 'sortOrder', width: 80 },
]);

const resourceModalVisible = ref(false);
const resourceModalTitle = ref('');
const resourceSelectLoading = ref(false);
const resourceSelectData = ref([]);
const selectedResourceIds = ref([]);
const resourceSubmitLoading = ref(false);
const currentResourceType = ref(null);
const currentActivityId = ref(null);

const resourceSelectColumns = ref([]);

onMounted(() => {
  loadActivities();
});

async function loadActivities() {
  if (!sessionId.value) return;
  try {
    resourceTableLoading.value = true;
    const res = await classActivityApi.queryBySessionId(sessionId.value);
    const list = res.data || [];
    resourceTableData.value = list.map((item, index) => ({ ...item, seq: index + 1, resourceType: getActivityTypeLabel(item.activityType), resourceName: getActivityName(item) }));
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    resourceTableLoading.value = false;
  }
}

function getActivityTypeLabel(type) {
  const map = { 'exercise': '随堂练习', 'quiz': '随堂测试', 'experiment': '课堂实验', 'discussion': '话题讨论' };
  return map[type] || type;
}

function getActivityName(item) {
  return item.activityName || getActivityTypeLabel(item.activityType);
}

function getActivityToResourceType(activityType) {
  const map = { 'quiz': 'question', 'exercise': 'question', 'experiment': 'experiment', 'discussion': 'discussion' };
  return map[activityType] || activityType;
}

function showQuizModal() {
  currentResourceType.value = 'quiz';
  resourceModalTitle.value = '选择随堂测试题目';
  resourceSelectColumns.value = [
    { title: '题目内容', dataIndex: 'questionContent', ellipsis: true },
    { title: '题目类型', dataIndex: 'questionType', width: 100 },
    { title: '难度', dataIndex: 'difficultyLevel', width: 80 },
    { title: '分数', dataIndex: 'score', width: 80 },
  ];
  createActivityAndLoadResources();
}

function showExerciseModal() {
  currentResourceType.value = 'exercise';
  resourceModalTitle.value = '选择随堂练习题目';
  resourceSelectColumns.value = [
    { title: '题目内容', dataIndex: 'questionContent', ellipsis: true },
    { title: '题目类型', dataIndex: 'questionType', width: 100 },
    { title: '难度', dataIndex: 'difficultyLevel', width: 80 },
    { title: '分数', dataIndex: 'score', width: 80 },
  ];
  createActivityAndLoadResources();
}

function showExperimentModal() {
  currentResourceType.value = 'experiment';
  resourceModalTitle.value = '选择课堂实验指导书';
  resourceSelectColumns.value = [
    { title: '指导书名称', dataIndex: 'guideName', ellipsis: true },
    { title: '实验环节', dataIndex: 'experimentSession', width: 100 },
  ];
  createActivityAndLoadResources();
}

function showDiscussionModal() {
  currentResourceType.value = 'discussion';
  resourceModalTitle.value = '选择讨论话题';
  resourceSelectColumns.value = [
    { title: '话题名称', dataIndex: 'topicName', ellipsis: true },
    { title: '话题描述', dataIndex: 'topicContent', ellipsis: true },
  ];
  createActivityAndLoadResources();
}

async function createActivityAndLoadResources() {
  if (!sessionId.value) {
    message.warning('缺少课堂ID参数');
    return;
  }
  try {
    resourceSelectLoading.value = true;
    const param = {
      sessionId: sessionId.value,
      courseId: courseId.value,
      clazzId: classId.value,
      activityType: currentResourceType.value,
      status: '0',
    };
    const res = await classActivityApi.add(param);
    currentActivityId.value = res.data?.id || res.data;
    if (!currentActivityId.value) {
      message.error('创建课堂活动失败');
      return;
    }
    await loadResources();
    resourceModalVisible.value = true;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    resourceSelectLoading.value = false;
  }
}

async function loadResources() {
  if (!courseId.value) {
    message.warning('缺少课程ID参数');
    return;
  }
  try {
    resourceSelectLoading.value = true;
    selectedResourceIds.value = [];
    resourceSelectData.value = [];

    const unitCodes = route.query.unitCodes ? JSON.parse(route.query.unitCodes) : [];
    const param = { courseId: courseId.value, unitCodes };

    let res;
    if (currentResourceType.value === 'exercise' || currentResourceType.value === 'quiz') {
      res = await questionApi.loadByCourseAndUnitCodes(param);
    } else if (currentResourceType.value === 'experiment') {
      res = await experimentGuideApi.loadByCourseAndUnitCodes(param);
    } else if (currentResourceType.value === 'discussion') {
      res = await discussionTopicApi.loadByCourseAndUnitCodes(param);
    }
    resourceSelectData.value = res.data || [];
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    resourceSelectLoading.value = false;
  }
}

function onResourceSelectChange(selectedKeys) {
  selectedResourceIds.value = selectedKeys;
}

async function handleResourceSubmit() {
  if (selectedResourceIds.value.length === 0) {
    message.warning('请至少选择一个资源');
    return;
  }
  if (!currentActivityId.value) {
    message.warning('缺少课堂活动ID参数');
    return;
  }
  try {
    resourceSubmitLoading.value = true;
    const param = {
      activityId: currentActivityId.value,
      resourceType: getActivityToResourceType(currentResourceType.value),
      resourceIds: selectedResourceIds.value,
    };
    await classActivityResourceApi.batchSave(param);
    message.success('保存成功');
    resourceModalVisible.value = false;
    loadActivityResources();
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    resourceSubmitLoading.value = false;
  }
}

function handleResourceModalClose() {
  resourceModalVisible.value = false;
  selectedResourceIds.value = [];
  resourceSelectData.value = [];
  currentActivityId.value = null;
}
</script>

<style scoped>
.class-activity-page {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 24px 32px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.header-info {
  font-size: 14px;
  opacity: 0.9;
}

.header-info .divider {
  margin: 0 12px;
}

.activity-buttons {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.activity-buttons .ant-btn {
  flex: 1;
  height: 56px;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.activity-buttons .ant-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.resource-list-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.resource-list-section h3 {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.resource-modal-content {
  max-height: 500px;
  overflow-y: auto;
}
</style>
