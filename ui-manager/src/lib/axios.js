/*
 *  ajax请求
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:46:03
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { message, Modal } from 'ant-design-vue';
import axios from 'axios';
import { localRead } from '/@/utils/local-util';
import { useUserStore } from '/@/store/modules/system/user';
import { decryptData, encryptData } from './encrypt';
import { DATA_TYPE_ENUM } from '../constants/common-const';
import _ from 'lodash';
import LocalStorageKeyConst from '/@/constants/local-storage-key-const.js';

// token的消息头
const TOKEN_HEADER = 't-jwts';

// 创建axios对象
const smartAxios = axios.create({
  baseURL: import.meta.env.VITE_APP_API_URL,
  timeout: 30000, // 30秒超时
});

// 退出系统
function logout() {
  useUserStore().logout();
  const baseUrl = import.meta.env.BASE_URL || '/';
  const normalizedBaseUrl = baseUrl.endsWith('/') ? baseUrl : `${baseUrl}/`;
  location.href = `${normalizedBaseUrl}#/login`;
}

// ================================= 请求拦截器 =================================

smartAxios.interceptors.request.use(
  (config) => {
    // 在发送请求之前消息头加入token token
    const token = localRead(LocalStorageKeyConst.USER_TOKEN);
    if (token) {
      config.headers[TOKEN_HEADER] = token;
    } else {
      delete config.headers[TOKEN_HEADER];
    }
    
    // CSRF Token
    const csrfToken = localRead('CSRF_TOKEN');
    if (csrfToken) {
        config.headers['x-csrf-token'] = csrfToken;
    }
    
    return config;
  },

  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// ================================= 响应拦截器 =================================

// 添加响应拦截器
smartAxios.interceptors.response.use(
  (response) => {
    // 根据content-type ，判断是否为 json 数据
    let contentType = response.headers['content-type'] ? response.headers['content-type'] : response.headers['Content-Type'];
    if (contentType.indexOf('application/json') === -1) {
      return Promise.resolve(response);
    }

    // 如果是json数据
    if (response.data && response.data instanceof Blob) {
      return Promise.reject(response.data);
    }

    // 如果是加密数据
    if (response.data.dataType === DATA_TYPE_ENUM.ENCRYPT.value) {
      response.data.encryptData = response.data.data;
      let decryptStr = decryptData(response.data.data);
      if (decryptStr) {
        response.data.data = JSON.parse(decryptStr);
      }
    }

    const res = response.data;
    if (res.code && res.code !== 200) {
      // 30x 未登录（后端统一封装的业务code）
      if ([301, 302, 307, 308].includes(res.code)) {
        message.destroy();
        message.error(res.msg || '您没有登录，请重新登录');
        setTimeout(logout, 300);
        return Promise.reject(response);
      }

      // 401 未认证或者无权限
      if (res.code === 401) {
        message.destroy();
        message.error(res.msg || '您没有登录，请重新登录');
        setTimeout(logout, 300);
        return Promise.reject(response);
      }

      // 403 无权限操作
      if (res.code === 403) {
        message.destroy();
        message.error(res.msg || '无权限操作');
        return Promise.reject(response);
      }
      
      // 300 业务逻辑错误
      if (res.code === 300) {
        message.destroy();
        message.error(res.msg || '操作失败');
        return Promise.reject(response);
      }
      
      // 303 重复提交
      if (res.code === 303) {
        message.destroy();
        message.error(res.msg || '重复提交');
        return Promise.reject(response);
      }

      // 500 系统错误
      if (res.code === 500) {
         message.destroy();
         message.error(res.msg || '系统错误');
         return Promise.reject(response);
      }

      // 其他错误
      message.destroy();
      message.error(res.msg);
      return Promise.reject(response);
    } else {
      return Promise.resolve(res);
    }
  },
  (error) => {
    // 对响应错误做点什么
    if (error.message.indexOf('timeout') !== -1) {
      message.destroy();
      message.error('网络超时');
    } else if (error.message === 'Network Error') {
      message.destroy();
      message.error('网络连接错误');
    } else if (error.message.indexOf('Request') !== -1) {
      message.destroy();
      message.error('网络发生错误');
    }
    return Promise.reject(error);
  }
);

// ================================= 对外提供请求方法：通用请求，get， post, 下载download等 =================================

/**
 * get请求
 */
export const getRequest = (url, params, config = {}) => {
  return request({ url, method: 'get', params, ...config });
};

/**
 * 通用请求封装
 * @param config
 */
export const request = (config) => {
  return smartAxios.request(config);
};

/**
 * post请求
 * @param url 请求地址
 * @param data 请求数据
 * @param config 额外配置（如timeout）
 */
export const postRequest = (url, data, config = {}) => {
  return request({
    data,
    url,
    method: 'post',
    ...config,
  });
};

// ================================= 加密 =================================

/**
 * 加密请求参数的post请求
 */
export const postEncryptRequest = (url, data) => {
  return request({
    data: { encryptData: encryptData(data) },
    url,
    method: 'post',
  });
};

// ================================= 下载 =================================

export const postDownload = function (url, data) {
  request({
    method: 'post',
    url,
    data,
    responseType: 'blob',
  })
    .then((data) => {
      handleDownloadData(data);
    })
    .catch((error) => {
      handleDownloadError(error);
    });
};

/**
 * 文件下载
 */
export const getDownload = function (url, params) {
  request({
    method: 'get',
    url,
    params,
    responseType: 'blob',
  })
    .then((data) => {
      handleDownloadData(data);
    })
    .catch((error) => {
      handleDownloadError(error);
    });
};

function handleDownloadError(error) {
  if (error instanceof Blob) {
    const fileReader = new FileReader();
    fileReader.readAsText(error);
    fileReader.onload = () => {
      const msg = fileReader.result;
      const jsonMsg = JSON.parse(msg);
      message.destroy();
      message.error(jsonMsg.msg);
    };
  } else {
    message.destroy();
    message.error('网络发生错误', error);
  }
}

function handleDownloadData(response) {
  if (!response) {
    return;
  }

  // 获取返回类型
  let contentType = _.isUndefined(response.headers['content-type']) ? response.headers['Content-Type'] : response.headers['content-type'];

  // 构建下载数据
  let url = window.URL.createObjectURL(new Blob([response.data], { type: contentType }));
  let link = document.createElement('a');
  link.style.display = 'none';
  link.href = url;

  // 从消息头获取文件名
  let str = _.isUndefined(response.headers['content-disposition'])
    ? response.headers['Content-Disposition'].split(';')[1]
    : response.headers['content-disposition'].split(';')[1];

  let filename = _.isUndefined(str.split('fileName=')[1]) ? str.split('filename=')[1] : str.split('fileName=')[1];
  link.setAttribute('download', decodeURIComponent(filename));

  // 触发点击下载
  document.body.appendChild(link);
  link.click();

  // 下载完释放
  document.body.removeChild(link); // 下载完成移除元素
  window.URL.revokeObjectURL(url); // 释放掉blob对象
}
