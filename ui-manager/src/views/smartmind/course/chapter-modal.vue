<template>
  <a-modal
    :open="visible"
    title="课程章节管理"
    :footer="null"
    @cancel="onClose"
    :width="1000"
    :bodyStyle="{ minHeight: '700px' }"
  >
    <div class="chapter-container">
      <!-- 顶部操作栏 -->
      <div class="chapter-header">
        <div class="course-info">
          <span class="label">课程：</span>
          <span class="value">{{ currentCourse?.courseName }}</span>
        </div>
        <div class="header-actions">
          <a-button type="default" @click="openGenerateModal" style="margin-right: 8px;">
            <BranchesOutlined />
            从知识结构生成
          </a-button>
          <a-button type="primary" @click="toAddChapter">
            <PlusOutlined />
            添加章节
          </a-button>
        </div>
      </div>

      <!-- 章节树形列表 -->
      <a-table
        :loading="loading"
        :dataSource="chapterList"
        :columns="columns"
        rowKey="id"
        size="small"
        :pagination="false"
        defaultExpandAllRows
        :scroll="{ y: 400 }"
      >
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'unitName'">
            <span :style="{ paddingLeft: (getNodeLevel(record) - 1) * 20 + 'px' }">
              <FileOutlined v-if="record.unitLevel === '3'" />
              <FolderOutlined v-else />
              {{ record.unitName }}
            </span>
          </template>
          <template v-if="column.dataIndex === 'unitLevel'">
            <a-tag :color="getLevelColor(record.unitLevel)">
              {{ getLevelText(record.unitLevel) }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'estimatedHours'">
            {{ record.estimatedHours || '-' }} 课时
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <a-tag :color="record.status === '1' ? 'success' : 'default'">
              {{ record.status === '1' ? '有效' : '失效' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-button type="link" size="small" @click="toEdit(record)">编辑</a-button>
            <a-button type="link" size="small" @click="toAddSubChapter(record)" v-if="record.unitLevel !== '3'">添加子章节</a-button>
            <a-popconfirm title="确定要删除吗?" @confirm="toDelete(record)">
              <a-button type="link" danger size="small">删除</a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 章节表单弹窗 -->
    <a-modal
      :open="formVisible"
      :title="formTitle"
      @ok="handleFormSubmit"
      @cancel="formVisible = false"
      :confirmLoading="formLoading"
      :width="700"
      :bodyStyle="{ maxHeight: '600px', overflow: 'auto' }"
    >
      <a-form :model="formData" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
        <a-form-item label="章节名称" required>
          <a-input v-model:value="formData.unitName" placeholder="请输入章节名称" />
        </a-form-item>
        <a-form-item label="章节层级" required>
          <a-select v-model:value="formData.unitLevel" placeholder="请选择章节层级">
            <a-select-option value="1">大单元</a-select-option>
            <a-select-option value="2">子单元</a-select-option>
            <a-select-option value="3">课时</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="上级章节" v-if="formData.parentId !== 0">
          <a-input :value="parentName" disabled />
        </a-form-item>
        <a-form-item label="预计课时">
          <a-input-number v-model:value="formData.estimatedHours" :min="0" placeholder="请输入预计课时" style="width: 100%" />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number v-model:value="formData.sortOrder" :min="0" placeholder="请输入排序序号" style="width: 100%" />
        </a-form-item>
        <a-form-item label="章节描述">
          <a-textarea v-model:value="formData.unitDesc" :rows="3" placeholder="请输入章节描述" />
        </a-form-item>
        <a-form-item label="章节目标">
          <a-textarea v-model:value="formData.unitObjectives" :rows="3" placeholder="请输入章节目标（知识、能力、素养）" />
        </a-form-item>
        <a-form-item label="状态">
          <a-radio-group v-model:value="formData.status">
            <a-radio value="1">有效</a-radio>
            <a-radio value="0">失效</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 从知识结构生成学习单元弹窗 -->
    <a-modal
      :open="generateModalVisible"
      title="从知识结构生成学习单元"
      @ok="handleGenerateSubmit"
      @cancel="closeGenerateModal"
      :confirmLoading="generateLoading"
      :width="900"
      :bodyStyle="{ minHeight: '500px' }"
    >
      <div class="generate-container">
        <a-alert
          message="提示"
          description="以下是从知识结构解析的学习单元大纲，您可以直接编辑Markdown内容，确认后将生成学习单元。"
          type="info"
          show-icon
          style="margin-bottom: 16px;"
        />
        <a-form-item label="生成层级" style="margin-bottom: 16px;">
          <a-radio-group v-model:value="generateLevel">
            <a-radio :value="2">两层（大单元、子单元）</a-radio>
            <a-radio :value="3">三层（大单元、子单元、课时）</a-radio>
          </a-radio-group>
        </a-form-item>
        <div class="markdown-editor-wrapper">
          <div class="editor-label">知识框架编辑（Markdown）</div>
          <a-textarea
            v-model:value="markdownContent"
            class="markdown-textarea"
            :rows="18"
            placeholder="知识框架内容..."
          />
        </div>
      </div>
    </a-modal>
  </a-modal>
</template>

<script setup>
  import { ref, computed, watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined, FileOutlined, FolderOutlined, BranchesOutlined } from '@ant-design/icons-vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { learningUnitApi } from '/@/api/smartmind/learning-unit-api.js';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const loading = ref(false);
  const currentCourse = ref(null);
  const chapterList = ref([]);

  // 从知识结构生成相关
  const generateModalVisible = ref(false);
  const generateLoading = ref(false);
  const markdownContent = ref('');
  const generateLevel = ref(3); // 默认三层：大单元、子单元、课时

  // 打开生成弹窗
  async function openGenerateModal() {
    if (!currentCourse.value?.id) {
      message.error('课程信息不存在');
      return;
    }
    generateLoading.value = true;
    try {
      const res = await learningUnitApi.queryKnowledgeFramework(currentCourse.value.id);
      if (res.data) {
        markdownContent.value = res.data;
        generateModalVisible.value = true;
      } else {
        message.error('获取知识结构失败');
      }
    } catch (e) {
      smartSentry.captureError(e);
      // 错误提示由框架全局拦截器处理
    } finally {
      generateLoading.value = false;
    }
  }

  // 关闭生成弹窗
  function closeGenerateModal() {
    generateModalVisible.value = false;
    markdownContent.value = '';
    generateLevel.value = 3; // 重置为默认值
  }

  // 提交生成
  async function handleGenerateSubmit() {
    if (!markdownContent.value.trim()) {
      message.error('知识框架内容不能为空');
      return;
    }
    generateLoading.value = true;
    try {
      await learningUnitApi.createFromFramework(currentCourse.value.id, markdownContent.value, generateLevel.value);
      message.success('学习单元生成成功');
      generateModalVisible.value = false;
      markdownContent.value = '';
      generateLevel.value = 3; // 重置为默认值
      queryChapters(); // 刷新列表
    } catch (e) {
      smartSentry.captureError(e);
      // 错误提示由框架全局拦截器处理
    } finally {
      generateLoading.value = false;
    }
  }

  const columns = [
    {
      title: '章节名称',
      dataIndex: 'unitName',
      ellipsis: true,
    },
    {
      title: '层级',
      dataIndex: 'unitLevel',
      width: 100,
    },
    {
      title: '预计课时',
      dataIndex: 'estimatedHours',
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 200,
    },
  ];

  // 表单相关
  const formVisible = ref(false);
  const formLoading = ref(false);
  const isEdit = ref(false);
  const formData = ref({
    id: null,
    unitName: '',
    courseId: null,
    parentId: 0,
    unitLevel: '1',
    sortOrder: 0,
    unitDesc: '',
    unitObjectives: '',
    estimatedHours: null,
    status: '1',
  });

  const formTitle = computed(() => (isEdit.value ? '编辑章节' : '添加章节'));
  const parentName = computed(() => {
    if (formData.value.parentId === 0) return '无（根章节）';
    const parent = findChapterById(chapterList.value, formData.value.parentId);
    return parent ? parent.unitName : '未知';
  });

  function findChapterById(list, id) {
    for (const item of list) {
      if (item.id === id) return item;
      if (item.children && item.children.length > 0) {
        const found = findChapterById(item.children, id);
        if (found) return found;
      }
    }
    return null;
  }

  // 计算节点层级
  function getNodeLevel(record) {
    return parseInt(record.unitLevel) || 1;
  }

  function getLevelColor(level) {
    const colors = { '1': 'blue', '2': 'green', '3': 'orange' };
    return colors[level] || 'default';
  }

  function getLevelText(level) {
    const texts = { '1': '大单元', '2': '子单元', '3': '课时' };
    return texts[level] || '未知';
  }

  function showModal(course) {
    currentCourse.value = course;
    visible.value = true;
    queryChapters();
  }

  function onClose() {
    visible.value = false;
    currentCourse.value = null;
    chapterList.value = [];
  }

  async function queryChapters() {
    if (!currentCourse.value?.id) return;
    
    loading.value = true;
    try {
      const res = await learningUnitApi.queryByCourseId(currentCourse.value.id);
      // 将扁平数据转换为树形结构
      chapterList.value = buildTree(res.data || []);
    } catch (e) {
      smartSentry.captureError(e);
      // 错误提示由框架全局拦截器处理
    } finally {
      loading.value = false;
    }
  }

  function buildTree(list) {
    const map = {};
    const roots = [];
    
    // 先创建所有节点的映射
    list.forEach(item => {
      map[item.id] = { ...item, children: [] };
    });
    
    // 构建树形结构
    list.forEach(item => {
      if (item.parentId === 0 || !map[item.parentId]) {
        roots.push(map[item.id]);
      } else {
        const parent = map[item.parentId];
        if (parent) {
          parent.children.push(map[item.id]);
        }
      }
    });
    
    // 按排序序号排序
    roots.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0));
    roots.forEach(node => {
      if (node.children) {
        node.children.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0));
      }
    });
    
    return roots;
  }

  function resetForm() {
    formData.value = {
      id: null,
      unitName: '',
      courseId: currentCourse.value?.id,
      parentId: 0,
      unitLevel: '1',
      sortOrder: 0,
      unitDesc: '',
      unitObjectives: '',
      estimatedHours: null,
      status: '1',
    };
  }

  function toAddChapter() {
    isEdit.value = false;
    resetForm();
    formVisible.value = true;
  }

  function toAddSubChapter(parent) {
    isEdit.value = false;
    resetForm();
    formData.value.parentId = parent.id;
    // 根据父级自动设置层级
    const parentLevel = parseInt(parent.unitLevel);
    formData.value.unitLevel = String(Math.min(parentLevel + 1, 3));
    formVisible.value = true;
  }

  function toEdit(record) {
    isEdit.value = true;
    formData.value = { ...record };
    formVisible.value = true;
  }

  async function toDelete(record) {
    try {
      await learningUnitApi.delete(record.id);
      message.success('删除成功');
      queryChapters();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
      // 错误提示由框架全局拦截器处理
    }
  }

  async function handleFormSubmit() {
    if (!formData.value.unitName) {
      message.error('请填写章节名称');
      return;
    }

    formLoading.value = true;
    try {
      const data = {
        ...formData.value,
        courseId: currentCourse.value.id,
        courseName: currentCourse.value.courseName,
      };

      if (isEdit.value) {
        await learningUnitApi.update(data);
        message.success('更新成功');
      } else {
        await learningUnitApi.add(data);
        message.success('添加成功');
      }
      
      formVisible.value = false;
      queryChapters();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
      // 错误提示由框架全局拦截器处理
    } finally {
      formLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less">
  .chapter-container {
    .chapter-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .course-info {
        .label {
          color: #666;
        }
        .value {
          font-weight: 500;
          color: #333;
        }
      }
      
      .header-actions {
        display: flex;
        align-items: center;
      }
    }
  }
  
  .generate-container {
    .markdown-editor-wrapper {
      .editor-label {
        font-weight: 500;
        margin-bottom: 8px;
        color: #333;
      }
      
      .markdown-textarea {
        font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
        font-size: 14px;
        line-height: 1.6;
      }
    }
  }
</style>
