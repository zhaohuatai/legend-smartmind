<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="导出名称" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.exportName" placeholder="请输入导出名称" />
        </a-form-item>
        <a-form-item label="课程ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.courseId" placeholder="请输入课程ID" />
        </a-form-item>
        <a-form-item label="班级ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.classId" placeholder="请输入班级ID" />
        </a-form-item>
        <a-form-item label="单元ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.unitId" placeholder="请输入单元ID" />
        </a-form-item>
        <a-form-item label="学生ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.studentId" placeholder="请输入学生ID" />
        </a-form-item>
        <a-form-item label="文件名" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.fileName" placeholder="请输入文件名" />
        </a-form-item>
        <a-form-item label="导出时间" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.exportTime" placeholder="请选择导出时间" style="width: 200px" />
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
            新建数据导出日志
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
          <template v-else-if="column.dataIndex === 'exportType'">
            {{ dictStore.getDataLabels('export_type', record.exportType) }}
          </template>
          <template v-else-if="column.dataIndex === 'dataType'">
            {{ dictStore.getDataLabels('data_type', record.dataType) }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            {{ dictStore.getDataLabels('export_status', record.status) }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="toEdit(record)" type="link">编辑</a-button>
              <a-popconfirm title="确定要删除吗?" @confirm="toDelete(record)">
                <a-button type="link" danger>删除</a-button>
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

    <DataExportLogFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { dataExportLogApi } from '/@/api/smartmind/dataExportLog-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import DataExportLogFormModal from './dataExportLog-form-modal.vue';

  const dictStore = useDictStore();

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '导出名称',
      dataIndex: 'exportName',
      ellipsis: true,
    },
    {
      title: '导出类型: 1-原始数据, 2-统计报表, 3-图表',
      dataIndex: 'exportType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '数据类型: 1-作答记录, 2-学情报告, 3-热力图, 4-课标达成, 5-能力跃迁',
      dataIndex: 'dataType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '课程ID',
      dataIndex: 'courseId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '班级ID',
      dataIndex: 'classId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '单元ID',
      dataIndex: 'unitId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '学生ID',
      dataIndex: 'studentId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '文件名',
      dataIndex: 'fileName',
      ellipsis: true,
    },
    {
      title: '导出时间',
      dataIndex: 'exportTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '过期时间',
      dataIndex: 'expireTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '状态: 0-生成中, 1-已完成, 2-已过期',
      dataIndex: 'status',
      width: 56,
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      ellipsis: true,
    },
    {
      title: '更新人',
      dataIndex: 'updateBy',
      ellipsis: true,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 150,
    },
  ]);

  const queryFormState = {
    exportName: undefined,
    courseId: undefined,
    classId: undefined,
    unitId: undefined,
    studentId: undefined,
    fileName: undefined,
    exportTime: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

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
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let res = await dataExportLogApi.queryPage(queryForm);
      tableData.value = res.data?.rows || res.data?.list || [];
      total.value = res.data?.total || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function toAdd() {
    formModalRef.value.showModal();
  }

  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  async function toDelete(record) {
    try {
      await dataExportLogApi.delete(record.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
