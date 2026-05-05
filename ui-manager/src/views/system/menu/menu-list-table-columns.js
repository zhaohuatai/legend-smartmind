/*
 *  菜单表格列
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-05-12 19:46:11
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { ref } from 'vue';
export const columns = ref([
  {
    title: '名称',
    dataIndex: 'name',
    key: 'ID',
    width: 220,
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    width: 120,
    align: "center"
  },
  {
    title: '图标',
    dataIndex: 'iconCls',
    width: 100,
  },
  {
    title: '路径',
    dataIndex: 'path',
    ellipsis: true,
    width: 180,
  },
  // {
  //   title: '组件',
  //   dataIndex: 'component',
  //   ellipsis: true,
  //   width: 180,
  // },
  // {
  //   title: '后端权限',
  //   dataIndex: 'permName',
  //   ellipsis: true,
  // },
  // {
  //   title: '前端权限',
  //   dataIndex: 'permId',
  //   ellipsis: true,
  // },
  {
    title: '顺序',
    dataIndex: 'showIndex',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 170,
    align: "center"
  },
]);
