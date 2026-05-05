import { postRequest } from '/@/lib/axios';

export const aiApi = {
  // 生成课件
  generateCourseware: (param) => {
    return postRequest('/ai/generateCourseware', param);
  },

  // 智能问答
  chat: (param) => {
    return postRequest('/ai/chat', param);
  },
};
