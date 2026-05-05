// vite.config.js
import { resolve } from "path";
import vue from "file:///E:/worksapce/sts5.0/legend-framework/legend-framework-ui/src/smart-admin-web-javascript/node_modules/@vitejs/plugin-vue/dist/index.mjs";

// src/theme/custom-variables.js
import { theme } from "file:///E:/worksapce/sts5.0/legend-framework/legend-framework-ui/src/smart-admin-web-javascript/node_modules/ant-design-vue/lib/index.js";
import convertLegacyToken from "file:///E:/worksapce/sts5.0/legend-framework/legend-framework-ui/src/smart-admin-web-javascript/node_modules/ant-design-vue/lib/theme/convertLegacyToken.js";
var { defaultAlgorithm, defaultSeed } = theme;
var mapToken = defaultAlgorithm(defaultSeed);
var token = convertLegacyToken.default(mapToken);
var custom_variables_default = {
  "@primary-color": token["primary-color"],
  // 全局主色
  "@base-bg-color": "#fff",
  "@hover-bg-color": "rgba(0, 0, 0, 0.025)",
  "@hover-bg-color-night": "rgba(255, 255, 255, 0.025)",
  "@header-light-bg-hover-color": "#f6f6f6",
  "@header-height": "80px",
  "@header-user-height": "40px",
  "@page-tag-height": "40px",
  "@theme-list": ["light", "dark", "night"]
};

// vite.config.js
var __vite_injected_original_dirname = "E:\\worksapce\\sts5.0\\legend-framework\\legend-framework-ui\\src\\smart-admin-web-javascript";
var pathResolve = (dir) => {
  return resolve(__vite_injected_original_dirname, ".", dir);
};
var vite_config_default = {
  base: process.env.NODE_ENV === "production" ? "/" : "/",
  root: process.cwd(),
  resolve: {
    alias: [
      // 国际化替换
      {
        find: "vue-i18n",
        replacement: "vue-i18n/dist/vue-i18n.cjs.js"
      },
      // 绝对路径重命名：/@/xxxx => src/xxxx
      {
        find: /\/@\//,
        replacement: pathResolve("src") + "/"
      },
      {
        find: /^~/,
        replacement: ""
      }
    ]
  },
  server: {
    host: "0.0.0.0",
    port: 8081,
    proxy: {
      "/": {
        target: "http://127.0.0.1:8080",
        changeOrigin: true,
        bypass(req) {
          const url = req.url || "";
          if (url.startsWith("/@") || url.startsWith("/src/") || url.startsWith("/node_modules/") || url.startsWith("/assets/") || url === "/favicon.ico") {
            return url;
          }
          return null;
        }
      }
    }
  },
  plugins: [vue()],
  optimizeDeps: {
    include: ["ant-design-vue/es/locale/zh_CN", "dayjs/locale/zh-cn", "ant-design-vue/es/locale/en_US"],
    exclude: ["vue-demi"]
  },
  build: {
    // 清除console和debugger
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
    rollupOptions: {
      output: {
        //配置这个是让不同类型文件放在不同文件夹，不会显得太乱
        chunkFileNames: "js/[name]-[hash].js",
        entryFileNames: "js/[name]-[hash].js",
        assetFileNames: "[ext]/[name]-[hash].[ext]",
        manualChunks(id) {
          if (id.includes("node_modules")) {
            return id.toString().split("node_modules/")[1].split("/")[0].toString();
          }
        }
      }
    },
    target: "esnext",
    outDir: "dist",
    // 指定输出路径
    assetsDir: "assets",
    // 指定生成静态文件目录
    assetsInlineLimit: "4096",
    // 小于此阈值的导入或引用资源将内联为 base64 编码
    chunkSizeWarningLimit: 500,
    // chunk 大小警告的限制
    minify: "terser",
    // 混淆器，terser构建后文件体积更小
    emptyOutDir: true
    //打包前先清空原有打包文件
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: custom_variables_default,
        javascriptEnabled: true
      }
    }
  },
  define: {
    __INTLIFY_PROD_DEVTOOLS__: false,
    "process.env": process.env
  }
};
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiLCAic3JjL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJFOlxcXFx3b3Jrc2FwY2VcXFxcc3RzNS4wXFxcXGxlZ2VuZC1mcmFtZXdvcmtcXFxcbGVnZW5kLWZyYW1ld29yay11aVxcXFxzcmNcXFxcc21hcnQtYWRtaW4td2ViLWphdmFzY3JpcHRcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkU6XFxcXHdvcmtzYXBjZVxcXFxzdHM1LjBcXFxcbGVnZW5kLWZyYW1ld29ya1xcXFxsZWdlbmQtZnJhbWV3b3JrLXVpXFxcXHNyY1xcXFxzbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdFxcXFx2aXRlLmNvbmZpZy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRTovd29ya3NhcGNlL3N0czUuMC9sZWdlbmQtZnJhbWV3b3JrL2xlZ2VuZC1mcmFtZXdvcmstdWkvc3JjL3NtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0L3ZpdGUuY29uZmlnLmpzXCI7LypcclxuICogdml0ZVx1OTE0RFx1N0Y2RVxyXG4gKlxyXG4gKiBAQXV0aG9yOiAgICAxMDI0XHU1MjFCXHU2NUIwXHU1QjlFXHU5QThDXHU1QkE0LVx1NEUzQlx1NEVGQlx1RkYxQVx1NTM1M1x1NTkyN1xyXG4gKiBARGF0ZTogICAgICAyMDIyLTA1LTAyIDIzOjQ0OjU2XHJcbiAqIEBXZWNoYXQ6ICAgIHpodWRhMTAyNFxyXG4gKiBARW1haWw6ICAgICBsYWIxMDI0QDE2My5jb21cclxuICogQENvcHlyaWdodCAgMTAyNFx1NTIxQlx1NjVCMFx1NUI5RVx1OUE4Q1x1NUJBNCBcdUZGMDggaHR0cHM6Ly8xMDI0bGFiLm5ldCBcdUZGMDlcdUZGMENTaW5jZSAyMDEyXHJcbiAqL1xyXG5pbXBvcnQgeyByZXNvbHZlIH0gZnJvbSAncGF0aCc7XHJcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJztcclxuaW1wb3J0IGN1c3RvbVZhcmlhYmxlcyBmcm9tICcvQC90aGVtZS9jdXN0b20tdmFyaWFibGVzLmpzJztcclxuXHJcbmNvbnN0IHBhdGhSZXNvbHZlID0gKGRpcikgPT4ge1xyXG4gIHJldHVybiByZXNvbHZlKF9fZGlybmFtZSwgJy4nLCBkaXIpO1xyXG59O1xyXG5leHBvcnQgZGVmYXVsdCB7XHJcbiAgYmFzZTogcHJvY2Vzcy5lbnYuTk9ERV9FTlYgPT09ICdwcm9kdWN0aW9uJyA/ICcvJyA6ICcvJyxcclxuICByb290OiBwcm9jZXNzLmN3ZCgpLFxyXG4gIHJlc29sdmU6IHtcclxuICAgIGFsaWFzOiBbXHJcbiAgICAgIC8vIFx1NTZGRFx1OTY0NVx1NTMxNlx1NjZGRlx1NjM2MlxyXG4gICAgICB7XHJcbiAgICAgICAgZmluZDogJ3Z1ZS1pMThuJyxcclxuICAgICAgICByZXBsYWNlbWVudDogJ3Z1ZS1pMThuL2Rpc3QvdnVlLWkxOG4uY2pzLmpzJyxcclxuICAgICAgfSxcclxuICAgICAgLy8gXHU3RUREXHU1QkY5XHU4REVGXHU1Rjg0XHU5MUNEXHU1NDdEXHU1NDBEXHVGRjFBL0AveHh4eCA9PiBzcmMveHh4eFxyXG4gICAgICB7XHJcbiAgICAgICAgZmluZDogL1xcL0BcXC8vLFxyXG4gICAgICAgIHJlcGxhY2VtZW50OiBwYXRoUmVzb2x2ZSgnc3JjJykgKyAnLycsXHJcbiAgICAgIH0sXHJcbiAgICAgIHtcclxuICAgICAgICBmaW5kOiAvXn4vLFxyXG4gICAgICAgIHJlcGxhY2VtZW50OiAnJyxcclxuICAgICAgfSxcclxuICAgIF0sXHJcbiAgfSxcclxuICBzZXJ2ZXI6IHtcclxuICAgIGhvc3Q6ICcwLjAuMC4wJyxcclxuICAgIHBvcnQ6IDgwODEsXHJcbiAgICBwcm94eToge1xyXG4gICAgICAnLyc6IHtcclxuICAgICAgICB0YXJnZXQ6ICdodHRwOi8vMTI3LjAuMC4xOjgwODAnLFxyXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZSxcclxuICAgICAgICBieXBhc3MocmVxKSB7XHJcbiAgICAgICAgICBjb25zdCB1cmwgPSByZXEudXJsIHx8ICcnO1xyXG4gICAgICAgICAgaWYgKHVybC5zdGFydHNXaXRoKCcvQCcpIHx8IHVybC5zdGFydHNXaXRoKCcvc3JjLycpIHx8IHVybC5zdGFydHNXaXRoKCcvbm9kZV9tb2R1bGVzLycpIHx8IHVybC5zdGFydHNXaXRoKCcvYXNzZXRzLycpIHx8IHVybCA9PT0gJy9mYXZpY29uLmljbycpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHVybDtcclxuICAgICAgICAgIH1cclxuICAgICAgICAgIHJldHVybiBudWxsO1xyXG4gICAgICAgIH0sXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gIH0sXHJcbiAgcGx1Z2luczogW3Z1ZSgpXSxcclxuICBvcHRpbWl6ZURlcHM6IHtcclxuICAgIGluY2x1ZGU6IFsnYW50LWRlc2lnbi12dWUvZXMvbG9jYWxlL3poX0NOJywgJ2RheWpzL2xvY2FsZS96aC1jbicsICdhbnQtZGVzaWduLXZ1ZS9lcy9sb2NhbGUvZW5fVVMnXSxcclxuICAgIGV4Y2x1ZGU6IFsndnVlLWRlbWknXSxcclxuICB9LFxyXG4gIGJ1aWxkOiB7XHJcbiAgICAvLyBcdTZFMDVcdTk2NjRjb25zb2xlXHU1NDhDZGVidWdnZXJcclxuICAgIHRlcnNlck9wdGlvbnM6IHtcclxuICAgICAgY29tcHJlc3M6IHtcclxuICAgICAgICBkcm9wX2NvbnNvbGU6IHRydWUsXHJcbiAgICAgICAgZHJvcF9kZWJ1Z2dlcjogdHJ1ZSxcclxuICAgICAgfSxcclxuICAgIH0sXHJcbiAgICByb2xsdXBPcHRpb25zOiB7XHJcbiAgICAgIG91dHB1dDoge1xyXG4gICAgICAgIC8vXHU5MTREXHU3RjZFXHU4RkQ5XHU0RTJBXHU2NjJGXHU4QkE5XHU0RTBEXHU1NDBDXHU3QzdCXHU1NzhCXHU2NTg3XHU0RUY2XHU2NTNFXHU1NzI4XHU0RTBEXHU1NDBDXHU2NTg3XHU0RUY2XHU1OTM5XHVGRjBDXHU0RTBEXHU0RjFBXHU2NjNFXHU1Rjk3XHU1OTJBXHU0RTcxXHJcbiAgICAgICAgY2h1bmtGaWxlTmFtZXM6ICdqcy9bbmFtZV0tW2hhc2hdLmpzJyxcclxuICAgICAgICBlbnRyeUZpbGVOYW1lczogJ2pzL1tuYW1lXS1baGFzaF0uanMnLFxyXG4gICAgICAgIGFzc2V0RmlsZU5hbWVzOiAnW2V4dF0vW25hbWVdLVtoYXNoXS5bZXh0XScsXHJcbiAgICAgICAgbWFudWFsQ2h1bmtzKGlkKSB7XHJcbiAgICAgICAgICAvL1x1OTc1OVx1NjAwMVx1OEQ0NFx1NkU5MFx1NTIwNlx1NjJDNlx1NjI1M1x1NTMwNVxyXG4gICAgICAgICAgaWYgKGlkLmluY2x1ZGVzKCdub2RlX21vZHVsZXMnKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gaWQudG9TdHJpbmcoKS5zcGxpdCgnbm9kZV9tb2R1bGVzLycpWzFdLnNwbGl0KCcvJylbMF0udG9TdHJpbmcoKTtcclxuICAgICAgICAgIH1cclxuICAgICAgICB9LFxyXG4gICAgICB9LFxyXG4gICAgfSxcclxuICAgIHRhcmdldDogJ2VzbmV4dCcsXHJcbiAgICBvdXREaXI6ICdkaXN0JywgLy8gXHU2MzA3XHU1QjlBXHU4RjkzXHU1MUZBXHU4REVGXHU1Rjg0XHJcbiAgICBhc3NldHNEaXI6ICdhc3NldHMnLCAvLyBcdTYzMDdcdTVCOUFcdTc1MUZcdTYyMTBcdTk3NTlcdTYwMDFcdTY1ODdcdTRFRjZcdTc2RUVcdTVGNTVcclxuICAgIGFzc2V0c0lubGluZUxpbWl0OiAnNDA5NicsIC8vIFx1NUMwRlx1NEU4RVx1NkI2NFx1OTYwOFx1NTAzQ1x1NzY4NFx1NUJGQ1x1NTE2NVx1NjIxNlx1NUYxNVx1NzUyOFx1OEQ0NFx1NkU5MFx1NUMwNlx1NTE4NVx1ODA1NFx1NEUzQSBiYXNlNjQgXHU3RjE2XHU3ODAxXHJcbiAgICBjaHVua1NpemVXYXJuaW5nTGltaXQ6IDUwMCwgLy8gY2h1bmsgXHU1OTI3XHU1QzBGXHU4QjY2XHU1NDRBXHU3Njg0XHU5NjUwXHU1MjM2XHJcbiAgICBtaW5pZnk6ICd0ZXJzZXInLCAvLyBcdTZERjdcdTZEQzZcdTU2NjhcdUZGMEN0ZXJzZXJcdTY3ODRcdTVFRkFcdTU0MEVcdTY1ODdcdTRFRjZcdTRGNTNcdTc5RUZcdTY2RjRcdTVDMEZcclxuICAgIGVtcHR5T3V0RGlyOiB0cnVlLCAvL1x1NjI1M1x1NTMwNVx1NTI0RFx1NTE0OFx1NkUwNVx1N0E3QVx1NTM5Rlx1NjcwOVx1NjI1M1x1NTMwNVx1NjU4N1x1NEVGNlxyXG4gIH0sXHJcbiAgY3NzOiB7XHJcbiAgICBwcmVwcm9jZXNzb3JPcHRpb25zOiB7XHJcbiAgICAgIGxlc3M6IHtcclxuICAgICAgICBtb2RpZnlWYXJzOiBjdXN0b21WYXJpYWJsZXMsXHJcbiAgICAgICAgamF2YXNjcmlwdEVuYWJsZWQ6IHRydWUsXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gIH0sXHJcbiAgZGVmaW5lOiB7XHJcbiAgICBfX0lOVExJRllfUFJPRF9ERVZUT09MU19fOiBmYWxzZSxcclxuICAgICdwcm9jZXNzLmVudic6IHByb2Nlc3MuZW52LFxyXG4gIH0sXHJcbn07XHJcbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiRTpcXFxcd29ya3NhcGNlXFxcXHN0czUuMFxcXFxsZWdlbmQtZnJhbWV3b3JrXFxcXGxlZ2VuZC1mcmFtZXdvcmstdWlcXFxcc3JjXFxcXHNtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0XFxcXHNyY1xcXFx0aGVtZVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRTpcXFxcd29ya3NhcGNlXFxcXHN0czUuMFxcXFxsZWdlbmQtZnJhbWV3b3JrXFxcXGxlZ2VuZC1mcmFtZXdvcmstdWlcXFxcc3JjXFxcXHNtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0XFxcXHNyY1xcXFx0aGVtZVxcXFxjdXN0b20tdmFyaWFibGVzLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9FOi93b3Jrc2FwY2Uvc3RzNS4wL2xlZ2VuZC1mcmFtZXdvcmsvbGVnZW5kLWZyYW1ld29yay11aS9zcmMvc21hcnQtYWRtaW4td2ViLWphdmFzY3JpcHQvc3JjL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanNcIjtpbXBvcnQgeyB0aGVtZSB9IGZyb20gJ2FudC1kZXNpZ24tdnVlL2xpYic7XHJcbmltcG9ydCBjb252ZXJ0TGVnYWN5VG9rZW4gZnJvbSAnYW50LWRlc2lnbi12dWUvbGliL3RoZW1lL2NvbnZlcnRMZWdhY3lUb2tlbic7XHJcblxyXG5jb25zdCB7IGRlZmF1bHRBbGdvcml0aG0sIGRlZmF1bHRTZWVkIH0gPSB0aGVtZTtcclxuXHJcbmNvbnN0IG1hcFRva2VuID0gZGVmYXVsdEFsZ29yaXRobShkZWZhdWx0U2VlZCk7XHJcbmNvbnN0IHRva2VuID0gY29udmVydExlZ2FjeVRva2VuLmRlZmF1bHQobWFwVG9rZW4pO1xyXG5cclxuZXhwb3J0IGRlZmF1bHQge1xyXG4gICdAcHJpbWFyeS1jb2xvcic6IHRva2VuWydwcmltYXJ5LWNvbG9yJ10sIC8vIFx1NTE2OFx1NUM0MFx1NEUzQlx1ODI3MlxyXG4gICdAYmFzZS1iZy1jb2xvcic6ICcjZmZmJyxcclxuICAnQGhvdmVyLWJnLWNvbG9yJzogJ3JnYmEoMCwgMCwgMCwgMC4wMjUpJyxcclxuICAnQGhvdmVyLWJnLWNvbG9yLW5pZ2h0JzogJ3JnYmEoMjU1LCAyNTUsIDI1NSwgMC4wMjUpJyxcclxuICAnQGhlYWRlci1saWdodC1iZy1ob3Zlci1jb2xvcic6ICcjZjZmNmY2JyxcclxuICAnQGhlYWRlci1oZWlnaHQnOiAnODBweCcsXHJcbiAgJ0BoZWFkZXItdXNlci1oZWlnaHQnOiAnNDBweCcsXHJcbiAgJ0BwYWdlLXRhZy1oZWlnaHQnOiAnNDBweCcsXHJcbiAgJ0B0aGVtZS1saXN0JzogWydsaWdodCcsICdkYXJrJywgJ25pZ2h0J10sXHJcbn07XHJcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFTQSxTQUFTLGVBQWU7QUFDeEIsT0FBTyxTQUFTOzs7QUNWaWUsU0FBUyxhQUFhO0FBQ3ZnQixPQUFPLHdCQUF3QjtBQUUvQixJQUFNLEVBQUUsa0JBQWtCLFlBQVksSUFBSTtBQUUxQyxJQUFNLFdBQVcsaUJBQWlCLFdBQVc7QUFDN0MsSUFBTSxRQUFRLG1CQUFtQixRQUFRLFFBQVE7QUFFakQsSUFBTywyQkFBUTtBQUFBLEVBQ2Isa0JBQWtCLE1BQU0sZUFBZTtBQUFBO0FBQUEsRUFDdkMsa0JBQWtCO0FBQUEsRUFDbEIsbUJBQW1CO0FBQUEsRUFDbkIseUJBQXlCO0FBQUEsRUFDekIsZ0NBQWdDO0FBQUEsRUFDaEMsa0JBQWtCO0FBQUEsRUFDbEIsdUJBQXVCO0FBQUEsRUFDdkIsb0JBQW9CO0FBQUEsRUFDcEIsZUFBZSxDQUFDLFNBQVMsUUFBUSxPQUFPO0FBQzFDOzs7QURsQkEsSUFBTSxtQ0FBbUM7QUFhekMsSUFBTSxjQUFjLENBQUMsUUFBUTtBQUMzQixTQUFPLFFBQVEsa0NBQVcsS0FBSyxHQUFHO0FBQ3BDO0FBQ0EsSUFBTyxzQkFBUTtBQUFBLEVBQ2IsTUFBTSxRQUFRLElBQUksYUFBYSxlQUFlLE1BQU07QUFBQSxFQUNwRCxNQUFNLFFBQVEsSUFBSTtBQUFBLEVBQ2xCLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQTtBQUFBLE1BRUw7QUFBQSxRQUNFLE1BQU07QUFBQSxRQUNOLGFBQWE7QUFBQSxNQUNmO0FBQUE7QUFBQSxNQUVBO0FBQUEsUUFDRSxNQUFNO0FBQUEsUUFDTixhQUFhLFlBQVksS0FBSyxJQUFJO0FBQUEsTUFDcEM7QUFBQSxNQUNBO0FBQUEsUUFDRSxNQUFNO0FBQUEsUUFDTixhQUFhO0FBQUEsTUFDZjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQUEsRUFDQSxRQUFRO0FBQUEsSUFDTixNQUFNO0FBQUEsSUFDTixNQUFNO0FBQUEsSUFDTixPQUFPO0FBQUEsTUFDTCxLQUFLO0FBQUEsUUFDSCxRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsUUFDZCxPQUFPLEtBQUs7QUFDVixnQkFBTSxNQUFNLElBQUksT0FBTztBQUN2QixjQUFJLElBQUksV0FBVyxJQUFJLEtBQUssSUFBSSxXQUFXLE9BQU8sS0FBSyxJQUFJLFdBQVcsZ0JBQWdCLEtBQUssSUFBSSxXQUFXLFVBQVUsS0FBSyxRQUFRLGdCQUFnQjtBQUMvSSxtQkFBTztBQUFBLFVBQ1Q7QUFDQSxpQkFBTztBQUFBLFFBQ1Q7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFNBQVMsQ0FBQyxJQUFJLENBQUM7QUFBQSxFQUNmLGNBQWM7QUFBQSxJQUNaLFNBQVMsQ0FBQyxrQ0FBa0Msc0JBQXNCLGdDQUFnQztBQUFBLElBQ2xHLFNBQVMsQ0FBQyxVQUFVO0FBQUEsRUFDdEI7QUFBQSxFQUNBLE9BQU87QUFBQTtBQUFBLElBRUwsZUFBZTtBQUFBLE1BQ2IsVUFBVTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsZUFBZTtBQUFBLE1BQ2pCO0FBQUEsSUFDRjtBQUFBLElBQ0EsZUFBZTtBQUFBLE1BQ2IsUUFBUTtBQUFBO0FBQUEsUUFFTixnQkFBZ0I7QUFBQSxRQUNoQixnQkFBZ0I7QUFBQSxRQUNoQixnQkFBZ0I7QUFBQSxRQUNoQixhQUFhLElBQUk7QUFFZixjQUFJLEdBQUcsU0FBUyxjQUFjLEdBQUc7QUFDL0IsbUJBQU8sR0FBRyxTQUFTLEVBQUUsTUFBTSxlQUFlLEVBQUUsQ0FBQyxFQUFFLE1BQU0sR0FBRyxFQUFFLENBQUMsRUFBRSxTQUFTO0FBQUEsVUFDeEU7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFBQSxJQUNBLFFBQVE7QUFBQSxJQUNSLFFBQVE7QUFBQTtBQUFBLElBQ1IsV0FBVztBQUFBO0FBQUEsSUFDWCxtQkFBbUI7QUFBQTtBQUFBLElBQ25CLHVCQUF1QjtBQUFBO0FBQUEsSUFDdkIsUUFBUTtBQUFBO0FBQUEsSUFDUixhQUFhO0FBQUE7QUFBQSxFQUNmO0FBQUEsRUFDQSxLQUFLO0FBQUEsSUFDSCxxQkFBcUI7QUFBQSxNQUNuQixNQUFNO0FBQUEsUUFDSixZQUFZO0FBQUEsUUFDWixtQkFBbUI7QUFBQSxNQUNyQjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQUEsRUFDQSxRQUFRO0FBQUEsSUFDTiwyQkFBMkI7QUFBQSxJQUMzQixlQUFlLFFBQVE7QUFBQSxFQUN6QjtBQUNGOyIsCiAgIm5hbWVzIjogW10KfQo=
