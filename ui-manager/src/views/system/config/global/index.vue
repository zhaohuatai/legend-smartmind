<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.remark" placeholder="请输入关键字" />
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery">
              <template #icon>
                <ReloadOutlined />
              </template>
              重置
            </a-button>
          </a-button-group>
          <a-button @click="toEditOrAdd()" type="primary" class="smart-margin-left20">
            <template #icon>
              <PlusOutlined />
            </template>
            新建
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row justify="end">
        <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SYSTEM.CONFIG_GLOBAL" :refresh="queryData" />
      </a-row>

      <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="id" :pagination="false">
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.dataIndex === 'seq'">
            {{ index + 1 }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            {{ getStatusLabel(record.status) || record.status }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="toEditOrAdd(record)" type="link">编辑</a-button>
              <a-button @click="onDelete(record)" type="link" danger>删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <ConfigGlobalFormModal ref="formModalRef" @reloadList="resetQuery" />
  </div>
</template>

<script setup>
  import { message, Modal } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { configGlobalApi } from '/@/api/system/config-global-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import ConfigGlobalFormModal from './config-global-form-modal.vue';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '配置Key',
      dataIndex: 'configKey',
      ellipsis: true,
    },
    {
      title: '配置值',
      dataIndex: 'configValue',
      ellipsis: true,
    },
    {
      title: '数据类型',
      dataIndex: 'dataType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
      width: 250,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 110,
    },
  ]);

  const queryFormState = {
    remark: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  const dictStore = useDictStore();
  function getStatusLabel(status) {
    return dictStore.getDataLabels('sys_normal_disable', status);
  }

  function resetQuery() {
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let res = await configGlobalApi.queryPage(queryForm);
      tableData.value = res.data?.rows || res.data?.list || [];
      total.value = res.data?.total || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const formModalRef = ref();
  function toEditOrAdd(rowData) {
    formModalRef.value.showModal(rowData);
  }

  function onDelete(rowData) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除这条数据吗?',
      okText: '删除',
      okType: 'danger',
      cancelText: '取消',
      onOk() {
        requestDelete(rowData);
      },
    });
  }

  async function requestDelete(rowData) {
    SmartLoading.show();
    try {
      await configGlobalApi.delete(rowData.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  onMounted(queryData);
</script>
