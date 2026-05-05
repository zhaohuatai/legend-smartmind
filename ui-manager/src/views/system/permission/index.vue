<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="权限名称" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.name" placeholder="请输入权限名称" />
        </a-form-item>
        <a-form-item label="权限编码" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.code" placeholder="请输入权限编码" />
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
          <a-button @click="toAdd" type="primary" class="smart-margin-left20">
            <template #icon>
              <PlusOutlined />
            </template>
            新建权限
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="id" :pagination="false">
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.dataIndex === 'seq'">
            {{ index + 1 }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <a-tag :color="record.status == '1' ? 'green' : 'red'">
              {{ record.status == '1' ? '正常' : '禁用' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="toEdit(record)" type="link">编辑</a-button>
              <a-popconfirm title="确定要更新状态吗?" @confirm="updateStatus(record)">
                  <a-button type="link" danger>{{ record.status == '1' ? '禁用' : '启用' }}</a-button>
              </a-popconfirm>
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

    <PermissionFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { permissionApi } from '/@/api/system/permission-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import PermissionFormModal from './permission-form-modal.vue';

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '权限名称',
      dataIndex: 'name',
    },
    {
      title: '权限编码',
      dataIndex: 'code',
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
    },
    {
      title: '备注',
      dataIndex: 'remark',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 150,
    },
  ]);

  const queryForm = reactive({
    name: '',
    code: '',
    pageNum: 1,
    pageSize: 10,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const formModalRef = ref();

  onMounted(() => {
    queryData();
  });

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  function resetQuery() {
    queryForm.name = '';
    queryForm.code = '';
    onSearch();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let result = await permissionApi.queryPage(queryForm);
      tableData.value = result.data.list;
      total.value = result.data.total;
    } catch (e) {
      // smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function toAdd() {
    formModalRef.value.show();
  }

  function toEdit(record) {
    formModalRef.value.show(record);
  }

  async function updateStatus(record) {
      try {
          const newStatus = record.status == '1' ? '0' : '1';
          await permissionApi.updateStatus(record.id, newStatus);
          message.success('状态更新成功');
          queryData();
      } catch (e) {
          // console.error(e);
      }
  }
</script>
