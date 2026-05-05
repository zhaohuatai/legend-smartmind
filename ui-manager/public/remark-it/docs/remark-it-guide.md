# remark-it PPT 制作指南

基于 remarkjs 的幻灯片框架使用手册

---

## 目录

1. [项目结构](#项目结构)
2. [快速开始](#快速开始)
3. [全局模板](#全局模板)
4. [主题系统](#主题系统)
5. [颜色系统](#颜色系统)
6. [布局系统](#布局系统)
7. [常用组件](#常用组件)
8. [增量内容](#增量内容)
9. [公式支持](#公式支持)
10. [表格](#表格)
11. [图表](#图表)
12. [媒体](#媒体)
13. [演讲者备注](#演讲者备注)
14. [快捷键](#快捷键)
15. [注意事项](#注意事项)

---

## 项目结构

```
remark-it/
├── index2.html          # 入口文件
├── css/                 # 样式文件
│   ├── nord-dark.css    # Nord 深色主题
│   ├── nord-light.css   # 浅色主题
│   ├── font-nord.css    # Nord 字体颜色
│   ├── typo.css         # 中文排版优化
│   └── ...
├── js/                  # 脚本文件
│   ├── remark.min.js    # 核心库
│   └── slide-loader.js  # 幻灯片加载器
├── slides/              # 幻灯片片段
│   ├── 00.md            # 全局模板
│   ├── 01.md            # 第1页
│   └── ...
└── static/              # 图片、视频等资源
```

---

## 快速开始

### 页面基本结构

```markdown
class: nord-dark, center, middle

# 页面标题

内容...
```

### 页面分隔

用 `---` 分隔不同幻灯片页面：

```markdown
class: nord-dark, center, middle

# 封面

---

class: nord-light

## 第一页内容

---

## 第二页内容
```

---

## 全局模板

### 模板页定义

在页面顶部设置 `layout: true`，该页会作为模板应用到其后所有页面：

```markdown
layout: true
class: typo, typo-selection
```

### 模板页特性

- 模板页可以有多个，每个只影响其后的幻灯片
- 模板页内容会追加到后续页面
- 常用模板配置：

```markdown
layout: true
class: typo, typo-selection
```

这会自动应用中英文排版优化和选中样式。

---

## 主题系统

### 单页主题

在页面顶部设置 class：

```markdown
class: nord-dark
class: nord-light
class: nord-dark, center, middle
```

### 主题选择

| 主题 | 适用场景 |
|-----|---------|
| `nord-dark` | 封面页、章节页、强调页 |
| `nord-light` | 内容页、列表页、表格页 |

### 代码高亮主题

在入口文件中配置：

```javascript
highlightStyle: "github"  // 可选: arta, dark, monokai, solarized-dark, vs, zenburn...
```

---

## 颜色系统

### Nord 色系（16色）

| 类名 | 颜色 | 用途 |
|-----|------|------|
| `.nord0` | 极夜黑 | 深色背景 |
| `.nord1` | 深黑 | 深色文字 |
| `.nord3` | 灰 | 正文文字（比纯黑柔和） |
| `.nord4` | 雪白 | 浅色背景上的文字 |
| `.nord8` | 红 | 警告、重要 |
| `.nord9` | 橙 | 提示、注意 |
| `.nord10` | 黄 | 高亮 |
| `.nord11` | 绿 | 成功、正面 |
| `.nord12` | 青 | 信息、链接 |
| `.nord13` | 蓝 | 链接、信息 |
| `.nord14` | 紫 | 特殊、创意 |
| `.nord15` | 玫瑰 | 强调 |

**使用**：`.nord11[绿色文字]`

### Open Color 色系

提供15组颜色，每组10个色阶：

```markdown
.oc-red-0[最浅] ... .oc-red-9[最深]
.oc-bg-gray-0[背景] ... .oc-bg-gray-9[深色背景]
```

### 背景色

```markdown
.bg-nord11[绿色背景]
.bg-nord12[青色背景]
.oc-bg-blue-2[浅蓝背景]
```

### 饱和度调节

```markdown
.sat-0[无饱和] ... .sat-100[完全饱和]
```

---

## 布局系统

### 基础对齐

```markdown
.left[左对齐内容]
.center[居中内容]
.right[右对齐内容]
```

### 页面居中

```markdown
class: center, middle
```

### 分栏布局

```markdown
.column-2[
左栏内容

---

右栏内容
]

.column-3[
左栏
---
中栏
---
右栏
]
```

分栏变体：

```markdown
.column-2.column-norule[无分隔线]
.column-2.column-gap-xs[小间距]
.column-5.column-norule.column-gap-xs[5栏无规则]
```

### 网格布局（Pure.css）

```markdown
.pure-g[
  .pure-u-12-24.font-xs.gutter-8[
    左侧占12/24
  ]
  .pure-u-12-24.font-xs.gutter-8[
    右侧占12/24
  ]
]

.pure-u-16-24[占16/24]
.pure-u-8-24[占8/24]
```

常用网格比例：
- `pure-u-12-24` = 50%
- `pure-u-16-24` = 66.7%
- `pure-u-8-24` = 33.3%

### Border 布局

将页面划分为5个区域：north、west、east、b-center、south

```markdown
class: border-layout

.north.height-20[顶部区域]
.west.height-60.width-20[左侧区域]
.east.height-60.width-20[右侧区域]
.b-center[中间区域]
.south.height-20[底部区域]
```

Border 布局示例 - 上下结构：

```markdown
class: border-layout, nord-light

.north.height-69[
  .card.noborder.noround.m-0.width-100.height-100[
    .img[![](static/image.jpg)]
  ]
]
.south.height-31.center[
  .font-xxl.pt-xs[标题文字]
]
```

Border 布局示例 - 左右结构：

```markdown
class: border-layout, nord-dark

.east.height-100.width-53.p-xxs.ml-m[
  .card.noborder.noround.m-0.width-100.height-100[
    .img[![](static/image.jpg)]
  ]
]
.b-center[
  .right.pt-xxl[
    .font-xxl.nord4[标题]
  ]
]
```

### 绝对定位

使用 `.abs-layout` 配合位置类自由放置元素：

```markdown
.abs-layout.top-26.left-58.width-23[
  内容
]

.abs-layout.right-12.bottom-4.width-20.center[
  内容
]
```

位置类：
- `.top-0` 至 `.top-100`（百分比）
- `.left-0` 至 `.left-100`（百分比）
- `.right-0` 至 `.right-100`（百分比）
- `.bottom-0` 至 `.bottom-100`（百分比）

绝对定位常用于：
- 浮动 header/footer
- 错落有致的图片/文字布局
- 半透明文字覆盖在图片上

```markdown
background-image: url(static/bg.png)

.abs-layout.p-m.top-23.left-2.width-47.oc-bg-black.opacity-40.nord-dark[
  半透明背景上的文字
]
```

### 经典布局 - 背景图片+渐变

```markdown
background-image: linear-gradient(150deg, rgba(0, 0, 0, 100%), rgba(0, 0, 0, 85%), rgba(0, 0, 0, 40%)),url(static/image.jpg)
class: nord-dark, center, middle

## 标题文字
```

---

## 常用组件

### 字体大小

```markdown
.font-xxl[特大标题]    # 封面主标题
.font-xl[大标题]       # 章节标题
.font-l[较大文字]      # 小标题
.font-m[正常文字]      # 正文（默认）
.font-s[小字]          # 注释
.font-xs[超小字]       # 脚注
.font-sm[小字体]       # 表格/卡片内文字
.font-md[中等字体]
.font-lg[大字体]
```

### 文字样式

```markdown
**粗体**  *斜体*  ~~删除线~~
<small>小字</small>  <big>大字</big>
<abbr title="全称">缩写</abbr>
<mark>高亮标记</mark>
```

### 字间距

```markdown
.letter-spacing-20[字间距20]
.letter-spacing-50[字间距50]
```

### 透明度

```markdown
.opacity-60[60%透明度]
```

### 图标（Remix Icon）

```markdown
.ri-mickey-line[]
.ri-mickey-line.icon-inline.nord14[]  # 内联图标+颜色
.ri-mail-check-line.icon-inline.nord11[]
.ri-calendar-check-line.icon-inline.nord14[]
.ri-mail-unread-line.icon-top[图标在上方]
```

### 图片

```markdown
![](图片URL)
.img[![](图片URL)]
```

图片浮动布局：

```markdown
.float-left.width-30.pt-xxs.pr-xs[![](图片URL)]
.float-right.width-27.pt-xxs.pl-xs[![](图片URL)]
.block-middle.width-33[![](图片URL)]
```

图片宽度类：`.width-20`、`.width-27`、`.width-30`、`.width-33`、`.width-50`、`.width-88`、`.width-100`

图片与背景融合（深色主题）：

```markdown
.block-middle.width-88.nord-dark-steep-img[
  ![](图片URL)
]
```

### 背景图片

```markdown
background-image: url(图片URL)
background-image: linear-gradient(150deg, rgba(0,0,0,100%), rgba(0,0,0,85%)), url(图片URL)
```

### 卡片

```markdown
.card[卡片内容]
.card.noborder.noround[无边框无圆角]
.card.gradient.lime[渐变背景]
.card.blue[蓝色背景]
.card.book[图书样式，图片在左/右]
.card.arrow-bottom[带下箭头]
.card.arrow-top[带上箭头]
.card.arrow-left[带左箭头]
.card.arrow-right[带右箭头]
.card.dark.grape[深色背景]
.card.frame[相框样式]
.card.preview-win[可点击预览图片]
.card.middle[垂直居中]
```

卡片矩阵：

```markdown
.card.matrix.border-bottom-solid.border-middle-dashed.border-right-dashed[
  .card.middle.border-bottom-none.width-20[内容]
  .card.width-20[内容]
]
```

### 文本框

```markdown
.rect[文本框]
.rect.gray[灰色]
.rect.red[红色]
.rect.dark[深色背景]
.rect.round-md[中等圆角]
.rect.round-lg[大圆角]
.rect.round-xxl[超大圆角]
.rect.transbg[半透明背景]
.rect.border[带边框]
.rect.sat-60[降低饱和度]
```

### 圆形

```markdown
.circle.blue.border[蓝色圆圈]
.circle.green.border.width-10[绿色圆圈]
.circle.orange.width-20[橙色大圆]
```

### 线条

```markdown
.hline[水平线]
.hline.gray.width-90[灰色水平线]
.vline.gray.height-66[垂直线]
```

### 间距工具

```markdown
.p-xxs, .p-xs, .p-s, .p-m, .p-l, .p-xl    # padding
.m-xxs, .m-xs, .m-s, .m-m, .m-l, .m-xl    # margin
.pt-xxxs, .pt-xxxs, .pt-xxs, .pt-xs, .pt-s, .pt-m, .pt-l, .pt-xl, .pt-xxl, .pt-xxxl  # padding-top
.pb-xxs, .pb-xxs  # padding-bottom
.pl-s, .pr-s  # padding-left/right
.ml-s, .mr-s  # margin-left/right
.mt-l, .mt-xxxl  # margin-top
.mb-xs  # margin-bottom
```

### 相对定位

```markdown
.relative-layout.top-23[相对定位]
```

---

## 表格

### 基础表格

```markdown
.st.noborder.st-hline.font-sm.mb-xs[
| 名称 | 价格 | 数量 |
| ---- | ---- | ----: |
| 香蕉 | $1  | 5    |
| 苹果 | $1  | 6    |
]
```

### 表格样式变体

| 样式类 | 效果 |
|--------|------|
| `.st` | 基础表格 |
| `.st-hline` | 横线 |
| `.st-vline` | 竖线 |
| `.st-allline` | 全边框 |
| `.noborder` | 无边框 |
| `.font-sm` | 小字体 |
| `.mb-xs` | 底部间距 |

```markdown
.st.st-hline.font-sm.mb-xs[表格]
.st.st-allline.font-sm.mb-xs[全边框表格]
.st.st-vline.font-sm.mb-xs[竖线表格]
```

---

## 增量内容

用 `--` 分隔，逐步显示：

```markdown
## 增量内容

- 第一点（立即显示）

--
- 第二点（按键后显示）

--
- 第三点（按键后显示）
```

---

## 公式支持

### 行内公式

```markdown
当 $a \ne 0$ 时，方程的解为 $x = {-b \pm \sqrt{b^2-4ac} \over 2a}$
```

### 块级公式

```markdown
$$x = {-b \pm \sqrt{b^2-4ac} \over 2a}$$
```

### 复杂公式

```markdown
\[
  \mathbf{V}_1 \times \mathbf{V}_2 =
   \begin{vmatrix}
    \mathbf{i} & \mathbf{j} & \mathbf{k} \\
    a & b & c \\
    d & e & f \\
   \end{vmatrix}
\]
```

### 多行公式

```markdown
<p>
\begin{align}
\dot{x} & = \sigma(y-x) \\
\dot{y} & = \rho x - y - xz \\
\dot{z} & = -\beta z + xy
\end{align}
</p>
```

---

## 图表

### Mermaid 图表

注意：Mermaid 图表需要用 `<pre>` 标签包裹：

```markdown
.mermaid[
<pre>
  graph LR;
  A-->B;
  A-->C;
  B-->D;
  C-->D;
</pre>
]
```

类图：

```markdown
.mermaid[
<pre>
  classDiagram
  Class01 <|-- AveryLongClass : Cool
  Class03 *-- Class04
  Class05 o-- Class06
</pre>
]
```

时序图：

```markdown
.mermaid[
<pre>
  sequenceDiagram
  participant Alice
  participant Bob
  Alice->>John: Hello John, how are you?
  loop Healthcheck
    John->>John: Fight against hypochondria
  end
  John-->>Alice: Great!
</pre>
]
```

甘特图：

```markdown
.mermaid[
<pre>
  gantt
  dateFormat  YYYY-MM-DD
  title 甘特图示例
  section 阶段一
  任务1 :done, 2024-01-01, 2024-01-10
  任务2 :active, 2024-01-11, 5d
</pre>
]
```

### SVG 图片

推荐使用 [ChartCube](https://chartcube.alipay.com/) 或 [drawio](https://github.com/jgraph/drawio) 导出 SVG 矢量图：

```markdown
.img[![](static/chart.svg)]
```

---

## 媒体

### 图片

```markdown
![](static/image.jpg)
.img[![](static/image.jpg)]
```

### 视频

```markdown
<video width="100%" height="420" controls>
    <source src="static/video.mp4" type="video/mp4">
</video>
```

### iframe

```markdown
<iframe width="100%" height="70%" src="http://example.org/" allowfullscreen frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
```

---

## 演讲者备注

用 `???` 添加，按 **P** 键查看：

```markdown
## 幻灯片标题

内容

???
演讲者备注内容
- 提醒1
- 提醒2
```

---

## 页面命名与模板引用

### 命名页面

```markdown
name: slide-name

# 页面标题

内容
```

### 引用模板

```markdown
template: slide-name

### 追加内容

会追加到 slide-name 的内容之后
```

---

## 快捷键

| 按键 | 功能 |
|-----|------|
| `→` / `↓` / `Space` | 下一页 |
| `←` / `↑` | 上一页 |
| `P` | 演讲者视图（显示备注） |
| `C` | 克隆窗口 |
| `F` | 全屏 |
| `H` | 显示帮助/快捷键列表 |

---

## 注意事项

1. **缩进限制**：不能超过2层缩进
2. **页面分隔**：用 `---` 分隔幻灯片
3. **文件位置**：幻灯片放在 `slides/` 目录
4. **资源路径**：静态资源放在 `static/` 目录
5. **Mermaid 图表**：必须用 `<pre>` 标签包裹
6. **HTML 转义**：在公式中使用 `&amp;` 代替 `&`

---

## 示例速查

### 封面页

```markdown
class: nord-dark, center, middle

# .letter-spacing-20[主标题]

<small>.letter-spacing-50[副标题]</small>
```

### 章节页

```markdown
class: nord-dark, center, middle

# .letter-spacing-20[01]

## 章节标题
```

### 内容页

```markdown
class: nord-light

## 页面标题

- .nord11[带颜色的] 列表项
- .ri-check-line.icon-inline[] 带图标的项

.column-2[
左侧内容

---
右侧内容
]
```

### 图文混排

```markdown
class: nord-light

## 标题

.column-2[
## 文字说明

- 要点1
- 要点2

---

.img[![](static/image.jpg)]
]
```

### 表格页

```markdown
class: nord-light

## 数据表格

.st.st-allline.font-sm.mb-xs[
| 名称 | 价格 | 数量 |
| ---- | ---- | ----: |
| 香蕉 | $1  | 5    |
| 苹果 | $1  | 6    |
]
```

### 增量内容页

```markdown
class: nord-light

## 逐步展示

- 第一点

--
- 第二点

--
- 第三点
```

---

*基于 remarkjs 构建 | remark-it 框架*
