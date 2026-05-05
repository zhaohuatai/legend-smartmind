<template>
  <div class="discussion-topic-page">
    <!-- 顶部：面包屑 + 操作区 -->
    <div class="header-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <router-link :to="`/smartmind/course/detail?courseId=${courseId}`">{{ courseInfo?.courseName || '课程详情' }}</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>{{ currentUnit?.unitName || '单元' }}</a-breadcrumb-item>
        <a-breadcrumb-item>讨论活动话题</a-breadcrumb-item>
      </a-breadcrumb>

      <div class="header-actions">
        <a-button type="primary" @click="openAIGenerateModal">
          <ThunderboltOutlined /> AI 生成话题
        </a-button>
        <a-button @click="openCreateModal" style="margin-left: 8px">
          <PlusOutlined /> 新建话题
        </a-button>
      </div>
    </div>

    <!-- 主体内容：左侧（话题列表） + 右侧（话题详情） -->
    <div class="main-content">
      <!-- 左侧：话题列表 -->
      <div class="left-panel">
        <div class="list-header">
          <span class="list-title">话题列表（{{ topicList.length }}）</span>
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索话题名称或内容"
            size="small"
            style="width: 200px"
            @search="onSearch"
          />
        </div>
        <div class="topic-list">
          <div
            v-for="item in topicList"
            :key="item.id"
            class="topic-item"
            :class="{ active: selectedTopic?.id === item.id }"
            @click="selectTopic(item)"
          >
            <div class="topic-item-header">
              <a-tag :color="getTopicTypeColor(item.topicType)" size="small">
                <DictLabel dict-code="discussion_topic_type" :data-value="item.topicType" />
              </a-tag>
            </div>
            <div class="topic-item-name">{{ item.topicName }}</div>
            <div class="topic-item-content">{{ truncateText(item.topicContent, 50) }}</div>
            <div class="topic-item-footer">
              <span class="topic-item-meta">时长：{{ item.estimatedDuration ? item.estimatedDuration + '分钟' : '-' }}</span>
              <span class="topic-item-meta">使用：{{ item.usageCount || 0 }}次</span>
            </div>
          </div>
          <a-empty v-if="topicList.length === 0" description="暂无话题" class="empty-list" />
        </div>
      </div>

      <!-- 右侧：话题详情 -->
      <div class="right-panel">
        <div class="detail-header" v-if="selectedTopic">
          <div class="detail-title-row">
            <h3 class="detail-title">话题详情</h3>
            <div class="detail-actions">
              <a-button size="small" @click="openEditModal">
                <EditOutlined /> 编辑
              </a-button>
              <a-popconfirm title="确定删除该话题吗？" @confirm="deleteTopic">
                <a-button danger size="small" style="margin-left: 8px">
                  <DeleteOutlined /> 删除
                </a-button>
              </a-popconfirm>
            </div>
          </div>
        </div>

        <div class="detail-content" v-if="selectedTopic">
          <div class="detail-section">
            <div class="section-label">基本信息</div>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">话题名称：</span>
                <span class="info-value">{{ selectedTopic.topicName }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">话题类型：</span>
                <span class="info-value"><DictLabel dict-code="discussion_topic_type" :data-value="selectedTopic.topicType" /></span>
              </div>
              <div class="info-item">
                <span class="info-label">预计时长：</span>
                <span class="info-value">{{ selectedTopic.estimatedDuration ? selectedTopic.estimatedDuration + '分钟' : '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">小组人数：</span>
                <span class="info-value">{{ selectedTopic.groupSize ? selectedTopic.groupSize + '人' : '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">知识点标签：</span>
                <span class="info-value">{{ selectedTopic.knowledgePoints || '-' }}</span>
              </div>
            </div>
          </div>

          <div class="detail-section">
            <div class="section-label">话题内容</div>
            <div class="content-display">{{ selectedTopic.topicContent || '暂无内容' }}</div>
          </div>

          <div class="detail-section" v-if="selectedTopic.backgroundMaterial">
            <div class="section-label">背景材料</div>
            <div class="content-display">{{ selectedTopic.backgroundMaterial }}</div>
          </div>

          <div class="detail-section" v-if="selectedTopic.guidanceTips">
            <div class="section-label">教师引导提示</div>
            <div class="content-display">{{ selectedTopic.guidanceTips }}</div>
          </div>

          <div class="detail-section" v-if="selectedTopic.expectedAnswers">
            <div class="section-label">预期答案要点</div>
            <div class="content-display">{{ selectedTopic.expectedAnswers }}</div>
          </div>

          <div class="detail-section" v-if="selectedTopic.evaluationCriteria">
            <div class="section-label">评价标准</div>
            <div class="content-display">{{ selectedTopic.evaluationCriteria }}</div>
          </div>
        </div>

        <div class="empty-detail" v-else>
          <a-empty description="请在左侧选择话题查看详情" />
        </div>
      </div>
    </div>

    <!-- AI生成话题弹窗 -->
    <AIGenerateModal
      v-model:visible="aiGenerateModalVisible"
      :course-id="courseId"
      :unit-code="unitCode"
      @created="loadTopics"
    />

    <!-- 话题编辑/新建弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      :title="isEditModalCreate ? '新建话题' : '编辑话题'"
      @ok="handleModalSubmit"
      @cancel="editModalVisible = false"
      width="720px"
      :confirm-loading="modalSubmitLoading"
      :mask-closable="false"
    >
      <a-form
        :model="editModalForm"
        :rules="modalFormRules"
        ref="modalFormRef"
        layout="horizontal"
        :label-col="{ style: { width: '100px' } }"
        :wrapper-col="{ style: { flex: 1 } }"
        class="topic-form"
      >
        <!-- 话题名称 -->
        <a-form-item label="话题名称" name="topicName">
          <a-input v-model:value="editModalForm.topicName" placeholder="请输入话题名称" />
        </a-form-item>

        <!-- 话题类型 -->
        <a-form-item label="话题类型" name="topicType">
          <DictSelect v-model:value="editModalForm.topicType" dict-code="discussion_topic_type" placeholder="请选择话题类型" width="100%" />
        </a-form-item>

        <!-- 话题内容 -->
        <a-form-item label="话题内容" name="topicContent">
          <a-textarea v-model:value="editModalForm.topicContent" :rows="4" placeholder="请输入讨论问题/话题内容" />
        </a-form-item>

        <!-- 预计时长、小组人数 -->
        <div class="form-row">
          <a-form-item label="预计时长" name="estimatedDuration" class="form-item-duration">
            <a-input-number v-model:value="editModalForm.estimatedDuration" :min="1" :max="120" style="width: 100%" addon-after="分钟" />
          </a-form-item>
          <a-form-item label="小组人数" name="groupSize" class="form-item-group">
            <a-input-number v-model:value="editModalForm.groupSize" :min="2" :max="20" style="width: 100%" addon-after="人" />
          </a-form-item>
        </div>

        <!-- 背景材料 -->
        <a-form-item label="背景材料">
          <a-textarea v-model:value="editModalForm.backgroundMaterial" :rows="3" placeholder="请输入背景材料或案例描述" />
        </a-form-item>

        <!-- 教师引导提示 -->
        <a-form-item label="引导提示">
          <a-textarea v-model:value="editModalForm.guidanceTips" :rows="3" placeholder="请输入教师引导提示" />
        </a-form-item>

        <!-- 预期答案要点 -->
        <a-form-item label="预期答案">
          <a-textarea v-model:value="editModalForm.expectedAnswers" :rows="3" placeholder="请输入预期答案要点" />
        </a-form-item>

        <!-- 评价标准 -->
        <a-form-item label="评价标准">
          <a-textarea v-model:value="editModalForm.evaluationCriteria" :rows="3" placeholder="请输入评价标准" />
        </a-form-item>

        <!-- 知识点标签 -->
        <a-form-item label="知识点">
          <a-input v-model:value="editModalForm.knowledgePoints" placeholder="多个标签用逗号分隔" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { getRequest, postRequest } from '/@/lib/axios';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import DictLabel from '/@/components/support/dict-label/index.vue';
  import {
    ThunderboltOutlined,
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
  } from '@ant-design/icons-vue';
  import AIGenerateModal from './components/AIGenerateModal.vue';

  const route = useRoute();
  const router = useRouter();

  const courseId = ref(route.query.courseId);
  const unitCode = ref(route.query.unitCode);

  const courseInfo = ref(null);
  const currentUnit = ref(null);

  const topicList = ref([]);
  const selectedTopic = ref(null);
  const searchKeyword = ref('');

  const aiGenerateModalVisible = ref(false);

  const editModalVisible = ref(false);
  const isEditModalCreate = ref(false);
  const modalSubmitLoading = ref(false);
  const modalFormRef = ref(null);

  const editModalForm = ref({
    id: null,
    topicName: '',
    topicType: '1',
    topicContent: '',
    estimatedDuration: 15,
    groupSize: 4,
    backgroundMaterial: '',
    guidanceTips: '',
    expectedAnswers: '',
    evaluationCriteria: '',
    knowledgePoints: '',
  });

  const modalFormRules = {
    topicName: [{ required: true, message: '请输入话题名称', trigger: 'blur' }],
    topicType: [{ required: true, message: '请选择话题类型', trigger: 'change' }],
    topicContent: [{ required: true, message: '请输入话题内容', trigger: 'blur' }],
  };

  onMounted(async () => {
    await loadCourseInfo();
    await loadUnitInfo();
    await loadTopics();
  });

  async function loadCourseInfo() {
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      console.error('加载课程信息失败', e);
    }
  }

  async function loadUnitInfo() {
    try {
      const res = await getRequest('/manage/smartmind/learningunit/loadByUnitCode', { unitCode: unitCode.value });
      if (res && res.data) {
        currentUnit.value = res.data;
      }
    } catch (e) {
      console.error('加载单元信息失败', e);
    }
  }

  async function loadTopics() {
    try {
      const res = await getRequest('/manage/smartmind/discussionTopic/loadByCourseAndUnit', {
        courseId: courseId.value,
        unitCode: unitCode.value,
      });
      if (res && res.data) {
        topicList.value = res.data;
      }
    } catch (e) {
      console.error('加载话题列表失败', e);
    }
  }

  function selectTopic(item) {
    selectedTopic.value = item;
  }

  function onSearch() {
    if (!searchKeyword.value) {
      loadTopics();
      return;
    }
    selectedTopic.value = null;
  }

  function openAIGenerateModal() {
    aiGenerateModalVisible.value = true;
  }

  function openCreateModal() {
    isEditModalCreate.value = true;
    editModalForm.value = {
      id: null,
      topicName: '',
      topicType: '1',
      topicContent: '',
      estimatedDuration: 15,
      groupSize: 4,
      backgroundMaterial: '',
      guidanceTips: '',
      expectedAnswers: '',
      evaluationCriteria: '',
      knowledgePoints: '',
    };
    editModalVisible.value = true;
  }

  function openEditModal() {
    if (!selectedTopic.value) {
      message.warning('请先选择话题');
      return;
    }
    isEditModalCreate.value = false;
    const t = selectedTopic.value;
    editModalForm.value = {
      id: t.id,
      topicName: t.topicName || '',
      topicType: t.topicType || '1',
      topicContent: t.topicContent || '',
      estimatedDuration: t.estimatedDuration || 15,
      groupSize: t.groupSize || 4,
      backgroundMaterial: t.backgroundMaterial || '',
      guidanceTips: t.guidanceTips || '',
      expectedAnswers: t.expectedAnswers || '',
      evaluationCriteria: t.evaluationCriteria || '',
      knowledgePoints: t.knowledgePoints || '',
    };
    editModalVisible.value = true;
  }

  async function handleModalSubmit() {
    try {
      await modalFormRef.value.validateFields();
    } catch (e) {
      return;
    }

    modalSubmitLoading.value = true;
    try {
      const data = {
        topicName: editModalForm.value.topicName,
        courseId: courseId.value,
        courseName: courseInfo.value?.courseName,
        unitId: currentUnit.value?.id,
        unitName: currentUnit.value?.unitName,
        unitCode: unitCode.value,
        topicType: editModalForm.value.topicType,
        topicContent: editModalForm.value.topicContent,
        estimatedDuration: editModalForm.value.estimatedDuration,
        groupSize: editModalForm.value.groupSize,
        backgroundMaterial: editModalForm.value.backgroundMaterial,
        guidanceTips: editModalForm.value.guidanceTips,
        expectedAnswers: editModalForm.value.expectedAnswers,
        evaluationCriteria: editModalForm.value.evaluationCriteria,
        knowledgePoints: editModalForm.value.knowledgePoints,
        status: '1',
      };

      if (isEditModalCreate.value) {
        await postRequest('/manage/smartmind/discussionTopic/create', data);
        message.success('创建成功');
      } else {
        data.id = editModalForm.value.id;
        await postRequest('/manage/smartmind/discussionTopic/update', data);
        message.success('更新成功');
      }
      editModalVisible.value = false;
      await loadTopics();
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    } finally {
      modalSubmitLoading.value = false;
    }
  }

  async function deleteTopic() {
    if (!selectedTopic.value) {
      message.warning('请先选择话题');
      return;
    }
    try {
      await postRequest('/manage/smartmind/discussionTopic/setStatus', {
        id: selectedTopic.value.id,
        status: '0',
      });
      message.success('删除成功');
      selectedTopic.value = null;
      await loadTopics();
    } catch (e) {
      console.error('删除失败', e);
      message.error('删除失败');
    }
  }

  function truncateText(text, maxLength) {
    if (!text) return '';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
  }

  function getTopicTypeColor(type) {
    const map = { '1': 'blue', '2': 'purple', '3': 'orange', '4': 'cyan', '5': 'magenta' };
    return map[type] || 'default';
  }
</script>

<style scoped lang="less">
.discussion-topic-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f7fa;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.left-panel {
  width: 380px;
  min-width: 380px;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.list-title {
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.topic-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.topic-item {
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #e8e8e8;

  &:hover {
    background: #f0f5ff;
    border-color: #91d5ff;
  }

  &.active {
    background: #e6f7ff;
    border-color: #1890ff;
  }
}

.topic-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.topic-item-name {
  font-size: 14px;
  font-weight: 500;
  color: #262626;
  margin-bottom: 4px;
}

.topic-item-content {
  font-size: 12px;
  color: #8c8c8c;
  margin-bottom: 8px;
  line-height: 1.5;
}

.topic-item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.topic-item-meta {
  font-size: 12px;
  color: #bfbfbf;
}

.right-panel {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: #fff;
  margin: 16px;
  border-radius: 8px;
}

.detail-header {
  margin-bottom: 24px;
}

.detail-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #262626;
}

.detail-section {
  margin-bottom: 24px;
}

.section-label {
  font-size: 14px;
  font-weight: 600;
  color: #262626;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e8e8e8;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-label {
  font-size: 13px;
  color: #8c8c8c;
  min-width: 80px;
}

.info-value {
  font-size: 13px;
  color: #262626;
}

.content-display {
  font-size: 14px;
  color: #262626;
  line-height: 1.8;
  padding: 12px;
  background: #fafafa;
  border-radius: 4px;

  &.answer {
    background: #f6ffed;
    border: 1px solid #b7eb8f;
  }
}

.empty-list {
  padding: 40px 0;
}

.empty-detail {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.topic-form {
  .form-row {
    display: flex;
    gap: 16px;

    .form-item-duration {
      flex: 1;
    }

    .form-item-group {
      flex: 1;
    }
  }
}
</style>
