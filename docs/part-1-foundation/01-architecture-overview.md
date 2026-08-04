# 第 1 章 系统全景与架构哲学

> 三层模块化、依赖反转、ADB 与 Web 双引擎统一编排——adb-bot 的整体架构设计。

---

adb-bot 不是一个简单的 ADB 命令封装工具。它是一个面向 Android 设备的 RPA 自动化平台，需要同时支撑实时投屏、AI 对话驱动、可视化流程编排、行为仿真、多设备群控等能力。

把这些功能堆在一个工程里很容易变成"屎山"。我们的架构决策是：**按能力分层、按模块解耦、用接口隔离**。

## 三层模块化

```mermaid
flowchart TB
    subgraph Web 层
        WA[投屏 WebSocket]
        WB[AI 对话 SSE]
        WC[流程管理 REST]
        WD[设备管理 REST]
    end
    
    subgraph 能力层
        C1[AI 引擎]
        C2[流程引擎]
        C3[录制引擎]
    end
    
    subgraph 基础层
        B1[ADB 命令]
        B2[仿真注入]
        B3[机器视觉]
        B4[输入引擎]
    end
    
    Web 层 --> 能力层
    能力层 --> 基础层
```

| 层 | 模块 | 职责 |
|---|------|------|
| Web 层 | adb-bot-web | HTTP/WebSocket 入口、投屏、授权 |
| 能力层 | adb-bot-ai / adb-bot-process | AI 对话、流程引擎、录制 |
| 基础层 | adb-bot-common | ADB 命令、仿真、视觉、输入 |

基础层不依赖能力层，能力层不依赖 Web 层。依赖方向严格向下。

### 为什么这样分

**仿真和视觉放在基础层**：因为 AI 工具调用和流程节点执行都需要"点击""滑动""找元素"。如果仿真逻辑写在 AI 模块里，流程引擎就没法复用。放在基础层，两个上层模块都能调用。

**录制引擎放在 AI 模块**：因为录制只发生在 AI 对话过程中（@Recordable 注解贴在 AI 工具方法上），流程引擎执行时不录制。但生成的 BPMN XML 交给流程模块消费。

### 依赖反转

基础层定义接口（`TextTyper`、`IDevice`），具体实现（`AdbKeyboardTyper`、`AutoDetectDevice`）也在基础层。能力层通过接口调用，不关心实现细节。

这带来了一个好处：测试时可以 mock 基础层接口，不需要真实设备。

## 双引擎统一编排

ADB 后端和 Selenium Web 后端共享同一套流程引擎。流程定义中通过 `support` 变量切换后端，一份 BPMN 定义可以同时驱动 Android 设备和浏览器。

目前 Selenium 后端是实验性的（仅实现了 Open 和 Manual），但架构上已经预留了完整的扩展点。

## 技术栈选择

| 领域 | 选型 | 理由 |
|------|------|------|
| 流程引擎 | Activiti 8 (Flowable) | Java 生态最成熟，BPMN 2.0 完整支持 |
| AI 框架 | Spring AI | Function Calling 原生支持，OpenAI 兼容 |
| ADB 通信 | jadb | 纯 Java ADB 客户端，无外部进程依赖 |
| 投屏 | scrcpy server + WebSocket | 业界标准方案，延迟低 |
| 机器视觉 | OpenCV (JavaCV) + Tesseract 4 | 模板匹配 + OCR 双能力 |
| 前端 | 原生 HTML + BPMN.js | 轻量，无框架依赖 |

## 关键代码

| 位置 | 说明 |
|------|------|
| `adb-bot-common` | 基础层：ADB 命令、仿真、视觉、输入 |
| `adb-bot-ai` | 能力层：AI 对话、工具注册、录制 |
| `adb-bot-process` | 能力层：Activiti 集成、条件表达式 |
| `adb-bot-web` | Web 层：HTTP/WS 入口、投屏、授权 |

---

> 本文是《adb-bot 技术内幕》系列第 1 章。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [目录](../README.md) ｜ [第 2 章 设备发现与并发治理 →](02-device-and-concurrency.md)
