<template>
  <a-modal
    :open="open"
    :title="activity?.activityName || '活动内容'"
    width="900px"
    @cancel="handleCancel"
    :footer="null"
    :mask-closable="true"
  >
    <div class="activity-content-viewer">
      <a-spin :spinning="loading">
        <!-- 活动基本信息 -->
        <div class="activity-info">
          <a-descriptions :column="2" bordered size="small">
            <a-descriptions-item label="活动类型">
              <a-tag color="blue">{{ dictStore.getDataLabels('class_activity_type', activity?.activityType) }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="活动状态">
              <a-tag :color="getStatusColor(activity?.status)">
                {{ dictStore.getDataLabels('class_activity_status', activity?.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="开始时间">
              {{ formatDateTime(activity?.startTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="截止时间">
              {{ formatDateTime(activity?.endTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="活动时长">
              {{ activity?.duration }}分钟
            </a-descriptions-item>
            <a-descriptions-item label="活动总分">
              {{ activity?.score }}分
            </a-descriptions-item>
          </a-descriptions>
        </div>

        <!-- 题库类型：显示题目列表 -->
        <div class="resource-section" v-if="activity?.activityType === '1' || activity?.activityType === '2'">
          <h3 class="section-title">
            <FileTextOutlined /> 题目列表
          </h3>
          <a-table
            :columns="questionColumns"
            :data-source="resourceList"
            :pagination="false"
            size="small"
            row-key="id"
          >
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'index'">
                {{ index + 1 }}
              </template>
              <template v-if="column.key === 'score'">
                <span class="score-text">{{ record.score }}分</span>
              </template>
            </template>
          </a-table>
        </div>

        <!-- 实验指导书类型：显示实验文档 -->
        <div class="resource-section" v-if="activity?.activityType === '3'">
          <h3 class="section-title">
            <ExperimentOutlined /> 实验指导书
          </h3>
          <div class="experiment-list">
            <div v-for="item in resourceList" :key="item.id" class="experiment-item">
              <div class="experiment-icon">
                <FilePdfOutlined />
              </div>
              <div class="experiment-info">
                <div class="experiment-name">{{ item.resourceName }}</div>
                <div class="experiment-score">满分: {{ item.score }}分</div>
              </div>
            </div>
            <a-empty v-if="resourceList.length === 0" description="暂无实验指导书" />
          </div>
        </div>

        <!-- 话题讨论类型：显示讨论话题 -->
        <div class="resource-section" v-if="activity?.activityType === '4'">
          <h3 class="section-title">
            <CommentOutlined /> 讨论话题
          </h3>
          <div class="discussion-list">
            <div v-for="item in resourceList" :key="item.id" class="discussion-item">
              <div class="discussion-icon">
                <MessageOutlined />
              </div>
              <div class="discussion-info">
                <div class="discussion-name">{{ item.resourceName }}</div>
                <div class="discussion-score">满分: {{ item.score }}分</div>
              </div>
            </div>
            <a-empty v-if="resourceList.length === 0" description="暂无讨论话题" />
          </div>
        </div>
      </a-spin>
    </div>
  </a-modal>
</template>

<script setup>
  import { ref, watch } from 'vue';
  import {
    FileTextOutlined,
    ExperimentOutlined,
    CommentOutlined,
    FilePdfOutlined,
    MessageOutlined,
  } from '@ant-design/icons-vue';
  import { message } from 'ant-design-vue';
  import { classActivityResourceApi } from '/@/api/smartmind/classActivityResource-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import dayjs from 'dayjs';

  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    activity: {
      type: Object,
      default: null,
    },
  });

  const emit = defineEmits(['update:open']);

  const dictStore = useDictStore();
  const loading = ref(false);
  const resourceList = ref([]);

  const questionColumns = [
    { title: '序号', key: 'index', width: 60 },
    { title: '题目名称', dataIndex: 'resourceName', key: 'resourceName' },
    { title: '分值', key: 'score', width: 100 },
  ];

  watch(() => props.open, (val) => {
    if (val && props.activity) {
      loadResources();
    }
  });

  async function loadResources() {
    if (!props.activity?.id) return;
    loading.value = true;
    try {
      const res = await classActivityResourceApi.loadByActivityId(props.activity.id);
      resourceList.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
      message.error('加载活动内容失败');
    } finally {
      loading.value = false;
    }
  }

  function handleCancel() {
    emit('update:open', false);
  }

  function formatDateTime(date) {
    if (!date) return '-';
    return dayjs(date).format('YYYY-MM-DD HH:mm');
  }

  function getStatusColor(status) {
    const map = {
      '0': 'default',
      '1': 'processing',
      '2': 'success',
      '3': 'error',
    };
    return map[status] || 'default';
  }
</script>

<style scoped lang="less">
.activity-content-viewer {
  padding: 16px 0;

  .activity-info {
    margin-bottom: 24px;
  }

  .resource-section {
    margin-bottom: 24px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #2d3748;
      margin: 0 0 16px 0;
      display: flex;
      align-items: center;
      gap: 8px;

      .anticon {
        font-size: 18px;
        color: #4facfe;
      }
    }

    .score-text {
      color: #ff6b9d;
      font-weight: 600;
    }

    .experiment-list {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .experiment-item {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 16px;
        background: rgba(79, 172, 254, 0.05);
        border: 1px solid rgba(79, 172, 254, 0.15);
        border-radius: 12px;
        transition: all 0.3s;

        &:hover {
          background: rgba(79, 172, 254, 0.1);
          border-color: rgba(79, 172, 254, 0.3);
        }

        .experiment-icon {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #4facfe, #667eea);
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
          color: white;
        }

        .experiment-info {
          flex: 1;

          .experiment-name {
            font-size: 15px;
            font-weight: 600;
            color: #2d3748;
            margin-bottom: 4px;
          }

          .experiment-score {
            font-size: 13px;
            color: #718096;
          }
        }
      }
    }

    .discussion-list {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .discussion-item {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 16px;
        background: rgba(161, 140, 209, 0.05);
        border: 1px solid rgba(161, 140, 209, 0.15);
        border-radius: 12px;
        transition: all 0.3s;

        &:hover {
          background: rgba(161, 140, 209, 0.1);
          border-color: rgba(161, 140, 209, 0.3);
        }

        .discussion-icon {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #a18cd1, #fbc2eb);
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
          color: white;
        }

        .discussion-info {
          flex: 1;

          .discussion-name {
            font-size: 15px;
            font-weight: 600;
            color: #2d3748;
            margin-bottom: 4px;
          }

          .discussion-score {
            font-size: 13px;
            color: #718096;
          }
        }
      }
    }
  }
}
</style>
