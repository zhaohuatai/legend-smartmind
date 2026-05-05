<!--
  * 登录
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 22:34:00
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <div class="login-container">
    <div class="box-item desc">
      <div class="welcome">
        <p>欢迎登录 SmartAdmin V3</p>
        <p class="sub-welcome">「高质量代码、简洁、安全」的开发平台</p>
      </div>
    </div>
    <div class="box-item login">
      <img class="login-qr" :src="loginQR" />
      <div class="login-title">账号登录</div>
      <a-form ref="formRef" class="login-form" :model="loginForm" :rules="rules">
        <a-form-item name="account">
          <a-input v-model:value.trim="loginForm.account" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item name="emailCode" v-if="emailCodeShowFlag">
          <a-input-group compact>
            <a-input style="width: calc(100% - 110px)" v-model:value="loginForm.emailCode" autocomplete="on" placeholder="请输入邮箱验证码" />
            <a-button @click="sendSmsCode" class="code-btn" type="primary" :disabled="emailCodeButtonDisabled">
              {{ emailCodeTips }}
            </a-button>
          </a-input-group>
        </a-form-item>
        <a-form-item name="password">
          <a-input-password
            v-model:value="loginForm.password"
            autocomplete="on"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item name="captchaCode">
          <a-input class="captcha-input" v-model:value.trim="loginForm.captchaCode" placeholder="请输入验证码" />
          <img class="captcha-img" :src="captchaBase64Image" @click="getCaptcha" />
        </a-form-item>
        <a-form-item>
          <a-checkbox v-model:checked="rememberPwd">记住密码</a-checkbox>
          <span> ( 账号：admin, 密码：123456)</span>
        </a-form-item>
        <a-form-item>
          <div class="btn" @click="onLogin">登录</div>
        </a-form-item>
      </a-form>
      <div class="more">
        <div class="title-box">
          <p class="line"></p>
          <p class="title">其他方式登录</p>
          <p class="line"></p>
        </div>
        <div class="login-type">
          <img :src="wechatIcon" />
          <img :src="aliIcon" />
          <img :src="douyinIcon" />
          <img :src="qqIcon" />
          <img :src="weiboIcon" />
          <img :src="feishuIcon" />
          <img :src="googleIcon" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, onUnmounted, reactive, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { loginApi } from '/@/api/system/login-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { LOGIN_DEVICE_ENUM } from '/@/constants/system/login-device-const';
  import { useUserStore } from '/@/store/modules/system/user';
  import loginQR from '/@/assets/images/login/login-qr.png';
  import wechatIcon from '/@/assets/images/login/wechat-icon.png';
  import aliIcon from '/@/assets/images/login/ali-icon.png';
  import douyinIcon from '/@/assets/images/login/douyin-icon.png';
  import qqIcon from '/@/assets/images/login/qq-icon.png';
  import weiboIcon from '/@/assets/images/login/weibo-icon.png';
  import feishuIcon from '/@/assets/images/login/feishu-icon.png';
  import googleIcon from '/@/assets/images/login/google-icon.png';

  import { buildRoutes, normalizeMenuList } from '/@/router/index';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { encryptData } from '/@/lib/encrypt';
  import { localRemove, localSave } from '/@/utils/local-util.js';
  import LocalStorageKeyConst from '/@/constants/local-storage-key-const.js';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import { dictApi } from '/@/api/support/dict-api.js';

  //--------------------- 登录表单 ---------------------------------

  const loginForm = reactive({
    account: 'admin',
    password: '',
    captchaCode: '',
    emailCode: '',
    captchaUuid: '',
    loginDevice: LOGIN_DEVICE_ENUM.PC.value,
  });
  const rules = {
    account: [{ required: true, message: '用户名不能为空' }],
    password: [{ required: true, message: '密码不能为空' }],
    captchaCode: [{ required: true, message: '验证码不能为空' }],
  };

  const showPassword = ref(false);
  const router = useRouter();
  const formRef = ref();
  const rememberPwd = ref(false);

  onMounted(() => {
    document.onkeyup = (e) => {
      if (e.keyCode === 13) {
        onLogin();
      }
    };
  });

  onUnmounted(() => {
    document.onkeyup = null;
  });

  //登录
  async function onLogin() {
    formRef.value.validate().then(async () => {
      try {
        SmartLoading.show();
        // 密码加密
        let encryptPasswordForm = Object.assign({}, loginForm, {
          password: encryptData(loginForm.password),
        });
        const res = await loginApi.login(encryptPasswordForm);
        stopRefreshCaptchaInterval();
        const token = res.data && res.data.token ? res.data.token : '';
        if (token) {
          localSave(LocalStorageKeyConst.USER_TOKEN, token);
        } else {
          localRemove(LocalStorageKeyConst.USER_TOKEN);
        }
        const csrfToken = res.data && res.data.csrfToken ? res.data.csrfToken : '';
        if (csrfToken) {
          localSave('CSRF_TOKEN', csrfToken);
        } else {
          localRemove('CSRF_TOKEN');
        }
        message.success('登录成功');

        // 获取用户信息
        const userRes = await loginApi.userInfo();
        const userInfo = userRes.data;

        //单独加载菜单
        const menuRes = await loginApi.loadMenu();
        const menuList = normalizeMenuList(menuRes.data);
        userInfo.menuList = menuList;

        //更新用户信息到pinia
        useUserStore().setUserLoginInfo(userInfo);
        
        // 初始化数据字典
        const dictRes = await dictApi.getAllDictData();
        useDictStore().initData(dictRes.data);
        
        //构建系统的路由
        buildRoutes(menuList);

        await router.push('/home');
      } catch (e) {
        if (e.data && e.data.code !== 0) {
          loginForm.captchaCode = '';
          getCaptcha();
        }
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    }).catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
  }

  //--------------------- 验证码 ---------------------------------

  const captchaBase64Image = ref('');
  async function getCaptcha() {
    try {
      let captchaResult = await loginApi.getCaptcha();
      captchaBase64Image.value = captchaResult.data.captchaBase64Image;
      loginForm.captchaUuid = captchaResult.data.captchaUuid;
      beginRefreshCaptchaInterval(captchaResult.data.expireSeconds);
    } catch (e) {
      console.log(e);
    }
  }

  let refreshCaptchaInterval = null;
  function beginRefreshCaptchaInterval(expireSeconds) {
    if (refreshCaptchaInterval === null) {
      refreshCaptchaInterval = setInterval(getCaptcha, (expireSeconds - 5) * 1000);
    }
  }

  function stopRefreshCaptchaInterval() {
    if (refreshCaptchaInterval != null) {
      clearInterval(refreshCaptchaInterval);
      refreshCaptchaInterval = null;
    }
  }

  onMounted(() => {
    getCaptcha();
    getTwoFactorLoginFlag();
  });

  //--------------------- 邮箱验证码 ---------------------------------

  const emailCodeShowFlag = ref(false);
  let emailCodeTips = ref('获取邮箱验证码');
  let emailCodeButtonDisabled = ref(false);
  // 定时器
  let countDownTimer = null;
  // 开始倒计时
  function runCountDown() {
    emailCodeButtonDisabled.value = true;
    let countDown = 60;
    emailCodeTips.value = `${countDown}秒后重新获取`;
    countDownTimer = setInterval(() => {
      if (countDown > 1) {
        countDown--;
        emailCodeTips.value = `${countDown}秒后重新获取`;
      } else {
        clearInterval(countDownTimer);
        emailCodeButtonDisabled.value = false;
        emailCodeTips.value = '获取验证码';
      }
    }, 1000);
  }

  // 获取双因子登录标识
  async function getTwoFactorLoginFlag() {
    try {
      let result = await loginApi.getTwoFactorLoginFlag();
      emailCodeShowFlag.value = result.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 发送邮箱验证码
  async function sendSmsCode() {
    try {
      SmartLoading.show();
      let result = await loginApi.sendLoginEmailCode(loginForm.account);
      message.success('验证码发送成功!请登录邮箱查看验证码~');
      runCountDown();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
<style lang="less" scoped>
  @import './login.less';
</style>
