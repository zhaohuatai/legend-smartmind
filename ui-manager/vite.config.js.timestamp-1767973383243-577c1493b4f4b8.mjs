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
      // 代理路径
      "/api": {
        target: "http://127.0.0.1:8080",
        // 目标服务器地址
        changeOrigin: true
        // 是否修改请求头中的 Origin 字段
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
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiLCAic3JjL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJFOlxcXFx3b3Jrc2FwY2VcXFxcc3RzNS4wXFxcXGxlZ2VuZC1mcmFtZXdvcmtcXFxcbGVnZW5kLWZyYW1ld29yay11aVxcXFxzcmNcXFxcc21hcnQtYWRtaW4td2ViLWphdmFzY3JpcHRcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkU6XFxcXHdvcmtzYXBjZVxcXFxzdHM1LjBcXFxcbGVnZW5kLWZyYW1ld29ya1xcXFxsZWdlbmQtZnJhbWV3b3JrLXVpXFxcXHNyY1xcXFxzbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdFxcXFx2aXRlLmNvbmZpZy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRTovd29ya3NhcGNlL3N0czUuMC9sZWdlbmQtZnJhbWV3b3JrL2xlZ2VuZC1mcmFtZXdvcmstdWkvc3JjL3NtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0L3ZpdGUuY29uZmlnLmpzXCI7LypcclxuICogdml0ZVx1OTE0RFx1N0Y2RVxyXG4gKlxyXG4gKiBAQXV0aG9yOiAgICAxMDI0XHU1MjFCXHU2NUIwXHU1QjlFXHU5QThDXHU1QkE0LVx1NEUzQlx1NEVGQlx1RkYxQVx1NTM1M1x1NTkyN1xyXG4gKiBARGF0ZTogICAgICAyMDIyLTA1LTAyIDIzOjQ0OjU2XHJcbiAqIEBXZWNoYXQ6ICAgIHpodWRhMTAyNFxyXG4gKiBARW1haWw6ICAgICBsYWIxMDI0QDE2My5jb21cclxuICogQENvcHlyaWdodCAgMTAyNFx1NTIxQlx1NjVCMFx1NUI5RVx1OUE4Q1x1NUJBNCBcdUZGMDggaHR0cHM6Ly8xMDI0bGFiLm5ldCBcdUZGMDlcdUZGMENTaW5jZSAyMDEyXHJcbiAqL1xyXG5pbXBvcnQgeyByZXNvbHZlIH0gZnJvbSAncGF0aCc7XHJcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJztcclxuaW1wb3J0IGN1c3RvbVZhcmlhYmxlcyBmcm9tICcvQC90aGVtZS9jdXN0b20tdmFyaWFibGVzLmpzJztcclxuXHJcbmNvbnN0IHBhdGhSZXNvbHZlID0gKGRpcikgPT4ge1xyXG4gIHJldHVybiByZXNvbHZlKF9fZGlybmFtZSwgJy4nLCBkaXIpO1xyXG59O1xyXG5leHBvcnQgZGVmYXVsdCB7XHJcbiAgYmFzZTogcHJvY2Vzcy5lbnYuTk9ERV9FTlYgPT09ICdwcm9kdWN0aW9uJyA/ICcvJyA6ICcvJyxcclxuICByb290OiBwcm9jZXNzLmN3ZCgpLFxyXG4gIHJlc29sdmU6IHtcclxuICAgIGFsaWFzOiBbXHJcbiAgICAgIC8vIFx1NTZGRFx1OTY0NVx1NTMxNlx1NjZGRlx1NjM2MlxyXG4gICAgICB7XHJcbiAgICAgICAgZmluZDogJ3Z1ZS1pMThuJyxcclxuICAgICAgICByZXBsYWNlbWVudDogJ3Z1ZS1pMThuL2Rpc3QvdnVlLWkxOG4uY2pzLmpzJyxcclxuICAgICAgfSxcclxuICAgICAgLy8gXHU3RUREXHU1QkY5XHU4REVGXHU1Rjg0XHU5MUNEXHU1NDdEXHU1NDBEXHVGRjFBL0AveHh4eCA9PiBzcmMveHh4eFxyXG4gICAgICB7XHJcbiAgICAgICAgZmluZDogL1xcL0BcXC8vLFxyXG4gICAgICAgIHJlcGxhY2VtZW50OiBwYXRoUmVzb2x2ZSgnc3JjJykgKyAnLycsXHJcbiAgICAgIH0sXHJcbiAgICAgIHtcclxuICAgICAgICBmaW5kOiAvXn4vLFxyXG4gICAgICAgIHJlcGxhY2VtZW50OiAnJyxcclxuICAgICAgfSxcclxuICAgIF0sXHJcbiAgfSxcclxuICBzZXJ2ZXI6IHtcclxuICAgIGhvc3Q6ICcwLjAuMC4wJyxcclxuICAgIHBvcnQ6IDgwODEsXHJcbiAgICBwcm94eToge1xyXG4gICAgICAvLyBcdTRFRTNcdTc0MDZcdThERUZcdTVGODRcclxuICAgICAgJy9hcGknOiB7XHJcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovLzEyNy4wLjAuMTo4MDgwJywgLy8gXHU3NkVFXHU2ODA3XHU2NzBEXHU1MkExXHU1NjY4XHU1NzMwXHU1NzQwXHJcbiAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLCAvLyBcdTY2MkZcdTU0MjZcdTRGRUVcdTY1MzlcdThCRjdcdTZDNDJcdTU5MzRcdTRFMkRcdTc2ODQgT3JpZ2luIFx1NUI1N1x1NkJCNVxyXG4gICAgICB9LFxyXG4gICAgfSxcclxuICB9LFxyXG4gIHBsdWdpbnM6IFt2dWUoKV0sXHJcbiAgb3B0aW1pemVEZXBzOiB7XHJcbiAgICBpbmNsdWRlOiBbJ2FudC1kZXNpZ24tdnVlL2VzL2xvY2FsZS96aF9DTicsICdkYXlqcy9sb2NhbGUvemgtY24nLCAnYW50LWRlc2lnbi12dWUvZXMvbG9jYWxlL2VuX1VTJ10sXHJcbiAgICBleGNsdWRlOiBbJ3Z1ZS1kZW1pJ10sXHJcbiAgfSxcclxuICBidWlsZDoge1xyXG4gICAgLy8gXHU2RTA1XHU5NjY0Y29uc29sZVx1NTQ4Q2RlYnVnZ2VyXHJcbiAgICB0ZXJzZXJPcHRpb25zOiB7XHJcbiAgICAgIGNvbXByZXNzOiB7XHJcbiAgICAgICAgZHJvcF9jb25zb2xlOiB0cnVlLFxyXG4gICAgICAgIGRyb3BfZGVidWdnZXI6IHRydWUsXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gICAgcm9sbHVwT3B0aW9uczoge1xyXG4gICAgICBvdXRwdXQ6IHtcclxuICAgICAgICAvL1x1OTE0RFx1N0Y2RVx1OEZEOVx1NEUyQVx1NjYyRlx1OEJBOVx1NEUwRFx1NTQwQ1x1N0M3Qlx1NTc4Qlx1NjU4N1x1NEVGNlx1NjUzRVx1NTcyOFx1NEUwRFx1NTQwQ1x1NjU4N1x1NEVGNlx1NTkzOVx1RkYwQ1x1NEUwRFx1NEYxQVx1NjYzRVx1NUY5N1x1NTkyQVx1NEU3MVxyXG4gICAgICAgIGNodW5rRmlsZU5hbWVzOiAnanMvW25hbWVdLVtoYXNoXS5qcycsXHJcbiAgICAgICAgZW50cnlGaWxlTmFtZXM6ICdqcy9bbmFtZV0tW2hhc2hdLmpzJyxcclxuICAgICAgICBhc3NldEZpbGVOYW1lczogJ1tleHRdL1tuYW1lXS1baGFzaF0uW2V4dF0nLFxyXG4gICAgICAgIG1hbnVhbENodW5rcyhpZCkge1xyXG4gICAgICAgICAgLy9cdTk3NTlcdTYwMDFcdThENDRcdTZFOTBcdTUyMDZcdTYyQzZcdTYyNTNcdTUzMDVcclxuICAgICAgICAgIGlmIChpZC5pbmNsdWRlcygnbm9kZV9tb2R1bGVzJykpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGlkLnRvU3RyaW5nKCkuc3BsaXQoJ25vZGVfbW9kdWxlcy8nKVsxXS5zcGxpdCgnLycpWzBdLnRvU3RyaW5nKCk7XHJcbiAgICAgICAgICB9XHJcbiAgICAgICAgfSxcclxuICAgICAgfSxcclxuICAgIH0sXHJcbiAgICB0YXJnZXQ6ICdlc25leHQnLFxyXG4gICAgb3V0RGlyOiAnZGlzdCcsIC8vIFx1NjMwN1x1NUI5QVx1OEY5M1x1NTFGQVx1OERFRlx1NUY4NFxyXG4gICAgYXNzZXRzRGlyOiAnYXNzZXRzJywgLy8gXHU2MzA3XHU1QjlBXHU3NTFGXHU2MjEwXHU5NzU5XHU2MDAxXHU2NTg3XHU0RUY2XHU3NkVFXHU1RjU1XHJcbiAgICBhc3NldHNJbmxpbmVMaW1pdDogJzQwOTYnLCAvLyBcdTVDMEZcdTRFOEVcdTZCNjRcdTk2MDhcdTUwM0NcdTc2ODRcdTVCRkNcdTUxNjVcdTYyMTZcdTVGMTVcdTc1MjhcdThENDRcdTZFOTBcdTVDMDZcdTUxODVcdTgwNTRcdTRFM0EgYmFzZTY0IFx1N0YxNlx1NzgwMVxyXG4gICAgY2h1bmtTaXplV2FybmluZ0xpbWl0OiA1MDAsIC8vIGNodW5rIFx1NTkyN1x1NUMwRlx1OEI2Nlx1NTQ0QVx1NzY4NFx1OTY1MFx1NTIzNlxyXG4gICAgbWluaWZ5OiAndGVyc2VyJywgLy8gXHU2REY3XHU2REM2XHU1NjY4XHVGRjBDdGVyc2VyXHU2Nzg0XHU1RUZBXHU1NDBFXHU2NTg3XHU0RUY2XHU0RjUzXHU3OUVGXHU2NkY0XHU1QzBGXHJcbiAgICBlbXB0eU91dERpcjogdHJ1ZSwgLy9cdTYyNTNcdTUzMDVcdTUyNERcdTUxNDhcdTZFMDVcdTdBN0FcdTUzOUZcdTY3MDlcdTYyNTNcdTUzMDVcdTY1ODdcdTRFRjZcclxuICB9LFxyXG4gIGNzczoge1xyXG4gICAgcHJlcHJvY2Vzc29yT3B0aW9uczoge1xyXG4gICAgICBsZXNzOiB7XHJcbiAgICAgICAgbW9kaWZ5VmFyczogY3VzdG9tVmFyaWFibGVzLFxyXG4gICAgICAgIGphdmFzY3JpcHRFbmFibGVkOiB0cnVlLFxyXG4gICAgICB9LFxyXG4gICAgfSxcclxuICB9LFxyXG4gIGRlZmluZToge1xyXG4gICAgX19JTlRMSUZZX1BST0RfREVWVE9PTFNfXzogZmFsc2UsXHJcbiAgICAncHJvY2Vzcy5lbnYnOiBwcm9jZXNzLmVudixcclxuICB9LFxyXG59O1xyXG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkU6XFxcXHdvcmtzYXBjZVxcXFxzdHM1LjBcXFxcbGVnZW5kLWZyYW1ld29ya1xcXFxsZWdlbmQtZnJhbWV3b3JrLXVpXFxcXHNyY1xcXFxzbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdFxcXFxzcmNcXFxcdGhlbWVcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkU6XFxcXHdvcmtzYXBjZVxcXFxzdHM1LjBcXFxcbGVnZW5kLWZyYW1ld29ya1xcXFxsZWdlbmQtZnJhbWV3b3JrLXVpXFxcXHNyY1xcXFxzbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdFxcXFxzcmNcXFxcdGhlbWVcXFxcY3VzdG9tLXZhcmlhYmxlcy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRTovd29ya3NhcGNlL3N0czUuMC9sZWdlbmQtZnJhbWV3b3JrL2xlZ2VuZC1mcmFtZXdvcmstdWkvc3JjL3NtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0L3NyYy90aGVtZS9jdXN0b20tdmFyaWFibGVzLmpzXCI7aW1wb3J0IHsgdGhlbWUgfSBmcm9tICdhbnQtZGVzaWduLXZ1ZS9saWInO1xyXG5pbXBvcnQgY29udmVydExlZ2FjeVRva2VuIGZyb20gJ2FudC1kZXNpZ24tdnVlL2xpYi90aGVtZS9jb252ZXJ0TGVnYWN5VG9rZW4nO1xyXG5cclxuY29uc3QgeyBkZWZhdWx0QWxnb3JpdGhtLCBkZWZhdWx0U2VlZCB9ID0gdGhlbWU7XHJcblxyXG5jb25zdCBtYXBUb2tlbiA9IGRlZmF1bHRBbGdvcml0aG0oZGVmYXVsdFNlZWQpO1xyXG5jb25zdCB0b2tlbiA9IGNvbnZlcnRMZWdhY3lUb2tlbi5kZWZhdWx0KG1hcFRva2VuKTtcclxuXHJcbmV4cG9ydCBkZWZhdWx0IHtcclxuICAnQHByaW1hcnktY29sb3InOiB0b2tlblsncHJpbWFyeS1jb2xvciddLCAvLyBcdTUxNjhcdTVDNDBcdTRFM0JcdTgyNzJcclxuICAnQGJhc2UtYmctY29sb3InOiAnI2ZmZicsXHJcbiAgJ0Bob3Zlci1iZy1jb2xvcic6ICdyZ2JhKDAsIDAsIDAsIDAuMDI1KScsXHJcbiAgJ0Bob3Zlci1iZy1jb2xvci1uaWdodCc6ICdyZ2JhKDI1NSwgMjU1LCAyNTUsIDAuMDI1KScsXHJcbiAgJ0BoZWFkZXItbGlnaHQtYmctaG92ZXItY29sb3InOiAnI2Y2ZjZmNicsXHJcbiAgJ0BoZWFkZXItaGVpZ2h0JzogJzgwcHgnLFxyXG4gICdAaGVhZGVyLXVzZXItaGVpZ2h0JzogJzQwcHgnLFxyXG4gICdAcGFnZS10YWctaGVpZ2h0JzogJzQwcHgnLFxyXG4gICdAdGhlbWUtbGlzdCc6IFsnbGlnaHQnLCAnZGFyaycsICduaWdodCddLFxyXG59O1xyXG4iXSwKICAibWFwcGluZ3MiOiAiO0FBU0EsU0FBUyxlQUFlO0FBQ3hCLE9BQU8sU0FBUzs7O0FDVmllLFNBQVMsYUFBYTtBQUN2Z0IsT0FBTyx3QkFBd0I7QUFFL0IsSUFBTSxFQUFFLGtCQUFrQixZQUFZLElBQUk7QUFFMUMsSUFBTSxXQUFXLGlCQUFpQixXQUFXO0FBQzdDLElBQU0sUUFBUSxtQkFBbUIsUUFBUSxRQUFRO0FBRWpELElBQU8sMkJBQVE7QUFBQSxFQUNiLGtCQUFrQixNQUFNLGVBQWU7QUFBQTtBQUFBLEVBQ3ZDLGtCQUFrQjtBQUFBLEVBQ2xCLG1CQUFtQjtBQUFBLEVBQ25CLHlCQUF5QjtBQUFBLEVBQ3pCLGdDQUFnQztBQUFBLEVBQ2hDLGtCQUFrQjtBQUFBLEVBQ2xCLHVCQUF1QjtBQUFBLEVBQ3ZCLG9CQUFvQjtBQUFBLEVBQ3BCLGVBQWUsQ0FBQyxTQUFTLFFBQVEsT0FBTztBQUMxQzs7O0FEbEJBLElBQU0sbUNBQW1DO0FBYXpDLElBQU0sY0FBYyxDQUFDLFFBQVE7QUFDM0IsU0FBTyxRQUFRLGtDQUFXLEtBQUssR0FBRztBQUNwQztBQUNBLElBQU8sc0JBQVE7QUFBQSxFQUNiLE1BQU0sUUFBUSxJQUFJLGFBQWEsZUFBZSxNQUFNO0FBQUEsRUFDcEQsTUFBTSxRQUFRLElBQUk7QUFBQSxFQUNsQixTQUFTO0FBQUEsSUFDUCxPQUFPO0FBQUE7QUFBQSxNQUVMO0FBQUEsUUFDRSxNQUFNO0FBQUEsUUFDTixhQUFhO0FBQUEsTUFDZjtBQUFBO0FBQUEsTUFFQTtBQUFBLFFBQ0UsTUFBTTtBQUFBLFFBQ04sYUFBYSxZQUFZLEtBQUssSUFBSTtBQUFBLE1BQ3BDO0FBQUEsTUFDQTtBQUFBLFFBQ0UsTUFBTTtBQUFBLFFBQ04sYUFBYTtBQUFBLE1BQ2Y7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sT0FBTztBQUFBO0FBQUEsTUFFTCxRQUFRO0FBQUEsUUFDTixRQUFRO0FBQUE7QUFBQSxRQUNSLGNBQWM7QUFBQTtBQUFBLE1BQ2hCO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFNBQVMsQ0FBQyxJQUFJLENBQUM7QUFBQSxFQUNmLGNBQWM7QUFBQSxJQUNaLFNBQVMsQ0FBQyxrQ0FBa0Msc0JBQXNCLGdDQUFnQztBQUFBLElBQ2xHLFNBQVMsQ0FBQyxVQUFVO0FBQUEsRUFDdEI7QUFBQSxFQUNBLE9BQU87QUFBQTtBQUFBLElBRUwsZUFBZTtBQUFBLE1BQ2IsVUFBVTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsZUFBZTtBQUFBLE1BQ2pCO0FBQUEsSUFDRjtBQUFBLElBQ0EsZUFBZTtBQUFBLE1BQ2IsUUFBUTtBQUFBO0FBQUEsUUFFTixnQkFBZ0I7QUFBQSxRQUNoQixnQkFBZ0I7QUFBQSxRQUNoQixnQkFBZ0I7QUFBQSxRQUNoQixhQUFhLElBQUk7QUFFZixjQUFJLEdBQUcsU0FBUyxjQUFjLEdBQUc7QUFDL0IsbUJBQU8sR0FBRyxTQUFTLEVBQUUsTUFBTSxlQUFlLEVBQUUsQ0FBQyxFQUFFLE1BQU0sR0FBRyxFQUFFLENBQUMsRUFBRSxTQUFTO0FBQUEsVUFDeEU7QUFBQSxRQUNGO0FBQUEsTUFDRjtBQUFBLElBQ0Y7QUFBQSxJQUNBLFFBQVE7QUFBQSxJQUNSLFFBQVE7QUFBQTtBQUFBLElBQ1IsV0FBVztBQUFBO0FBQUEsSUFDWCxtQkFBbUI7QUFBQTtBQUFBLElBQ25CLHVCQUF1QjtBQUFBO0FBQUEsSUFDdkIsUUFBUTtBQUFBO0FBQUEsSUFDUixhQUFhO0FBQUE7QUFBQSxFQUNmO0FBQUEsRUFDQSxLQUFLO0FBQUEsSUFDSCxxQkFBcUI7QUFBQSxNQUNuQixNQUFNO0FBQUEsUUFDSixZQUFZO0FBQUEsUUFDWixtQkFBbUI7QUFBQSxNQUNyQjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQUEsRUFDQSxRQUFRO0FBQUEsSUFDTiwyQkFBMkI7QUFBQSxJQUMzQixlQUFlLFFBQVE7QUFBQSxFQUN6QjtBQUNGOyIsCiAgIm5hbWVzIjogW10KfQo=
