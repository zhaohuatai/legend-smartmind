<template>
  <a-modal
    v-model:open="visible"
    title="批量导入学生"
    width="900px"
    :footer="null"
    @cancel="onClose"
    :maskClosable="false"
  >
    <div class="import-container">
      <!-- 步骤条 -->
      <a-steps :current="currentStep" class="import-steps">
        <a-step title="粘贴文本" description="复制粘贴学生信息" />
        <a-step title="智能解析" description="AI自动识别" />
        <a-step title="预览确认" description="编辑后提交" />
      </a-steps>

      <!-- 步骤1：粘贴文本 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="paste-area">
          <a-textarea
            v-model:value="pasteText"
            placeholder="请粘贴学生信息文本，例如：&#10;张三 男 13800138001&#10;李四 女 13900139001&#10;支持多种格式，AI将自动识别姓名、性别、电话等信息"
            :rows="10"
            class="paste-textarea"
          />
          <div class="paste-tips">
            <InfoCircleOutlined />
            <span>支持从Excel、Word、记事本等复制粘贴，AI会自动识别关键信息</span>
          </div>
        </div>
        <div class="step-actions">
          <a-button @click="onClose" class="btn-cancel" size="large">
            <span>取消</span>
          </a-button>
          <a-button type="primary" @click="onParse" :loading="parsing" :disabled="!pasteText.trim()" class="btn-primary" size="large">
            <RobotOutlined /> <span>开始解析</span>
          </a-button>
        </div>
      </div>

      <!-- 步骤2：解析中 -->
      <div v-if="currentStep === 1" class="step-content">
        <div class="parsing-area">
          <a-spin size="large" tip="AI正在智能解析学生信息..." />
          <p class="parsing-desc">智能体自动识别姓名、学号、性别、电话、班级等信息</p>
        </div>
      </div>

      <!-- 步骤3：预览编辑 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="preview-area">
          <div class="preview-header">
            <span class="preview-title">解析结果（共 {{ parsedList.length }} 条）</span>
            <a-button type="dashed" size="small" @click="addNewRow" class="btn-add-row">
              <PlusOutlined /> <span>手动添加</span>
            </a-button>
          </div>
          
          <a-table
            :dataSource="parsedList"
            :columns="previewColumns"
            rowKey="id"
            size="small"
            :pagination="false"
            :scroll="{ y: 300 }"
            class="preview-table"
          >
            <template #bodyCell="{ record, column, index }">
              <template v-if="column.dataIndex === 'seq'">
                {{ index + 1 }}
              </template>
              <template v-else-if="column.dataIndex === 'studentName'">
                <a-input v-model:value="record.studentName" size="small" placeholder="姓名" />
              </template>
              <template v-else-if="column.dataIndex === 'studentNo'">
                <a-input v-model:value="record.studentNo" size="small" placeholder="学号" />
              </template>
              <template v-else-if="column.dataIndex === 'gender'">
                <a-select v-model:value="record.gender" size="small" style="width: 70px">
                  <a-select-option value="1">男</a-select-option>
                  <a-select-option value="0">女</a-select-option>
                </a-select>
              </template>
              <template v-else-if="column.dataIndex === 'phone'">
                <a-input v-model:value="record.phone" size="small" placeholder="电话" />
              </template>
              <template v-else-if="column.dataIndex === 'classId'">
                <a-select
                  v-model:value="record.classId"
                  size="small"
                  style="width: 120px"
                  placeholder="选择班级"
                  allowClear
                >
                  <a-select-option v-for="item in classOptions" :key="item.value" :value="item.value">
                    {{ item.label }}
                  </a-select-option>
                </a-select>
              </template>
              <template v-else-if="column.dataIndex === 'action'">
                <a-button type="text" danger size="small" @click="removeRow(index)">
                  <DeleteOutlined />
                </a-button>
              </template>
            </template>
          </a-table>

          <div class="validation-summary" v-if="validationErrors.length > 0">
            <a-alert
              type="warning"
              :message="`有 ${validationErrors.length} 条记录需要完善`"
              :description="validationErrors.join('；')"
              showIcon
            />
          </div>
        </div>

        <div class="step-actions">
          <a-button @click="currentStep = 0" class="btn-cancel" size="large">
            <span>上一步</span>
          </a-button>
          <a-button type="primary" @click="onSubmit" :loading="submitting" :disabled="parsedList.length === 0" class="btn-primary" size="large">
            <span>确认导入 ({{ parsedList.length }}条)</span>
          </a-button>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, computed } from 'vue';
  import { studentApi } from '/@/api/smartmind/student-api.js';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import {
    PlusOutlined,
    DeleteOutlined,
    RobotOutlined,
    InfoCircleOutlined
  } from '@ant-design/icons-vue';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const currentStep = ref(0);
  const pasteText = ref('');
  const parsing = ref(false);
  const submitting = ref(false);
  const parsedList = ref([]);
  const classOptions = ref([]);

  // 预览表格列
  const previewColumns = [
    { title: '序号', dataIndex: 'seq', width: 60, align: 'center' },
    { title: '姓名', dataIndex: 'studentName', width: 100 },
    { title: '学号', dataIndex: 'studentNo', width: 120 },
    { title: '性别', dataIndex: 'gender', width: 80, align: 'center' },
    { title: '电话', dataIndex: 'phone', width: 130 },
    { title: '班级', dataIndex: 'classId', width: 140 },
    { title: '操作', dataIndex: 'action', width: 60, align: 'center' },
  ];

  // 验证错误
  const validationErrors = computed(() => {
    const errors = [];
    parsedList.value.forEach((item, index) => {
      if (!item.studentName?.trim()) {
        errors.push(`第${index + 1}行：姓名不能为空`);
      }
      if (!item.studentNo?.trim()) {
        errors.push(`第${index + 1}行：学号不能为空`);
      }
    });
    return errors;
  });

  // 加载班级选项
  async function loadClassOptions() {
    try {
      const res = await clazzApi.selectVo();
      classOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function showModal() {
    visible.value = true;
    currentStep.value = 0;
    pasteText.value = '';
    parsedList.value = [];
    loadClassOptions();
  }

  function onClose() {
    visible.value = false;
    reset();
  }

  function reset() {
    pasteText.value = '';
    parsedList.value = [];
    currentStep.value = 0;
  }

  // 解析文本
  async function onParse() {
    if (!pasteText.value.trim()) {
      message.warning('请输入要解析的文本');
      return;
    }

    parsing.value = true;
    currentStep.value = 1;

    try {
      const res = await studentApi.parseText({ text: pasteText.value });
      if (res.data && res.data.length > 0) {
        // 为每条记录添加唯一ID
        parsedList.value = res.data.map((item, index) => ({
          ...item,
          id: `temp_${Date.now()}_${index}`,
          gender: item.gender || '1',
          status: '0',
        }));
        currentStep.value = 2;
        message.success(`成功解析 ${parsedList.value.length} 条学生信息`);
      } else {
        message.warning('未能从文本中识别出学生信息，请检查文本格式');
        currentStep.value = 0;
      }
    } catch (e) {
      smartSentry.captureError(e);
      message.error('解析失败，请稍后重试');
      currentStep.value = 0;
    } finally {
      parsing.value = false;
    }
  }

  // 添加新行
  function addNewRow() {
    parsedList.value.push({
      id: `temp_${Date.now()}_${parsedList.value.length}`,
      studentName: '',
      studentNo: '',
      gender: '1',
      phone: '',
      classId: undefined,
      status: '0',
    });
  }

  // 删除行
  function removeRow(index) {
    parsedList.value.splice(index, 1);
  }

  // 提交导入
  async function onSubmit() {
    if (parsedList.value.length === 0) {
      message.warning('没有可导入的数据');
      return;
    }

    // 基础验证
    const invalidItems = parsedList.value.filter(item => !item.studentName?.trim() || !item.studentNo?.trim());
    if (invalidItems.length > 0) {
      message.warning(`有 ${invalidItems.length} 条记录缺少必填信息（姓名、学号）`);
      return;
    }

    submitting.value = true;
    SmartLoading.show();

    try {
      // 清理临时ID，只保留业务字段
      const submitData = parsedList.value.map(({ id, ...rest }) => rest);
      await studentApi.batchImport(submitData);
      message.success(`成功导入 ${parsedList.value.length} 名学生`);
      visible.value = false;
      emit('reloadList');
      reset();
    } catch (e) {
      smartSentry.captureError(e);
      message.error('导入失败，请检查数据后重试');
    } finally {
      submitting.value = false;
      SmartLoading.hide();
    }
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less">
  .import-container {
    padding: 20px 0;

    .import-steps {
      margin-bottom: 30px;
    }

    .step-content {
      min-height: 300px;
    }

    .paste-area {
      .paste-textarea {
        font-size: 14px;
        line-height: 1.6;
      }

      .paste-tips {
        margin-top: 12px;
        color: #666;
        font-size: 13px;

        .anticon {
          margin-right: 6px;
          color: #1890ff;
        }
      }
    }

    .parsing-area {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60px 0;

      .parsing-desc {
        margin-top: 20px;
        color: #666;
      }
    }

    .preview-area {
      .preview-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .preview-title {
          font-weight: 500;
          color: #333;
        }

        .btn-add-row {
          border-radius: 8px;
          border-color: rgba(79, 172, 254, 0.5);
          color: #4facfe;
          background: rgba(255, 255, 255, 0.6);
          transition: all 0.3s ease;

          &:hover {
            background: rgba(79, 172, 254, 0.1);
            border-color: #4facfe;
            color: #00c6fb;
            transform: translateY(-1px);
          }

          .anticon {
            margin-right: 4px;
          }
        }
      }

      .preview-table {
        border: 1px solid #f0f0f0;
        border-radius: 4px;
      }

      .validation-summary {
        margin-top: 16px;
      }
    }

    .step-actions {
      display: flex;
      justify-content: flex-end;
      gap: 16px;
      margin-top: 24px;
      padding-top: 20px;
      border-top: 1px solid rgba(255, 255, 255, 0.5);

      .btn-cancel {
        height: 44px;
        padding: 0 28px;
        font-size: 15px;
        border-radius: 12px;
        background: rgba(255, 255, 255, 0.7);
        backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.8);
        color: #4a5568;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;

        &:hover {
          background: rgba(255, 255, 255, 0.9);
          border-color: rgba(79, 172, 254, 0.5);
          color: #4facfe;
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(79, 172, 254, 0.15);
        }
      }

      .btn-primary {
        height: 44px;
        padding: 0 32px;
        font-size: 15px;
        border-radius: 12px;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        border: none;
        box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
        }

        &:active {
          transform: translateY(0);
        }

        &:disabled {
          background: rgba(255, 255, 255, 0.5);
          border: 1px solid rgba(79, 172, 254, 0.3);
          color: rgba(79, 172, 254, 0.5);
          box-shadow: none;
          cursor: not-allowed;
        }

        .anticon {
          margin-right: 6px;
        }
      }
    }
  }
</style>
