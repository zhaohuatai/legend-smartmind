<template>
  <a-modal
    :open="open"
    title="创建课堂"
    @ok="handleCreate"
    @cancel="handleCancel"
    width="600px"
    :confirm-loading="loading"
    :mask-closable="false"
    ok-text="创建"
  >
    <a-form :model="form" layout="vertical" class="create-form">
      <a-form-item label="课堂名称">
        <a-input :value="generatedSessionName" disabled placeholder="自动生成" />
      </a-form-item>

      <a-form-item label="关联单元" required>
        <a-tree-select
          v-model:value="form.unitCodes"
          :tree-data="unitTreeData"
          tree-checkable
          :show-checked-strategy="TreeSelect.SHOW_PARENT"
          placeholder="请选择单元"
          style="width: 100%"
          :field-names="{ children: 'children', label: 'title', value: 'value' }"
        />
      </a-form-item>

      <a-form-item label="上课班级" required>
        <a-select
          v-model:value="form.clazzId"
          placeholder="请选择上课班级"
          style="width: 100%"
          :options="clazzOptions"
          :field-names="{ label: 'className', value: 'classId' }"
        />
      </a-form-item>

      <a-form-item label="上课时间" required>
        <a-date-picker
          v-model:value="form.startTime"
          show-time
          format="YYYY-MM-DD HH:mm"
          placeholder="选择上课时间"
          style="width: 100%"
        />
      </a-form-item>

      <a-form-item label="课堂时长(分钟)" required>
        <a-input-number v-model:value="form.duration" :min="10" :max="180" style="width: 100%" addon-after="分钟" />
      </a-form-item>

      <a-form-item label="是否发布" required>
        <a-radio-group v-model:value="form.published">
          <a-radio :value="true">是</a-radio>
          <a-radio :value="false">否</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { TreeSelect } from 'ant-design-vue';
import { postRequest } from '/@/lib/axios';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  courseId: {
    type: [String, Number],
    default: null,
  },
  courseInfo: {
    type: Object,
    default: null,
  },
  unitTreeData: {
    type: Array,
    default: () => [],
  },
  unitOptions: {
    type: Array,
    default: () => [],
  },
  clazzOptions: {
    type: Array,
    default: () => [],
  },
  teacherId: {
    type: [String, Number],
    default: null,
  },
  teacherName: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:open', 'success']);

const loading = ref(false);

const form = ref({
  sessionName: '',
  unitCodes: [],
  clazzId: null,
  startTime: null,
  duration: 45,
  published: false,
});

const generatedSessionName = computed(() => {
  const courseName = props.courseInfo?.courseName || '';
  const selectedClass = props.clazzOptions.find(c => c.classId === form.value.clazzId);
  const className = selectedClass?.className || '';
  const dateTime = form.value.startTime ? dayjs(form.value.startTime).format('YYYY-MM-DD HH:mm') : '';
  if (courseName && className && dateTime) {
    return `${courseName}-${className}-${dateTime}`;
  }
  return '';
});

watch(() => props.open, (val) => {
  if (val) {
    form.value = {
      sessionName: '',
      unitCodes: [],
      clazzId: null,
      startTime: null,
      duration: 45,
      published: false,
    };
  }
});

function handleCancel() {
  emit('update:open', false);
}

async function handleCreate() {
  if (!generatedSessionName.value) {
    message.warning('请选择上课班级和上课时间');
    return;
  }
  if (!form.value.unitCodes || form.value.unitCodes.length === 0) {
    message.warning('请选择关联单元');
    return;
  }
  if (!form.value.clazzId) {
    message.warning('请选择上课班级');
    return;
  }
  if (!form.value.startTime) {
    message.warning('请选择上课时间');
    return;
  }

  loading.value = true;
  try {
    const unitIds = form.value.unitCodes.map(v => {
      const match = String(v).match(/^unit-(\d+)$/);
      return match ? match[1] : null;
    }).filter(id => id !== null);

    const clazz = props.clazzOptions.find(c => c.classId === form.value.clazzId);

    const param = {
      sessionName: generatedSessionName.value,
      courseId: props.courseId,
      courseName: props.courseInfo?.courseName,
      clazzId: form.value.clazzId,
      clazzName: clazz?.className,
      teacherId: props.teacherId,
      teacherName: props.teacherName,
      unitIds: unitIds.join(','),
      startTime: dayjs(form.value.startTime).format('YYYY-MM-DD HH:mm:ss'),
      duration: form.value.duration,
      status: form.value.published ? '1' : '0',
    };

    await postRequest('/manage/smartmind/classsession/create', param);
    message.success('创建成功');
    emit('update:open', false);
    emit('success');
  } catch (e) {
    smartSentry.captureError(e);
    message.error('创建失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.create-form {
  padding-top: 8px;
}
</style>
