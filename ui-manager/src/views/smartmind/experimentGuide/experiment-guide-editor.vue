<template>
  <div class="experiment-guide-page">
    <!-- 顶部：面包屑 + 指导书操作区 -->
    <div class="header-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <router-link :to="`/smartmind/course/detail?courseId=${courseId}`">{{ courseInfo?.courseName || '课程详情' }}</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>{{ currentUnit?.unitName || '单元' }}</a-breadcrumb-item>
        <a-breadcrumb-item>{{ selectedGuide?.experimentTitle || '实验指导书' }}</a-breadcrumb-item>
      </a-breadcrumb>

      <div class="header-actions" v-if="currentUnit">
        <a-select
          v-model:value="selectedGuideId"
          style="width: 220px"
          placeholder="请选择实验指导书"
          @change="onGuideChange"
        >
          <a-select-option v-for="item in guideList" :key="item.id" :value="item.id">
            {{ item.experimentTitle }}
          </a-select-option>
        </a-select>
        <a-button type="primary" @click="openCreateModal" style="margin-left: 8px">
          <PlusOutlined /> 创建指导书
        </a-button>
        <a-button @click="openEditModal" :disabled="!selectedGuide" style="margin-left: 8px">
          <EditOutlined /> 编辑
        </a-button>
        <a-popconfirm
          title="确定删除该实验指导书吗？"
          @confirm="deleteGuide"
          :disabled="!selectedGuide"
        >
          <a-button danger :disabled="!selectedGuide" style="margin-left: 8px">
            <DeleteOutlined /> 删除
          </a-button>
        </a-popconfirm>
      </div>
    </div>

    <!-- 主体内容：左侧（指导书信息展示+AI Chat） + 右侧（指导书预览） -->
    <div class="main-content">
      <!-- 左侧 -->
      <div class="left-panel">
        <!-- 实验指导书信息只读展示 -->
        <div class="info-section" v-if="selectedGuide">
          <div class="section-header">
            <FileTextOutlined />
            <span>实验指导书信息</span>
          </div>
          <div class="info-content">
            <div class="info-row">
              <span class="info-label">指导书名称：</span>
              <span class="info-value">{{ selectedGuide.guideName }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">实验主题：</span>
              <span class="info-value">{{ selectedGuide.experimentTitle }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">预计课时：</span>
              <span class="info-value">{{ selectedGuide.estimatedHours }} 学时</span>
            </div>
            <div class="info-row" v-if="selectedGuide.experimentObjectives">
              <span class="info-label">实验目的：</span>
              <span class="info-value">{{ selectedGuide.experimentObjectives }}</span>
            </div>
            <div class="info-row" v-if="selectedGuide.experimentPrinciple">
              <span class="info-label">实验原理：</span>
              <span class="info-value">{{ selectedGuide.experimentPrinciple }}</span>
            </div>
            <div class="info-row" v-if="selectedGuide.experimentEquipment">
              <span class="info-label">实验器材：</span>
              <span class="info-value">{{ selectedGuide.experimentEquipment }}</span>
            </div>
            <div class="info-row" v-if="selectedGuide.precautions">
              <span class="info-label">注意事项：</span>
              <span class="info-value">{{ selectedGuide.precautions }}</span>
            </div>
            <div class="info-row" v-if="selectedGuide.reportRequirements">
              <span class="info-label">报告要求：</span>
              <span class="info-value">{{ selectedGuide.reportRequirements }}</span>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div class="info-section empty-state" v-else>
          <a-empty description="请先选择或创建实验指导书" :image="simpleImage" />
        </div>

        <!-- AI Chat -->
        <div class="aichat-section">
          <div class="section-header">
            <RobotOutlined />
            <span>AI智能生成实验指导书详细内容</span>
          </div>
          <ExperimentGuideAIChat
            ref="aiChatRef"
            :course-id="courseId"
            :unit-id="currentUnit?.id"
            :unit-code="unitCode"
            :guide-data="guideForm"
            :guide-id="selectedGuide?.id"
            @generate="onAIGenerate"
            @apply="onAIApply"
          />
        </div>
      </div>

      <!-- 右侧：Markdown 预览/编辑切换 -->
      <div class="right-panel">
        <div class="preview-toolbar">
          <span class="toolbar-title"><FileTextOutlined /> 实验指导书预览</span>
          <div class="toolbar-actions">
            <a-radio-group v-model:value="previewMode" size="small" button-style="solid">
              <a-radio-button value="preview">
                <EyeOutlined /> 预览
              </a-radio-button>
              <a-radio-button value="source">
                <CodeOutlined /> 源码
              </a-radio-button>
            </a-radio-group>
            <a-button type="primary" size="small" @click="saveMainContent" :loading="savingContent" style="margin-left: 12px">
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
            v-model:value="mainContent"
            class="markdown-editor"
            placeholder="在此输入实验指导书Markdown内容..."
            @input="onEditorInput"
          />
        </div>
      </div>
    </div>

    <!-- 实验指导书编辑/新建弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      :title="isEditModalCreate ? '创建实验指导书' : '编辑实验指导书'"
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
            <a-form-item label="指导书名称" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
              <a-input :value="modalExperimentTitleDisplay" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="第几次实验" name="experimentSession" :label-col="{ style: { width: '68px' } }" :wrapper-col="{ style: { width: 'calc(100% - 68px)' } }">
              <a-input-number v-model:value="editModalForm.experimentSession" :min="1" style="width: 100%" @change="onExperimentSessionChange" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="学时" name="estimatedHours" :label-col="{ style: { width: '68px' } }" :wrapper-col="{ style: { width: 'calc(100% - 68px)' } }">
              <a-input-number v-model:value="editModalForm.estimatedHours" :min="1" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-form-item label="内容说明" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.contentDescription" :rows="3" placeholder="请输入实验指导书内容说明，作为AI生成的提示词" />
        </a-form-item>
        <a-form-item :wrapper-col="{ offset: 4 }">
          <a-button type="primary" @click="generateModalBaseInfo" :loading="modalGeneratingBase">
            <ThunderboltOutlined /> AI 生成基础信息
          </a-button>
        </a-form-item>

        <a-divider>AI 生成基础信息</a-divider>

        <a-form-item label="实验目的" name="experimentObjectives" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.experimentObjectives" :rows="3" placeholder="请输入实验目的" />
        </a-form-item>
        <a-form-item label="实验原理" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.experimentPrinciple" :rows="2" placeholder="请输入实验原理" />
        </a-form-item>
        <a-form-item label="实验器材" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.experimentEquipment" :rows="2" placeholder="请输入实验器材/设备" />
        </a-form-item>
        <a-form-item label="注意事项" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.precautions" :rows="2" placeholder="请输入注意事项" />
        </a-form-item>
        <a-form-item label="报告要求" :label-col="{ style: { width: '80px' } }" :wrapper-col="{ style: { width: 'calc(100% - 80px)' } }">
          <a-textarea v-model:value="editModalForm.reportRequirements" :rows="2" placeholder="请输入实验报告要求" />
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
  import ExperimentGuideAIChat from './components/ExperimentGuideAIChat.vue';

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

  // 实验指导书列表
  const guideList = ref([]);
  const selectedGuide = ref(null);
  const selectedGuideId = ref(null);
  const saveLoading = ref(false);
  const savingContent = ref(false);
  const aiChatRef = ref(null);
  const formRef = ref(null);

  // 实验指导书表单
  const guideForm = ref({
    id: null,
    guideCode: '',
    guideName: '',
    courseId: null,
    courseName: '',
    unitId: null,
    unitName: '',
    unitCode: '',
    experimentSession: 1,
    experimentTitle: '',
    estimatedHours: 2,
    experimentObjectives: '',
    experimentPrinciple: '',
    experimentEquipment: '',
    experimentSteps: '',
    precautions: '',
    reportRequirements: '',
    mainContent: '',
    aiGenerateMode: 0,
    status: '0',
  });

  // 主内容
  const mainContent = ref('');
  const previewMode = ref('preview');

  // 表单校验规则
  const formRules = {
    guideName: [{ required: true, message: '请输入指导书名称', trigger: 'blur' }],
    estimatedHours: [{ required: true, message: '请输入预计课时', trigger: 'blur' }],
  };

  // 实验主题显示
  const experimentTitleDisplay = computed(() => {
    if (!currentUnit.value) return '';
    return `${currentUnit.value.unitName} - 第${guideForm.value.experimentSession}次实验`;
  });

  // 渲染 Markdown
  const renderedMarkdown = computed(() => {
    if (!mainContent.value) return '<p class="empty-hint">暂无实验指导书内容，请使用 AI 助手生成或手动编辑</p>';
    try {
      return marked(mainContent.value);
    } catch (e) {
      return mainContent.value;
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
    experimentSession: 1,
    estimatedHours: 2,
    contentDescription: '',
    experimentObjectives: '',
    experimentPrinciple: '',
    experimentEquipment: '',
    precautions: '',
    reportRequirements: '',
  });

  const modalFormRules = {
    experimentSession: [{ required: true, message: '请输入第几次实验', trigger: 'blur' }],
    estimatedHours: [{ required: true, message: '请输入学时', trigger: 'blur' }],
  };

  const modalExperimentTitleDisplay = computed(() => {
    if (!currentUnit.value) return '';
    return `${currentUnit.value.unitName} - 第${editModalForm.value.experimentSession}次实验`;
  });

  onMounted(async () => {
    await loadCourseInfo();
    await loadUnitInfo();
    await loadGuideList();
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

  // 加载实验指导书列表
  async function loadGuideList() {
    try {
      const params = { courseId: courseId.value };
      if (unitCode.value) {
        params.unitCode = unitCode.value;
      }
      const res = await getRequest('/manage/smartmind/experimentGuide/loadByCourseAndUnit', params);
      if (res && res.data) {
        guideList.value = res.data;
        
        // 如果URL中有guideId，优先选中该指导书
        const urlGuideId = route.query.guideId;
        if (urlGuideId && guideList.value.length > 0) {
          const targetGuide = guideList.value.find(p => p.id == urlGuideId);
          if (targetGuide) {
            selectedGuideId.value = targetGuide.id;
            selectGuide(targetGuide);
            return;
          }
        }
        
        // 默认选中第一个
        if (guideList.value.length > 0) {
          selectedGuideId.value = guideList.value[0].id;
          selectGuide(guideList.value[0]);
        }
      }
    } catch (e) {
      console.error('加载实验指导书列表失败', e);
    }
  }

  // 实验指导书选择变化
  function onGuideChange(guideId) {
    const guide = guideList.value.find(p => p.id === guideId);
    if (guide) {
      selectGuide(guide);
      // 更新URL参数
      router.replace({
        query: { ...route.query, guideId: guideId }
      });
    }
  }

  // 选择实验指导书
  async function selectGuide(item) {
    selectedGuide.value = item;
    
    try {
      const res = await postRequest(`/manage/smartmind/experimentGuide/loadById/${item.id}`, {});
      if (res && res.data) {
        const fullGuide = res.data;
        guideForm.value = {
          id: fullGuide.id,
          guideCode: fullGuide.guideCode,
          guideName: fullGuide.guideName,
          courseId: fullGuide.courseId,
          courseName: fullGuide.courseName,
          unitId: fullGuide.unitId,
          unitName: fullGuide.unitName,
          unitCode: fullGuide.unitCode,
          experimentSession: fullGuide.experimentSession,
          experimentTitle: fullGuide.experimentTitle,
          estimatedHours: fullGuide.estimatedHours,
          experimentObjectives: fullGuide.experimentObjectives || '',
          experimentPrinciple: fullGuide.experimentPrinciple || '',
          experimentEquipment: fullGuide.experimentEquipment || '',
          experimentSteps: fullGuide.experimentSteps || '',
          precautions: fullGuide.precautions || '',
          reportRequirements: fullGuide.reportRequirements || '',
          mainContent: fullGuide.mainContent || '',
          aiGenerateMode: fullGuide.aiGenerateMode || 0,
          status: fullGuide.status || '0',
        };
        mainContent.value = fullGuide.mainContent || '';
      }
    } catch (e) {
      console.error('加载实验指导书详情失败', e);
    }
  }

  // 保存实验指导书
  async function saveGuide() {
    if (!guideForm.value.guideName) {
      message.warning('请填写指导书名称');
      return;
    }

    // 将markdown内容同步到表单
    guideForm.value.mainContent = mainContent.value;

    saveLoading.value = true;
    try {
      const url = guideForm.value.id ? '/manage/smartmind/experimentGuide/update' : '/manage/smartmind/experimentGuide/create';
      await postRequest(url, guideForm.value);
      message.success('保存成功');
      await loadGuideList();
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    } finally {
      saveLoading.value = false;
    }
  }

  // 重置表单
  function resetForm() {
    if (selectedGuide.value) {
      selectGuide(selectedGuide.value);
    }
  }

  // AI 生成回调
  function onAIGenerate(content) {
    mainContent.value = content;
    message.success('AI 生成完成');
  }

  function onAIApply(content) {
    mainContent.value = content;
  }

  // 保存实验指导书详细内容
  async function saveMainContent() {
    if (!selectedGuide.value?.id) {
      message.warning('请先选择实验指导书');
      return;
    }
    if (!mainContent.value.trim()) {
      message.warning('实验指导书内容为空');
      return;
    }
    savingContent.value = true;
    try {
      await postRequest('/manage/smartmind/experimentGuide/updateMainContent', {
        guideId: selectedGuide.value.id,
        mainContent: mainContent.value,
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
      experimentSession: guideList.value.length + 1,
      estimatedHours: 2,
      contentDescription: '',
      experimentObjectives: '',
      experimentPrinciple: '',
      experimentEquipment: '',
      precautions: '',
      reportRequirements: '',
    };
    editModalVisible.value = true;
  }

  // 打开编辑弹窗
  function openEditModal() {
    if (!selectedGuide.value) {
      message.warning('请先选择实验指导书');
      return;
    }
    isEditModalCreate.value = false;
    editModalForm.value = {
      id: selectedGuide.value.id,
      experimentSession: selectedGuide.value.experimentSession,
      estimatedHours: selectedGuide.value.estimatedHours,
      contentDescription: '',
      experimentObjectives: selectedGuide.value.experimentObjectives || '',
      experimentPrinciple: selectedGuide.value.experimentPrinciple || '',
      experimentEquipment: selectedGuide.value.experimentEquipment || '',
      precautions: selectedGuide.value.precautions || '',
      reportRequirements: selectedGuide.value.reportRequirements || '',
    };
    editModalVisible.value = true;
  }

  // 第几次实验变化时更新实验主题
  function onExperimentSessionChange() {
    // 实验主题会自动通过computed更新
  }

  // 弹窗中AI生成基础信息
  async function generateModalBaseInfo() {
    modalGeneratingBase.value = true;
    try {
      const experimentTitle = `${currentUnit.value.unitName} - 第${editModalForm.value.experimentSession}次实验`;
      const requestData = {
        courseId: courseId.value,
        unitCode: unitCode.value,
        guideName: experimentTitle,
        experimentSession: editModalForm.value.experimentSession,
        contentDescription: editModalForm.value.contentDescription || '',
      };
      const res = await postRequest('/api/smartmind/ai/experiment-guide/base-info', requestData);
      if (res && res.data) {
        const baseInfo = res.data;
        editModalForm.value.experimentObjectives = baseInfo.experimentObjectives || editModalForm.value.experimentObjectives;
        editModalForm.value.experimentPrinciple = baseInfo.experimentPrinciple || editModalForm.value.experimentPrinciple;
        editModalForm.value.experimentEquipment = baseInfo.experimentEquipment || editModalForm.value.experimentEquipment;
        editModalForm.value.precautions = baseInfo.precautions || editModalForm.value.precautions;
        editModalForm.value.reportRequirements = baseInfo.reportRequirements || editModalForm.value.reportRequirements;
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
      const experimentTitle = `${currentUnit.value.unitName} - 第${editModalForm.value.experimentSession}次实验`;
      
      if (isEditModalCreate.value) {
        // 创建实验指导书
        const newGuide = {
          guideCode: `GUIDE_${courseId.value}_${unitCode.value}_${editModalForm.value.experimentSession}`,
          guideName: experimentTitle,
          courseId: courseId.value,
          courseName: courseInfo.value?.courseName,
          unitId: currentUnit.value?.id,
          unitName: currentUnit.value?.unitName,
          unitCode: unitCode.value,
          experimentSession: editModalForm.value.experimentSession,
          experimentTitle: experimentTitle,
          estimatedHours: editModalForm.value.estimatedHours,
          experimentObjectives: editModalForm.value.experimentObjectives,
          experimentPrinciple: editModalForm.value.experimentPrinciple,
          experimentEquipment: editModalForm.value.experimentEquipment,
          precautions: editModalForm.value.precautions,
          reportRequirements: editModalForm.value.reportRequirements,
          status: '0',
        };
        const res = await postRequest('/manage/smartmind/experimentGuide/create', newGuide);
        if (res) {
          message.success('创建成功');
          editModalVisible.value = false;
          await loadGuideList();
          // 创建成功后更新URL
          const newGuideId = res.data?.id || res.data;
          if (newGuideId) {
            router.replace({
              query: { ...route.query, guideId: newGuideId }
            });
          }
        }
      } else {
        // 更新实验指导书
        const updateData = {
          id: editModalForm.value.id,
          guideCode: selectedGuide.value.guideCode,
          guideName: experimentTitle,
          courseId: courseId.value,
          courseName: courseInfo.value?.courseName,
          unitId: currentUnit.value?.id,
          unitName: currentUnit.value?.unitName,
          unitCode: unitCode.value,
          experimentSession: editModalForm.value.experimentSession,
          experimentTitle: experimentTitle,
          estimatedHours: editModalForm.value.estimatedHours,
          experimentObjectives: editModalForm.value.experimentObjectives,
          experimentPrinciple: editModalForm.value.experimentPrinciple,
          experimentEquipment: editModalForm.value.experimentEquipment,
          precautions: editModalForm.value.precautions,
          reportRequirements: editModalForm.value.reportRequirements,
          status: '0',
        };
        await postRequest('/manage/smartmind/experimentGuide/update', updateData);
        message.success('更新成功');
        editModalVisible.value = false;
        await loadGuideList();
      }
    } catch (e) {
      console.error(isEditModalCreate.value ? '创建失败' : '更新失败', e);
      message.error(isEditModalCreate.value ? '创建失败' : '更新失败');
    } finally {
      modalSubmitLoading.value = false;
    }
  }

  // 删除实验指导书
  async function deleteGuide() {
    if (!selectedGuide.value) {
      message.warning('请先选择实验指导书');
      return;
    }
    try {
      await postRequest('/manage/smartmind/experimentGuide/setStatus', { id: selectedGuide.value.id, status: '-1' });
      message.success('删除成功');
      selectedGuide.value = null;
      selectedGuideId.value = null;
      mainContent.value = '';
      // 删除后移除URL中的guideId
      router.replace({
        query: { ...route.query, guideId: undefined }
      });
      await loadGuideList();
    } catch (e) {
      console.error('删除失败', e);
      message.error('删除失败');
    }
  }
</script>

<style scoped lang="less">
  .experiment-guide-page {
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
