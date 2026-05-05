<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="报告名称" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.reportName" placeholder="请输入报告名称" />
        </a-form-item>
        <a-form-item label="活动ID: 单活动/作业报告时关联" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.activityId" placeholder="请输入活动ID: 单活动/作业报告时关联" />
        </a-form-item>
        <a-form-item label="课程ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.courseId" placeholder="请输入课程ID" />
        </a-form-item>
        <a-form-item label="单元ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.unitId" placeholder="请输入单元ID" />
        </a-form-item>
        <a-form-item label="班级ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.classId" placeholder="请输入班级ID" />
        </a-form-item>
        <a-form-item label="学生ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.studentId" placeholder="请输入学生ID" />
        </a-form-item>
        <a-form-item label="学生姓名" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.studentName" placeholder="请输入学生姓名" />
        </a-form-item>
        <a-form-item label="统计开始日期" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.startDate" placeholder="请选择统计开始日期" style="width: 200px" />
        </a-form-item>
        <a-form-item label="统计结束日期" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.endDate" placeholder="请选择统计结束日期" style="width: 200px" />
        </a-form-item>
        <a-form-item label="生成时间" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.generateTime" placeholder="请选择生成时间" style="width: 200px" />
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
            新建学情报告
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
          <template v-else-if="column.dataIndex === 'reportType'">
            {{ dictStore.getDataLabels('report_type', record.reportType) }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            {{ dictStore.getDataLabels('common_status', record.status) }}
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

    <LearningReportFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { learningReportApi } from '/@/api/smartmind/learningReport-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import LearningReportFormModal from './learningReport-form-modal.vue';

  const dictStore = useDictStore();

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '报告名称',
      dataIndex: 'reportName',
      ellipsis: true,
    },
    {
      title: '报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告',
      dataIndex: 'reportType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '活动ID: 单活动/作业报告时关联',
      dataIndex: 'activityId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '课程ID',
      dataIndex: 'courseId',
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
      title: '班级ID',
      dataIndex: 'classId',
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
      title: '统计开始日期',
      dataIndex: 'startDate',
      width: 120,
      ellipsis: true,
    },
    {
      title: '统计结束日期',
      dataIndex: 'endDate',
      width: 120,
      ellipsis: true,
    },
    {
      title: '生成时间',
      dataIndex: 'generateTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '状态: 0-草稿, 1-已生成',
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
    reportName: undefined,
    activityId: undefined,
    courseId: undefined,
    unitId: undefined,
    classId: undefined,
    studentId: undefined,
    studentName: undefined,
    startDate: undefined,
    endDate: undefined,
    generateTime: undefined,
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
      let res = await learningReportApi.queryPage(queryForm);
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
      await learningReportApi.delete(record.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
