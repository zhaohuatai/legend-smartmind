<template>
  <a-modal :open="visible" :title="form.id ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="租户" name="tenantId">
        <a-select v-model:value="form.tenantId" placeholder="请选择租户" show-search :filter-option="filterOption">
            <a-select-option v-for="item in tenantList" :key="item.id" :value="item.id">
                {{ item.name }}
            </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="配置Key" name="configKey">
        <a-input v-model:value="form.configKey" :disabled="Boolean(form.id)" placeholder="请输入配置Key" />
      </a-form-item>
      <a-form-item label="配置值" name="configValue">
        <a-input v-model:value="form.configValue" placeholder="请输入配置值" />
      </a-form-item>
      <a-form-item label="数据类型" name="dataType">
        <a-input v-model:value="form.dataType" placeholder="请输入数据类型" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, onMounted } from 'vue';
  import { configTenantApi } from '/@/api/system/config-tenant-api.js';
  import { tenantApi } from '/@/api/system/tenant-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  const emit = defineEmits(['reloadList']);

  const formRef = ref();
  const formDefault = {
    id: undefined,
    tenantId: undefined,
    configKey: '',
    configValue: '',
    dataType: '',
    remark: '',
  };
  let form = reactive({ ...formDefault });

  const rules = {
    tenantId: [{ required: true, message: '请选择租户' }],
    configKey: [{ required: true, message: '请输入配置Key' }],
    configValue: [{ required: true, message: '请输入配置值' }],
  };

  const visible = ref(false);
  const tenantList = ref([]);

  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
    }
    visible.value = true;
    loadTenantList();
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  async function loadTenantList() {
    try {
        const res = await tenantApi.searchTenant();
        tenantList.value = res.data;
    } catch (e) {
        smartSentry.captureError(e);
    }
  }

  function filterOption(input, option) {
    return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let param = {
            id: form.id,
            tenantId: form.tenantId,
            configKey: form.configKey,
            configValue: form.configValue,
            dataType: form.dataType,
            remark: form.remark,
          };
          if (param.id) {
            await configTenantApi.update(param);
          } else {
            delete param.id;
            await configTenantApi.add(param);
          }
          message.success(`${form.id ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch(() => {
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  defineExpose({
    showModal,
  });
</script>
