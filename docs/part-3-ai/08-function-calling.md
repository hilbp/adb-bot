# 第 8 章 Function Calling 工具体系

> 16 个工具函数注册为 Spring AI Tool，AI 模型自主决策调用哪个工具、传什么参数。

---

让 AI 控制手机，核心问题不是 AI 本身，而是"怎么把设备操作能力暴露给 AI"。adb-bot 的方案是 Function Calling：把每个设备操作封装为一个工具函数，AI 根据用户自然语言指令自主选择调用。

## 核心设计

### 工具注册：注解驱动

每个工具方法标注 `@Tool(description="...")` + `@ToolParam`，Spring AI 的 `MethodToolCallbackProvider` 自动扫描注册。AI 模型看到的是工具的 description 和参数描述，它根据这些自然语言描述决定何时调用、传什么值。

```
用户："打开抖音"
→ AI 分析：需要启动应用
→ AI 选择工具：startApp(serial, packageName="com.ss.android.ugc.aweme")
→ 工具执行：AdbCmdUtil.openApp()
→ 返回结果给 AI
→ AI 回复用户："已打开抖音"
```

### 16 个工具函数

| 类别 | 工具 | 说明 |
|------|------|------|
| 感知 | screenshot | 截图返回 URL |
| 感知 | recognizeScreen | 多模态识别屏幕内容 |
| 感知 | getUiTree | 获取 UI 元素树 |
| 应用 | listApps | 查询已安装应用 |
| 应用 | startApp | 启动应用 |
| 操作 | tap | 点击坐标 |
| 操作 | swipe | 滑动（支持循环） |
| 操作 | inputText | 输入文字 |
| 操作 | pressKey | 按键 |
| 操作 | back | 返回 |
| 控制流 | beginCondition | 开始条件块 |
| 控制流 | endCondition | 结束条件块 |
| AI | aiTask | 录制 AI 决策节点 |
| 群控 | listDevices | 列出设备 |
| 群控 | execMulti | 多设备并发执行 |

### 场景化提示词路由

同一个 AI 模型，在不同场景下需要不同的系统提示词：

| 场景 | 提示词焦点 | 触发条件 |
|------|-----------|---------|
| COMMAND | 执行用户指令，返回结果 | 默认对话 |
| RECORD | 录制脚本，生成 BPMN | 录制开关打开 |
| BPMN_NODE | 流程回放中的 AI 决策 | 流程引擎调用 |

三套提示词互不污染。`PromptProvider.buildSystemPrompt(scenario, serial)` 按场景动态构建，提示词模板存为外部 `.st` 文件，修改不需要重启。

### 上下文窗口管理

历史对话不是全部传给模型的。只保留最近 5 轮的 **user 消息**，丢弃 assistant 回复。

为什么丢 assistant？因为模型自身生成的回复（尤其是包含工具调用结果的长文本）会污染上下文。比如 AI 回复"已点击搜索按钮，搜索框出现了..."，这段文字在下一轮对话中会被模型当作事实依据，但它实际上是 AI 的"猜测"而非设备真实状态。

保留 user 消息是为了让模型理解"用户想做什么"的连续意图。

### 输入超长拦截

单次输入超过 8000 字符直接拒绝调用模型。这是防御性设计：避免前端误传大量文本（如整个 UI 树未精简）导致 token 消耗暴增。

### UI 树精简

`getUiTree` 不是把原始 XML dump 给 AI。它用正则提取所有 `<node>`，只保留有 text / content-desc / resource-id 的节点，bounds 转为中心坐标，最多 200 个节点。这样 AI 拿到的是精简后的元素列表，而不是几十 KB 的 XML。

### MCP Server 暴露

`ToolCallbackProvider` 同时用于 Spring AI 内部调用和 MCP 协议暴露。外部 MCP 客户端（如 Claude Desktop）可以直接调用 adb-bot 的工具函数控制设备，无需额外开发。

## 关键代码

| 方法 | 说明 |
|------|------|
| `DeviceActionTools` | 16 个 @Tool 方法集合 |
| `ChatService.chatInternal()` | 对话编排核心（提示词 + 工具 + 消息构建） |
| `PromptProvider.buildSystemPrompt()` | 场景化提示词路由 |
| `ChatService.buildMessages()` | 历史窗口裁剪（5 轮 user only） |
| `DeviceActionTools.simplifyUiXml()` | UI XML → 精简元素列表 |

---

> 本文是《adb-bot 技术内幕》系列第 8 章。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 第 7 章 双模型适配层](07-dual-model-adapter.md) ｜ [目录](../README.md) ｜ [第 9 章 并发与中断控制 →](09-concurrency-control.md)
