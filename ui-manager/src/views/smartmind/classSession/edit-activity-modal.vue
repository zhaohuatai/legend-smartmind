<template>
  <a-modal
    :open="open"
    title="编辑课堂活动"
    width="960px"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :confirm-loading="submitLoading"
    :mask-closable="false"
    ok-text="保存"
    cancel-text="取消"
  >
    <div class="resource-modal" v-if="loaded">
      <div class="modal-section time-section">
        <div class="section-label">
          <ClockCircleOutlined />
          <span>活动时间</span>
        </div>
        <div class="time-row">
          <div class="time-item">
            <span class="time-label">开始时间</span>
            <a-date-picker
              v-model:value="startTime"
              show-time
              format="YYYY-MM-DD HH:mm"
              placeholder="选择开始时间"
              style="width: 100%"
            />
          </div>
          <div class="time-divider">至</div>
          <div class="time-item">
            <span class="time-label">截止时间</span>
            <a-date-picker
              v-model:value="endTime"
              show-time
              format="YYYY-MM-DD HH:mm"
              placeholder="选择截止时间"
              style="width: 100%"
            />
          </div>
        </div>
      </div>

      <div class="modal-section score-section">
        <div class="section-label">
          <TrophyOutlined />
          <span>满分设定</span>
        </div>
        <div class="score-row">
          <span class="score-label">活动总分</span>
          <span class="score-value">100</span>
          <span class="score-unit">分</span>
        </div>
      </div>

      <div class="modal-section table-section">
        <div class="section-label">
          <FileTextOutlined />
          <span>选择题目</span>
          <span class="selected-count">已选 {{ selectedIds.length }} 题</span>
        </div>
        <a-table
          size="small"
          :loading="tableLoading"
          bordered
          :dataSource="tableData"
          :columns="columns"
          rowKey="id"
          :row-selection="{ selectedRowKeys: selectedIds, onChange: onSelectChange }"
          :pagination="{ pageSize: 8, showSizeChanger: true, showTotal: (total) => `共${total}条` }"
        >
          <template #bodyCell="{ record, column }">
            <template v-if="column.dataIndex === 'questionType'">
              {{ dictStore.getDataLabels('question_type', record.questionType) }}
            </template>
            <template v-else-if="column.dataIndex === 'difficultyLevel'">
              {{ dictStore.getDataLabels('question_difficulty', record.difficultyLevel) }}
            </template>
            <template v-else-if="column.dataIndex === 'itemScore'">
              <a-input-number
                v-model:value="record.itemScore"
                :min="0"
                :max="100"
                :precision="2"
                size="small"
                style="width: 80px"
                @change="onScoreChange"
              />
            </template>
          </template>
        </a-table>
      </div>

      <div class="modal-section action-section">
        <a-button type="primary" @click="autoDistributeScore" :disabled="selectedIds.length === 0">
          <ThunderboltOutlined /> 一键设置分数
        </a-button>
        <span class="score-summary">当前总分: {{ currentTotalScore }} / {{ TOTAL_SCORE }}</span>
      </div>
    </div>
    <div v-else class="loading-container">
      <a-spin size="large" tip="加载中..." />
    </div>
  </a-modal>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { ClockCircleOutlined, FileTextOutlined, TrophyOutlined, ThunderboltOutlined } from '@ant-design/icons-vue';
import { useDictStore } from '/@/store/modules/system/dict.js';
import { questionApi } from '/@/api/smartmind/question-api.js';
import { classActivityApi } from '/@/api/smartmind/classActivity-api.js';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const props = defineProps({
  open: { type: Boolean, default: false },
  activityId: { type: [String, Number], default: null },
});

const emit = defineEmits(['update:open', 'success']);

const dictStore = useDictStore();
const tableLoading = ref(false);
const tableData = ref([]);
const selectedIds = ref([]);
const submitLoading = ref(false);
const startTime = ref(null);
const endTime = ref(null);
const loaded = ref(false);
const activityData = ref(null);
const TOTAL_SCORE = 100;

const columns = [
  { title: '题目内容', dataIndex: 'questionContent', ellipsis: true },
  { title: '题目类型', dataIndex: 'questionType', width: 100 },
  { title: '难度', dataIndex: 'difficultyLevel', width: 80 },
  { title: '分数', dataIndex: 'itemScore', width: 100 },
];

const currentTotalScore = computed(() => {
  const selected = tableData.value.filter(item => selectedIds.value.includes(item.id));
  return selected.reduce((sum, item) => sum + (item.itemScore || 0), 0).toFixed(2);
});

watch(() => props.open, (val) => {
  if (val) {
    loadActivityData();
  } else {
    loaded.value = false;
    activityData.value = null;
  }
});

async function loadActivityData() {
  if (!props.activityId) return;
  try {
    tableLoading.value = true;
    loaded.value = false;
    
    const res = await classActivityApi.loadCreateDtoById(props.activityId);
    activityData.value = res.data;
    
    if (activityData.value) {
      if (activityData.value.startTime) {
        startTime.value = dayjs(activityData.value.startTime);
      }
      if (activityData.value.endTime) {
        endTime.value = dayjs(activityData.value.endTime);
      }
      
      await loadResources();
      
      if (activityData.value.activityResourceList && activityData.value.activityResourceList.length > 0) {
        const resourceIds = activityData.value.activityResourceList.map(r => r.resourceId);
        selectedIds.value = resourceIds;
        
        activityData.value.activityResourceList.forEach(res => {
          const item = tableData.value.find(t => t.id === res.resourceId);
          if (item) {
            item.itemScore = res.score;
          }
        });
      }
    }
    
    loaded.value = true;
  } catch (e) {
    smartSentry.captureError(e);
    message.error('加载活动数据失败');
  } finally {
    tableLoading.value = false;
  }
}

async function loadResources() {
  if (!activityData.value) return;
  try {
    selectedIds.value = [];
    tableData.value = [];

    const courseId = activityData.value.courseId;
    const sessionUnitsInfo = activityData.value.sessionUnitsInfo;
    const unitCodes = sessionUnitsInfo ? JSON.parse(sessionUnitsInfo).map(u => u.unitCode) : [];
    
    const res = await questionApi.loadByCourseAndUnitCodes({ courseId, unitCodes });
    tableData.value = (res.data || []).map(item => ({ ...item, itemScore: null }));
  } catch (e) {
    smartSentry.captureError(e);
  }
}

function onSelectChange(keys) {
  selectedIds.value = keys;
  autoDistributeScore();
}

function onScoreChange() {
}

function autoDistributeScore() {
  if (selectedIds.value.length === 0) {
    message.warning('请先选择题目');
    return;
  }
  const avg = Math.floor((TOTAL_SCORE / selectedIds.value.length) * 100) / 100;
  const remainder = Math.round((TOTAL_SCORE - avg * selectedIds.value.length) * 100) / 100;

  tableData.value.forEach(item => {
    if (selectedIds.value.includes(item.id)) {
      item.itemScore = avg;
    }
  });

  if (remainder > 0) {
    const firstSelected = tableData.value.find(item => selectedIds.value.includes(item.id));
    if (firstSelected) {
      firstSelected.itemScore = Math.round((firstSelected.itemScore + remainder) * 100) / 100;
    }
  }
}

async function handleSubmit() {
  if (selectedIds.value.length === 0) {
    message.warning('请至少选择一个题目');
    return;
  }
  if (!startTime.value || !endTime.value) {
    message.warning('请设置开始时间和截止时间');
    return;
  }
  if (dayjs(endTime.value).isBefore(dayjs(startTime.value))) {
    message.warning('截止时间不能早于开始时间');
    return;
  }

  const selectedItems = tableData.value.filter(item => selectedIds.value.includes(item.id));
  const hasNullScore = selectedItems.some(item => item.itemScore === null || item.itemScore === undefined);
  if (hasNullScore) {
    message.warning('请为所有选中的题目设置分数');
    return;
  }

  const actualTotal = selectedItems.reduce((sum, item) => sum + (item.itemScore || 0), 0);
  if (Math.abs(actualTotal - TOTAL_SCORE) > 0.01) {
    message.warning(`各题目分数之和(${actualTotal})必须等于活动总分(${TOTAL_SCORE})`);
    return;
  }

  try {
    submitLoading.value = true;

    const activityResourceList = selectedItems.map(item => ({
      resourceType: '1',
      resourceId: item.id,
      resourceName: item.questionContent?.substring(0, 100) || '',
      score: item.itemScore,
    }));

    const param = {
      id: props.activityId,
      activityCode: activityData.value.activityCode,
      sessionId: activityData.value.sessionId,
      courseId: activityData.value.courseId,
      clazzId: activityData.value.clazzId,
      activityType: activityData.value.activityType,
      status: activityData.value.status,
      score: TOTAL_SCORE,
      startTime: dayjs(startTime.value).format('YYYY-MM-DD HH:mm:ss'),
      endTime: dayjs(endTime.value).format('YYYY-MM-DD HH:mm:ss'),
      activityResourceList,
    };
    
    await classActivityApi.update(param);
    message.success('保存成功');
    emit('update:open', false);
    emit('success');
  } catch (e) {
    smartSentry.captureError(e);
    message.error('保存失败');
  } finally {
    submitLoading.value = false;
  }
}

function handleCancel() {
  emit('update:open', false);
}
</script>

<style scoped lang="less">
.resource-modal {
  .modal-section {
    margin-bottom: 16px;

    .section-label {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 14px;
      font-weight: 600;
      color: #1a1a2e;
      margin-bottom: 12px;

      .selected-count {
        margin-left: auto;
        font-size: 12px;
        font-weight: 400;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 2px 10px;
        border-radius: 10px;
      }
    }
  }

  .time-section {
    background: rgba(102, 126, 234, 0.04);
    border-radius: 10px;
    padding: 14px 16px;

    .time-row {
      display: flex;
      align-items: flex-end;
      gap: 12px;

      .time-item {
        flex: 1;

        .time-label {
          display: block;
          font-size: 12px;
          color: #6b7280;
          margin-bottom: 6px;
        }
      }

      .time-divider {
        font-size: 13px;
        color: #9ca3af;
        padding-bottom: 8px;
      }
    }
  }

  .score-section {
    background: rgba(250, 173, 20, 0.04);
    border-radius: 10px;
    padding: 14px 16px;

    .score-row {
      display: flex;
      align-items: center;
      gap: 8px;

      .score-label {
        font-size: 13px;
        color: #4b5563;
      }

      .score-value {
        font-size: 18px;
        font-weight: 700;
        color: #fa8c16;
      }

      .score-unit {
        font-size: 13px;
        color: #6b7280;
      }
    }
  }

  .table-section {
    .ant-table {
      border-radius: 8px;
      overflow: hidden;
    }
  }

  .action-section {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    background: rgba(102, 126, 234, 0.04);
    border-radius: 10px;

    .score-summary {
      font-size: 14px;
      font-weight: 600;
      color: #1a1a2e;
    }
  }
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}
</style>
