<template>
  <div class="teaching-plan-page">
    <!-- 顶部：面包屑 + 教案操作区 -->
    <div class="header-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <router-link :to="`/smartmind/course/detail?courseId=${courseId}`">{{ courseInfo?.courseName || '课程详情' }}</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>{{ currentUnit?.unitName || '单元' }}</a-breadcrumb-item>
        <a-breadcrumb-item>{{ selectedPlan?.lessonTitle || '教案' }}</a-breadcrumb-item>
      </a-breadcrumb>

      <div class="header-actions" v-if="currentUnit">
        <a-select
          v-model:value="selectedPlanId"
          style="width: 220px"
          placeholder="请选择教案"
          @change="onPlanChange"
        >
          <a-select-option v-for="item in planList" :key="item.id" :value="item.id">
            {{ item.lessonTitle }}
          </a-select-option>
        </a-select>
        <a-button type="primary" @click="openCreateModal" style="margin-left: 8px">
          <PlusOutlined /> 创建教案
        </a-button>
        <a-button @click="openEditModal" :disabled="!selectedPlan" style="margin-left: 8px">
          <EditOutlined /> 编辑
        </a-button>
        <a-popconfirm
          title="确定删除该教案吗？"
          @confirm="deletePlan"
          :disabled="!selectedPlan"
        >
          <a-button danger :disabled="!selectedPlan" style="margin-left: 8px">
            <DeleteOutlined /> 删除
          </a-button>
        </a-popconfirm>
      </div>
    </div>

    <!-- 主体内容：左侧（教案信息展示+AI Chat） + 右侧（教案预览） -->
    <div class="main-content">
      <!-- 左侧 -->
      <div class="left-panel">
        <!-- 教案信息只读展示 -->
        <div class="info-section" v-if="selectedPlan">
          <div class="section-header">
            <FileTextOutlined />
            <span>教案信息</span>
          </div>
          <div class="info-content">
            <div class="info-row">
              <span class="info-label">教案名称：</span>
              <span class="info-value">{{ selectedPlan.planName }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">课时主题：</span>
              <span class="info-value">{{ selectedPlan.lessonTitle }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">预计课时：</span>
              <span class="info-value">{{ selectedPlan.estimatedHours }} 学时</span>
            </div>
            <div class="info-row" v-if="selectedPlan.lessonSummary">
              <span class="info-label">授课内容简介：</span>
              <span class="info-value">{{ selectedPlan.lessonSummary }}</span>
            </div>
            <div class="info-row" v-if="selectedPlan.knowledgeObjectives">
              <span class="info-label">知识目标：</span>
              <span class="info-value">{{ selectedPlan.knowledgeObjectives }}</span>
            </div>
            <div class="info-row" v-if="selectedPlan.abilityObjectives">
              <span class="info-label">能力目标：</span>
              <span class="info-value">{{ selectedPlan.abilityObjectives }}</span>
            </div>
            <div class="info-row" v-if="selectedPlan.literacyObjectives">
              <span class="info-label">素养目标：</span>
              <span class="info-value">{{ selectedPlan.literacyObjectives }}</span>
            </div>
            <div class="info-row" v-if="selectedPlan.keyPoints">
              <span class="info-label">教学重点：</span>
              <span class="info-value">{{ selectedPlan.keyPoints }}</span>
            </div>
            <div class="info-row" v-if="selectedPlan.difficultPoints">
              <span class="info-label">教学难点：</span>
              <span class="info-value">{{ selectedPlan.difficultPoints }}</span>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div class="info-section empty-state" v-else>
          <a-empty description="请先选择或创建教案" :image="simpleImage" />
        </div>

        <!-- AI Chat -->
        <div class="aichat-section">
          <div class="section-header">
            <RobotOutlined />
            <span>AI智能生成教案详细内容</span>
          </div>
          <TeachingPlanAIChat
            ref="aiChatRef"
            :course-id="courseId"
            :unit-id="currentUnit?.id"
            :unit-code="unitCode"
            :plan-data="planForm"
            :plan-id="selectedPlan?.id"
            @generate="onAIGenerate"
            @apply="onAIApply"
          />
        </div>
      </div>

      <!-- 右侧：Markdown 预览/编辑切换 -->
      <div class="right-panel">
        <div class="preview-toolbar">
          <span class="toolbar-title"><FileTextOutlined /> 教案预览</span>
          <div class="toolbar-actions">
            <a-radio-group v-model:value="previewMode" size="small" button-style="solid">
              <a-radio-button value="preview">
                <EyeOutlined /> 预览
              </a-radio-button>
              <a-radio-button value="source">
                <CodeOutlined /> 源码
              </a-radio-button>
            </a-radio-group>
            <a-button type="primary" size="small" @click="saveMarkdownContent" :loading="savingContent" style="margin-left: 12px">
              <SaveOutlined /> 保存
            </a-button>
          </div>
        </div>
        <div class="preview-content">
          <!-- Markdown 预览模式 -->
          <div v-if="previewMode === 'preview'" class="markdown-preview" v-html="renderedMarkdown"></div>
          <!-- Markdown 源码模式 -->
          <a-textarea
            v-else
            v-model:value="markdownContent"
            class="markdown-editor"
            placeholder="在此输入教案Markdown内容..."
            @input="onEditorInput"
          />
        </div>
      </div>
    </div>

    <!-- 教案编辑/新建弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      :title="isEditModalCreate ? '创建教案' : '编辑教案'"
      @ok="handleModalSubmit"
      @cancel="editModalVisible = false"
      width="900px"
      :confirm-loading="modalSubmitLoading"
    >
      <a-form
        :model="editModalForm"
        :rules="modalFormRules"
        ref="modalFormRef"
        layout="horizontal"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
        class="modal-form"
      >
        <a-row :gutter="12">
          <a-col :span="14">
            <a-form-item label="教案名称" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
              <a-input :value="modalLessonTitleDisplay" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="第几次课" name="lessonSession" :label-col="{ style: { width: '68px' } }" :wrapper-col="{ style: { width: 'calc(100% - 68px)' } }">
              <a-input-number v-model:value="editModalForm.lessonSession" :min="1" style="width: 100%" @change="onLessonSessionChange" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="学时" name="estimatedHours" :label-col="{ style: { width: '68px' } }" :wrapper-col="{ style: { width: 'calc(100% - 68px)' } }">
              <a-input-number v-model:value="editModalForm.estimatedHours" :min="1" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-form-item label="内容说明" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.contentDescription" :rows="3" placeholder="请输入教案内容说明，作为AI生成的提示词" />
        </a-form-item>
        <a-form-item :wrapper-col="{ offset: 4 }">
          <a-button type="primary" @click="generateModalBaseInfo" :loading="modalGeneratingBase">
            <ThunderboltOutlined /> AI 生成基础信息
          </a-button>
        </a-form-item>

        <a-divider>AI 生成基础信息</a-divider>

        <a-form-item label="授课简介" name="lessonSummary" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.lessonSummary" :rows="3" placeholder="请输入授课内容简介" />
        </a-form-item>
        <a-form-item label="知识目标" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.knowledgeObjectives" :rows="2" placeholder="请输入知识目标" />
        </a-form-item>
        <a-form-item label="能力目标" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.abilityObjectives" :rows="2" placeholder="请输入能力目标" />
        </a-form-item>
        <a-form-item label="素养目标" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.literacyObjectives" :rows="2" placeholder="请输入素养目标" />
        </a-form-item>
        <a-form-item label="教学重点" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.keyPoints" :rows="2" placeholder="请输入教学重点" />
        </a-form-item>
        <a-form-item label="教学难点" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.difficultPoints" :rows="2" placeholder="请输入教学难点" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
  import { ref, onMounted, computed, nextTick, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { Empty } from 'ant-design-vue';
  import { marked } from 'marked';
  import { getRequest, postRequest } from '/@/lib/axios';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import {
    FileTextOutlined,
    PlusOutlined,
    RobotOutlined,
    SaveOutlined,
    ReloadOutlined,
    FormOutlined,
    EyeOutlined,
    CodeOutlined,
    EditOutlined,
    DeleteOutlined,
    ThunderboltOutlined,
  } from '@ant-design/icons-vue';
  import TeachingPlanAIChat from './components/TeachingPlanAIChat.vue';

  const route = useRoute();
  const router = useRouter();
  const dictStore = useDictStore();
  const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;

  // 路由参数
  const courseId = ref(route.query.courseId);
  const unitCode = ref(route.query.unitCode);

  // 课程和单元信息
  const courseInfo = ref(null);
  const currentUnit = ref(null);

  // 教案列表
  const planList = ref([]);
  const selectedPlan = ref(null);
  const selectedPlanId = ref(null);
  const saveLoading = ref(false);
  const savingContent = ref(false);
  const aiChatRef = ref(null);
  const formRef = ref(null);

  // 教案表单
  const planForm = ref({
    id: null,
    planCode: '',
    planName: '',
    courseId: null,
    courseName: '',
    unitId: null,
    unitName: '',
    unitCode: '',
    lessonHour: 1,
    lessonSession: 1,
    lessonTitle: '',
    lessonSummary: '',
    knowledgeObjectives: '',
    abilityObjectives: '',
    literacyObjectives: '',
    keyPoints: '',
    difficultPoints: '',
    teachingMethods: null,
    teachingTools: null,
    mainContent: '',
    teachingResources: '',
    referenceMaterials: '',
    teachingReflection: '',
    markdownContent: '',
    aiGenerateMode: 0,
    status: '0',
  });

  // Markdown 内容
  const markdownContent = ref('');
  const previewMode = ref('preview');

  // 表单校验规则
  const formRules = {
    planName: [{ required: true, message: '请输入教案名称', trigger: 'blur' }],
    estimatedHours: [{ required: true, message: '请输入预计课时', trigger: 'blur' }],
  };

  // 课时主题显示
  const lessonTitleDisplay = computed(() => {
    if (!currentUnit.value) return '';
    return `${currentUnit.value.unitName} - 第${planForm.value.lessonSession}次课`;
  });

  // 渲染 Markdown
  const renderedMarkdown = computed(() => {
    if (!markdownContent.value) return '<p class="empty-hint">暂无教案内容，请使用 AI 助手生成或手动编辑</p>';
    try {
      return marked(markdownContent.value);
    } catch (e) {
      return markdownContent.value;
    }
  });

  // 弹窗相关
  const editModalVisible = ref(false);
  const isEditModalCreate = ref(false);
  const modalSubmitLoading = ref(false);
  const modalGeneratingBase = ref(false);
  const modalFormRef = ref(null);
  
  const editModalForm = ref({
    id: null,
    lessonSession: 1,
    estimatedHours: 2,
    contentDescription: '',
    lessonSummary: '',
    knowledgeObjectives: '',
    abilityObjectives: '',
    literacyObjectives: '',
    keyPoints: '',
    difficultPoints: '',
  });

  const modalFormRules = {
    lessonSession: [{ required: true, message: '请输入第几次课', trigger: 'blur' }],
    estimatedHours: [{ required: true, message: '请输入学时', trigger: 'blur' }],
  };

  const modalLessonTitleDisplay = computed(() => {
    if (!currentUnit.value) return '';
    return `${currentUnit.value.unitName} - 第${editModalForm.value.lessonSession}次课`;
  });

  onMounted(async () => {
    await loadCourseInfo();
    await loadUnitInfo();
    await loadPlanList();
  });

  // 加载课程信息
  async function loadCourseInfo() {
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      console.error('加载课程信息失败', e);
    }
  }

  // 加载单元信息
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

  // 加载教案列表
  async function loadPlanList() {
    try {
      const params = { courseId: courseId.value };
      if (unitCode.value) {
        params.unitCode = unitCode.value;
      }
      const res = await getRequest('/manage/smartmind/teachingplan/loadByCourseAndUnit', params);
      if (res && res.data) {
        planList.value = res.data;
        
        // 如果URL中有planId，优先选中该教案
        const urlPlanId = route.query.planId;
        if (urlPlanId && planList.value.length > 0) {
          const targetPlan = planList.value.find(p => p.id == urlPlanId);
          if (targetPlan) {
            selectedPlanId.value = targetPlan.id;
            selectPlan(targetPlan);
            return;
          }
        }
        
        // 默认选中第一个
        if (planList.value.length > 0) {
          selectedPlanId.value = planList.value[0].id;
          selectPlan(planList.value[0]);
        }
      }
    } catch (e) {
      console.error('加载教案列表失败', e);
    }
  }

  // 教案选择变化
  function onPlanChange(planId) {
    const plan = planList.value.find(p => p.id === planId);
    if (plan) {
      selectPlan(plan);
      // 更新URL参数
      router.replace({
        query: { ...route.query, planId: planId }
      });
    }
  }

  // 选择教案
  async function selectPlan(item) {
    selectedPlan.value = item;
    
    try {
      const res = await postRequest(`/manage/smartmind/teachingplan/loadById/${item.id}`, {});
      if (res && res.data) {
        const fullPlan = res.data;
        planForm.value = {
          id: fullPlan.id,
          planCode: fullPlan.planCode,
          planName: fullPlan.planName,
          courseId: fullPlan.courseId,
          courseName: fullPlan.courseName,
          unitId: fullPlan.unitId,
          unitName: fullPlan.unitName,
          unitCode: fullPlan.unitCode,
          lessonHour: fullPlan.lessonHour,
          lessonSession: fullPlan.lessonSession,
          lessonTitle: fullPlan.lessonTitle,
          lessonSummary: fullPlan.lessonSummary || '',
          knowledgeObjectives: fullPlan.knowledgeObjectives || '',
          abilityObjectives: fullPlan.abilityObjectives || '',
          literacyObjectives: fullPlan.literacyObjectives || '',
          keyPoints: fullPlan.keyPoints || '',
          difficultPoints: fullPlan.difficultPoints || '',
          teachingMethods: fullPlan.teachingMethods,
          teachingTools: fullPlan.teachingTools,
          mainContent: fullPlan.mainContent || '',
          teachingResources: fullPlan.teachingResources || '',
          referenceMaterials: fullPlan.referenceMaterials || '',
          teachingReflection: fullPlan.teachingReflection || '',
          markdownContent: fullPlan.markdownContent || '',
          aiGenerateMode: fullPlan.aiGenerateMode || 0,
          status: fullPlan.status || '0',
        };
        markdownContent.value = fullPlan.markdownContent || '';
      }
    } catch (e) {
      console.error('加载教案详情失败', e);
    }
  }

  // 保存教案
  async function savePlan() {
    if (!planForm.value.planName) {
      message.warning('请填写教案名称');
      return;
    }

    // 将markdown内容同步到表单
    planForm.value.markdownContent = markdownContent.value;

    saveLoading.value = true;
    try {
      const url = planForm.value.id ? '/manage/smartmind/teachingplan/update' : '/manage/smartmind/teachingplan/create';
      await postRequest(url, planForm.value);
      message.success('保存成功');
      await loadPlanList();
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    } finally {
      saveLoading.value = false;
    }
  }

  // 重置表单
  function resetForm() {
    if (selectedPlan.value) {
      selectPlan(selectedPlan.value);
    }
  }

  // AI 生成回调
  function onAIGenerate(content) {
    markdownContent.value = content;
    message.success('AI 生成完成');
  }

  // AI 应用回调（流式更新右侧预览）
  function onAIApply(content) {
    markdownContent.value = content;
  }

  // 保存教案详细内容
  async function saveMarkdownContent() {
    if (!selectedPlan.value?.id) {
      message.warning('请先选择教案');
      return;
    }
    if (!markdownContent.value.trim()) {
      message.warning('教案内容为空');
      return;
    }
    savingContent.value = true;
    try {
      await postRequest('/manage/smartmind/teachingplan/updateMarkdownContent', {
        planId: selectedPlan.value.id,
        mainContent: markdownContent.value,
      });
      message.success('保存成功');
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    } finally {
      savingContent.value = false;
    }
  }

  // 编辑器输入事件
  function onEditorInput() {
    // 实时更新
  }

  // 打开创建弹窗
  function openCreateModal() {
    isEditModalCreate.value = true;
    editModalForm.value = {
      id: null,
      lessonSession: planList.value.length + 1,
      estimatedHours: 2,
      contentDescription: '',
      lessonSummary: '',
      knowledgeObjectives: '',
      abilityObjectives: '',
      literacyObjectives: '',
      keyPoints: '',
      difficultPoints: '',
    };
    editModalVisible.value = true;
  }

  // 打开编辑弹窗
  function openEditModal() {
    if (!selectedPlan.value) {
      message.warning('请先选择教案');
      return;
    }
    isEditModalCreate.value = false;
    editModalForm.value = {
      id: selectedPlan.value.id,
      lessonSession: selectedPlan.value.lessonSession,
      estimatedHours: selectedPlan.value.estimatedHours,
      contentDescription: '',
      lessonSummary: selectedPlan.value.lessonSummary || '',
      knowledgeObjectives: selectedPlan.value.knowledgeObjectives || '',
      abilityObjectives: selectedPlan.value.abilityObjectives || '',
      literacyObjectives: selectedPlan.value.literacyObjectives || '',
      keyPoints: selectedPlan.value.keyPoints || '',
      difficultPoints: selectedPlan.value.difficultPoints || '',
    };
    editModalVisible.value = true;
  }

  // 第几次课变化时更新课时主题
  function onLessonSessionChange() {
    // 课时主题会自动通过computed更新
  }

  // 弹窗中AI生成基础信息
  async function generateModalBaseInfo() {
    modalGeneratingBase.value = true;
    try {
      const lessonTitle = `${currentUnit.value.unitName} - 第${editModalForm.value.lessonSession}次课`;
      const requestData = {
        courseId: courseId.value,
        unitCode: unitCode.value,
        planName: lessonTitle,
        lessonSession: editModalForm.value.lessonSession,
        contentDescription: editModalForm.value.contentDescription || '',
      };
      const res = await postRequest('/api/smartmind/ai/teaching-plan/base-info', requestData);
      if (res && res.data) {
        const baseInfo = res.data;
        editModalForm.value.lessonSummary = baseInfo.lessonSummary || editModalForm.value.lessonSummary;
        editModalForm.value.knowledgeObjectives = baseInfo.knowledgeObjectives || editModalForm.value.knowledgeObjectives;
        editModalForm.value.abilityObjectives = baseInfo.abilityObjectives || editModalForm.value.abilityObjectives;
        editModalForm.value.literacyObjectives = baseInfo.literacyObjectives || editModalForm.value.literacyObjectives;
        editModalForm.value.keyPoints = baseInfo.keyPoints || editModalForm.value.keyPoints;
        editModalForm.value.difficultPoints = baseInfo.difficultPoints || editModalForm.value.difficultPoints;
        editModalForm.value.estimatedHours = baseInfo.estimatedHours || editModalForm.value.estimatedHours;
        message.success('基础信息生成成功');
      } else {
        message.error('生成失败，请重试');
      }
    } catch (e) {
      console.error('生成基础信息异常:', e);
      message.error('生成失败：' + (e.message || '未知错误'));
    } finally {
      modalGeneratingBase.value = false;
    }
  }

  // 弹窗提交
  async function handleModalSubmit() {
    try {
      await modalFormRef.value.validateFields();
    } catch (e) {
      return;
    }

    modalSubmitLoading.value = true;
    try {
      const lessonTitle = `${currentUnit.value.unitName} - 第${editModalForm.value.lessonSession}次课`;
      
      if (isEditModalCreate.value) {
        // 创建教案
        const newPlan = {
          planCode: `PLAN_${courseId.value}_${unitCode.value}_${editModalForm.value.lessonSession}`,
          planName: lessonTitle,
          courseId: courseId.value,
          courseName: courseInfo.value?.courseName,
          unitId: currentUnit.value?.id,
          unitName: currentUnit.value?.unitName,
          unitCode: unitCode.value,
          lessonSession: editModalForm.value.lessonSession,
          lessonTitle: lessonTitle,
          lessonSummary: editModalForm.value.lessonSummary,
          estimatedHours: editModalForm.value.estimatedHours,
          knowledgeObjectives: editModalForm.value.knowledgeObjectives,
          abilityObjectives: editModalForm.value.abilityObjectives,
          literacyObjectives: editModalForm.value.literacyObjectives,
          keyPoints: editModalForm.value.keyPoints,
          difficultPoints: editModalForm.value.difficultPoints,
          status: '0',
        };
        const res = await postRequest('/manage/smartmind/teachingplan/create', newPlan);
        if (res && res.data && res.data.id) {
          message.success('创建成功');
          editModalVisible.value = false;
          await loadPlanList();
          // 创建成功后更新URL
          router.replace({
            query: { ...route.query, planId: res.data.id }
          });
        }
      } else {
        // 更新教案
        const updateData = {
          id: editModalForm.value.id,
          planCode: selectedPlan.value.planCode,
          planName: lessonTitle,
          courseId: courseId.value,
          courseName: courseInfo.value?.courseName,
          unitId: currentUnit.value?.id,
          unitName: currentUnit.value?.unitName,
          unitCode: unitCode.value,
          lessonSession: editModalForm.value.lessonSession,
          lessonTitle: lessonTitle,
          lessonSummary: editModalForm.value.lessonSummary,
          estimatedHours: editModalForm.value.estimatedHours,
          knowledgeObjectives: editModalForm.value.knowledgeObjectives,
          abilityObjectives: editModalForm.value.abilityObjectives,
          literacyObjectives: editModalForm.value.literacyObjectives,
          keyPoints: editModalForm.value.keyPoints,
          difficultPoints: editModalForm.value.difficultPoints,
        };
        await postRequest('/manage/smartmind/teachingplan/update', updateData);
        message.success('更新成功');
        editModalVisible.value = false;
        await loadPlanList();
      }
    } catch (e) {
      console.error(isEditModalCreate.value ? '创建失败' : '更新失败', e);
      message.error(isEditModalCreate.value ? '创建失败' : '更新失败');
    } finally {
      modalSubmitLoading.value = false;
    }
  }

  // 删除教案
  async function deletePlan() {
    if (!selectedPlan.value) {
      message.warning('请先选择教案');
      return;
    }
    try {
      await postRequest('/manage/smartmind/teachingplan/delete', { id: selectedPlan.value.id });
      message.success('删除成功');
      selectedPlan.value = null;
      selectedPlanId.value = null;
      markdownContent.value = '';
      // 删除后移除URL中的planId
      router.replace({
        query: { ...route.query, planId: undefined }
      });
      await loadPlanList();
    } catch (e) {
      console.error('删除失败', e);
      message.error('删除失败');
    }
  }
</script>

<style scoped lang="less">
  .teaching-plan-page {
    height: calc(100vh - 100px);
    display: flex;
    flex-direction: column;
    padding: 16px;
    background: #f5f7fa;

    .header-bar {
      background: #fff;
      padding: 12px 20px;
      border-radius: 8px;
      margin-bottom: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-actions {
        display: flex;
        align-items: center;
        flex-shrink: 0;
      }
    }

    .main-content {
      flex: 1;
      display: flex;
      gap: 12px;
      min-height: 0;

      .left-panel {
        width: 50%;
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        gap: 12px;

        .info-section {
          background: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
          display: flex;
          flex-direction: column;
          flex: 1;
          overflow: hidden;

          .section-header {
            padding: 12px 16px;
            border-bottom: 1px solid #f0f0f0;
            font-size: 14px;
            font-weight: 500;
            color: #333;
            display: flex;
            align-items: center;
            gap: 8px;
          }

          .info-content {
            flex: 1;
            overflow-y: auto;
            padding: 12px 16px;

            .info-row {
              margin-bottom: 10px;
              line-height: 1.6;

              .info-label {
                font-size: 13px;
                font-weight: 500;
                color: #8c8c8c;
                display: block;
                margin-bottom: 2px;
              }

              .info-value {
                font-size: 13px;
                color: #333;
                display: block;
              }
            }
          }

          &.empty-state {
            display: flex;
            align-items: center;
            justify-content: center;
          }
        }

        .aichat-section {
          background: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
          display: flex;
          flex-direction: column;
          height: 200px;
          overflow: hidden;

          .section-header {
            padding: 12px 16px;
            border-bottom: 1px solid #f0f0f0;
            font-size: 14px;
            font-weight: 500;
            color: #333;
            display: flex;
            align-items: center;
            gap: 8px;
          }
        }
      }

      .right-panel {
        flex: 1;
        min-width: 0;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .preview-toolbar {
          padding: 10px 12px;
          border-bottom: 1px solid #f0f0f0;
          display: flex;
          justify-content: space-between;
          align-items: center;
          background: #fafafa;

          .toolbar-title {
            font-size: 14px;
            color: #333;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 6px;
          }
        }

        .preview-content {
          flex: 1;
          overflow-y: auto;
          padding: 16px;

          .markdown-preview {
            :deep(h1), :deep(h2), :deep(h3), :deep(h4) {
              margin-top: 16px;
              margin-bottom: 8px;
              color: #262626;
            }

            :deep(p) {
              margin-bottom: 8px;
              line-height: 1.8;
              color: #595959;
            }

            :deep(ul), :deep(ol) {
              padding-left: 24px;
              margin-bottom: 8px;
            }

            :deep(li) {
              margin-bottom: 4px;
              line-height: 1.6;
            }

            :deep(table) {
              width: 100%;
              border-collapse: collapse;
              margin-bottom: 12px;

              th, td {
                border: 1px solid #d9d9d9;
                padding: 8px;
                text-align: left;
              }

              th {
                background: #fafafa;
                font-weight: 500;
              }
            }

            .empty-hint {
              color: #8c8c8c;
              text-align: center;
              padding: 40px 0;
            }
          }

          .markdown-editor {
            width: 100%;
            height: 100%;
            border: none;
            resize: none;
            font-family: 'Courier New', monospace;
            font-size: 14px;
            line-height: 1.6;
          }
        }
      }
    }

    .modal-form {
      max-height: 60vh;
      overflow-y: auto;
      padding-right: 8px;

      :deep(.ant-form-item) {
        margin-bottom: 14px;
      }

      :deep(.ant-divider) {
        margin: 16px 0;
      }
    }
  }
</style>
