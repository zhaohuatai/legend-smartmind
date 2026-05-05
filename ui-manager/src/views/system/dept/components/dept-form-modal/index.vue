<template>
  <a-modal
    :title="form.id ? '编辑部门' : '添加部门'"
    :width="600"
    :open="modalVisible"
    @cancel="onClose"
    :footer="null"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :labelCol="{ span: 6 }">
      <a-form-item label="上级部门" name="parentId">
        <a-tree-select
          v-model:value="form.parentId"
          style="width: 100%"
          :tree-data="treeData"
          placeholder="请选择上级部门"
          allow-clear
          tree-default-expand-all
          :fieldNames="{ children: 'children', label: 'name', value: 'id' }"
          :disabled="!!form.id"
        />
      </a-form-item>
      <a-form-item label="部门名称" name="name">
        <a-input v-model:value="form.name" placeholder="请输入部门名称" />
      </a-form-item>
      <a-form-item label="部门编码" name="code">
        <a-input v-model:value="form.code" placeholder="请输入部门编码" />
      </a-form-item>
       <a-form-item label="负责人" name="manager">
        <a-input v-model:value="form.manager" placeholder="请输入负责人" />
      </a-form-item>
      <a-form-item label="联系电话" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入联系电话" />
      </a-form-item>
      <a-form-item label="邮箱" name="email">
        <a-input v-model:value="form.email" placeholder="请输入邮箱" />
      </a-form-item>
      <a-form-item label="显示排序" name="showIndex">
        <a-input-number v-model:value="form.showIndex" style="width: 100%" :min="0" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" />
      </a-form-item>
    </a-form>

    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" :loading="submitLoading" @click="submitForm">提交</a-button>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive, defineExpose, defineEmits } from 'vue';
import { message } from 'ant-design-vue';
import { departmentApi } from '/@/api/system/department-api';

const emits = defineEmits(['refresh']);

const modalVisible = ref(false);
const submitLoading = ref(false);
const treeData = ref([]);

const formDefault = {
  id: undefined,
  parentId: undefined,
  name: '',
  code: '',
  manager: '',
  phone: '',
  email: '',
  showIndex: 0,
  remark: ''
};

const form = reactive({ ...formDefault });
const formRef = ref();

const rules = {
  parentId: [{ required: true, message: '请选择上级部门', trigger: 'change' }],
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  showIndex: [{ required: true, message: '请输入排序', trigger: 'blur' }]
};

function showModal(data, parentId) {
  Object.assign(form, formDefault);
  if (data) {
    Object.assign(form, data);
  }
  if (parentId) {
    form.parentId = parentId;
  }
  loadTree();
  modalVisible.value = true;
}

function onClose() {
  modalVisible.value = false;
  Object.assign(form, formDefault);
}

async function loadTree() {
  try {
    const res = await departmentApi.queryDeptTree();
    if (res.data) {
        if (Array.isArray(res.data)) {
            treeData.value = res.data;
        } else {
            treeData.value = [res.data];
        }
    } else {
        treeData.value = [];
    }
  } catch (e) {
    console.error(e);
  }
}

async function submitForm() {
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    if (form.id) {
      await departmentApi.updateDepartment(form);
    } else {
      await departmentApi.addDepartment(form);
    }
    message.success('操作成功');
    emits('refresh');
    onClose();
  } catch (e) {
    console.error(e);
  } finally {
    submitLoading.value = false;
  }
}

defineExpose({
  showModal
});
</script>

<style scoped>
.footer {
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>
