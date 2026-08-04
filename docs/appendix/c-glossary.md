# 附录 C 术语表

> adb-bot 涉及的核心术语和缩写。

---

| 术语 | 说明 |
|------|------|
| **ADB** | Android Debug Bridge，Android 调试桥。PC 与 Android 设备通信的命令行工具。 |
| **scrcpy** | 开源的 Android 屏幕镜像工具，通过设备端 server 编码 H.264 推送到电脑。 |
| **sendevent** | Linux input 子系统命令，向 `/dev/input/eventN` 设备节点写入触摸/按键事件。 |
| **getevent** | Linux input 子系统命令，读取 input 设备的能力和事件信息。 |
| **BPMN** | Business Process Model and Notation，业务流程建模标准。adb-bot 用 BPMN 2.0 定义自动化流程。 |
| **Activiti** | Java 生态的开源 BPMN 流程引擎（现 Flowable）。 |
| **delegateExpression** | BPMN serviceTask 的属性，用 EL 表达式指定执行的 Spring Bean。 |
| **Function Calling** | AI 模型的工具调用能力。模型根据自然语言指令自主选择并调用预注册的函数。 |
| **MCP** | Model Context Protocol，AI 工具暴露协议，允许外部客户端调用工具函数。 |
| **MSE** | Media Source Extensions，浏览器 API，允许 JavaScript 动态构建媒体流。 |
| **JMuxer** | 纯 JavaScript 的 H.264 软解码器，通过 MSE 播放视频。 |
| **SPS/PPS** | H.264 的序列参数集和图像参数集，解码器初始化必需的基准帧。 |
| **IDR** | H.264 的即时解码刷新帧（I 帧），不依赖其他帧可独立解码。 |
| **NAL** | H.264 的网络抽象层单元，每个 NAL 有类型标识（SPS=7, PPS=8, IDR=5）。 |
| **STOMP** | Simple Text Oriented Messaging Protocol，基于 WebSocket 的消息订阅协议。 |
| **SockJS** | WebSocket 降级方案，WebSocket 不可用时自动切换到 HTTP 轮询。 |
| **OCR** | Optical Character Recognition，光学字符识别。adb-bot 用 Tesseract 4。 |
| **OpenCV** | 开源计算机视觉库，用于模板匹配。 |
| **TM_CCORR_NORMED** | OpenCV 的归一化互相关模板匹配算法。 |
| **UiAutomator** | Android 的 UI 自动化框架，可 dump 页面 XML 布局。 |
| **XPath** | XML Path Language，在 XML 中定位节点的查询语言。 |
| **ADBKeyBoard** | 自定义输入法 APK，监听广播直接 commitText 上屏。 |
| **T9 九宫格** | 传统手机九键拼音输入法布局（ABC/DEF/GHI...）。 |
| **track id** | Linux 触摸事件的触点追踪标识，每个触摸点有唯一 id。 |
| **BTN_TOOL_FINGER** | Linux input 事件，表示手指接触触摸屏。 |
| **ABS_MT_TOUCH_MAJOR** | Linux input 事件，表示触摸面积。 |
| **HMAC** | Hash-based Message Authentication Code，哈希消息认证码。 |
| **RSA** | 非对称加密算法，公钥验签，私钥签发。 |
| **CAS** | Compare-And-Swap，无锁化原子操作。 |
| **AOP** | Aspect-Oriented Programming，面向切面编程。 |
| **EnvironmentPostProcessor** | Spring 扩展点，在自动配置之前执行环境准备。 |
| **VLM** | Vision Language Model，视觉语言大模型（多模态模型）。 |

---

> 本文是《adb-bot 技术内幕》系列附录 C。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 附录 B 技术点索引表](b-tech-index.md) ｜ [目录](../README.md)
