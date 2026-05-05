/*
 *  登录
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:59:58
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const loginApi = {
  /**
   * 登录 @author 卓大
   */
  login: (param) => {
    return postRequest('/api/auth/login', param);
  },

  /**
   * 退出登录 @author 卓大
   */
  logout: () => {
    return getRequest('/api/auth/logout');
  },

  /**
   * 获取验证码 @author 卓大
   */
  getCaptcha: () => {
    return getRequest('/api/open/captcha/applay');
  },

  /**
   * 获取登录信息 @author 卓大
   */
  getLoginInfo: () => {
    return getRequest('/login/getLoginInfo');
  },

  /**
   * 获取邮箱登录验证码 @author 卓大
   */
  sendLoginEmailCode: (account) => {
    return getRequest(`/login/sendEmailCode/${account}`);
  },

  /**
   * 获取双因子登录标识 @author 卓大
   */
  getTwoFactorLoginFlag: () => {
    return getRequest('/login/getTwoFactorLoginFlag');
  },
  
  /**
   * 获取用户信息
   */
  userInfo: () => {
    return postRequest('/api/service/webapi/userInfo');
  },

  /**
   * 加载菜单
   */
  loadMenu: () => {
    return postRequest('/api/service/webapi/loadMenu');
  },
};

