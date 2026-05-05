/**
 * Slide Loader - 动态加载 Markdown 幻灯片片段
 * 从 slides/ 目录加载 .md 文件并合并到 textarea#source
 * 
 * 将此脚本放在 <head> 中，使用同步 XMLHttpRequest 确保页面渲染前完成加载
 */


  // ============================================
  // 配置：定义要加载的幻灯片文件列表
  // ============================================
  const SLIDES_CONFIG = {
    // 从 index2.html 拆分的 56 个页面 (00.md - 55.md)
    slides: Array.from({length: 56}, (_, i) => `slides/${String(i).padStart(2, '0')}.md`)
  };

  // ============================================
  // 同步加载单个文件 (XMLHttpRequest)
  // ============================================
  function loadFileSync(url) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, false); // 同步请求
    xhr.send();
    if (xhr.status === 200) {
      return xhr.responseText;
    } else {
      console.error('加载失败: ' + url);
      return '\n---\n\n## ⚠️ 加载失败\n\n文件 `' + url + '` 加载失败\n\n状态码: ' + xhr.status + '\n';
    }
  }

  // ============================================
  // 同步加载所有幻灯片
  // ============================================
  function loadAllSlidesSync() {
    var contents = [];
    var total = SLIDES_CONFIG.slides.length;

    for (var i = 0; i < total; i++) {
      var file = SLIDES_CONFIG.slides[i];
      var content = loadFileSync(file);
      contents.push(content);
    }

    // 合并所有内容：去除每个文件开头和末尾的---，然后用---连接
    var normalizedContents = contents.map(function(c) {
      return c
        .replace(/^\s*---\s*/, '')   // 去除开头的 ---
        .replace(/\s*---\s*$/, '')   // 去除末尾的 ---
        .trim();
    });
    var fullContent = normalizedContents.join('\n\n---\n\n');

    // 设置到 textarea
    var sourceEl = document.getElementById('source');
    if (sourceEl) {
      sourceEl.innerHTML = fullContent;
    }
     console.log("--targetTextArea----", sourceEl.innerHTML);

    console.log('幻灯片加载完成，共 ' + total + ' 个文件');
    return fullContent;
  }




