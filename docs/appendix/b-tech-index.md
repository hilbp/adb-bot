# 附录 B 技术点索引表

> 133 个技术点速查，按主题分类，标注章节位置。

---

## 实时投屏与操控引擎

| 技术点 | 章节 |
|--------|------|
| scrcpy server v4.1 启动协议 | 第 3 章 |
| serial→固定端口映射 + scid 隔离 | 第 3 章 |
| dummy byte 就绪检测 | 第 3 章 |
| SPS/PPS/IDR 基准帧缓存 | 第 3 章 |
| ConcurrentWebSocketSessionDecorator 异步发送 | 第 3 章 |
| screenrecord 降级模式 | 第 4 章 |
| JMuxer H.264 软解码 | 第 4 章 |
| 追帧策略 (1.5s 跳帧) | 第 4 章 |
| 触控坐标归一化换算 | 第 5 章 |
| inputProxy 焦点桥接 | 第 5 章 |
| 键盘事件转发协议 | 第 5 章 |
| 文字输入缓冲合并 (100ms) | 第 5 章 |
| IME 组词三段式处理 | 第 5 章 |
| Shift+Enter 文案匹配点击 | 第 5 章 |
| 多观看者共享 session | 第 3 章 |
| 会话复用机制 | 第 3 章 |
| 启动清理 (残留 forward) | 第 3 章 |
| 批量并行启动 | 第 4 章 |

## 日志实时推送引擎

| 技术点 | 章节 |
|--------|------|
| Logback Appender → STOMP 桥接 | 第 6 章 |
| ThreadLocal 设备上下文路由 | 第 6 章 |
| LogBuffer 环形缓冲 (200 条) | 第 6 章 |
| SockJS 降级 + STOMP | 第 6 章 |
| volatile + 静态注入 | 第 6 章 |

## AI 驱动与智能编排引擎

| 技术点 | 章节 |
|--------|------|
| OpenAI 兼容协议统一接入 | 第 7 章 |
| 双模型缓存 (文本/多模态) | 第 7 章 |
| 多模态可用性探测 (256×256) | 第 7 章 |
| SSE 流式对话 (120s 超时) | 第 7 章 |
| Function Calling 工具注册 | 第 8 章 |
| 场景化提示词路由 | 第 8 章 |
| 提示词外部化 (.st) | 第 8 章 |
| 设备排他锁 (CAS) | 第 9 章 |
| 上下文窗口管理 (5 轮) | 第 8 章 |
| ProcessStopFlag 按设备隔离 | 第 9 章 |
| 群控并发调度 (ThreadPool 8) | 第 9 章 |
| MCP Server 规格暴露 | 第 8 章 |

## 零侵入录制与 BPMN 生成

| 技术点 | 章节 |
|--------|------|
| @Recordable 注解 + AOP | 第 10 章 |
| RecordingAspect @Around | 第 10 章 |
| ActionExecutedEvent 事件驱动 | 第 10 章 |
| 循环体智能去重 | 第 11 章 |
| BPMN 2.0 XML 生成 | 第 11 章 |
| 控制流映射 | 第 11 章 |
| BPMNDI 坐标自动布局 | 第 11 章 |
| AI 决策节点录制 | 第 11 章 |

## BPMN 流程引擎

| 技术点 | 章节 |
|--------|------|
| Activiti 8 + Spring Boot 3 | 第 12 章 |
| delegateExpression 绑定 | 第 12 章 |
| ActionDataBinder 反射绑定 | 第 12 章 |
| ConditionUtil 表达式求值 | 第 12 章 |
| support 变量分流 | 第 12 章 |
| ProcessStopFlag 三段式停止 | 第 13 章 |
| 动作守护 (前置/后置校验) | 第 13 章 |
| Spring @Scheduled 调度 | 第 13 章 |
| 手写 cron 解析 | 第 13 章 |
| 流程版本化部署 | 第 13 章 |

## 文字输入引擎

| 技术点 | 章节 |
|--------|------|
| TextTyperFactory 策略分派 | 第 18 章 |
| ADB_INPUT_TEXT 广播注入 | 第 18 章 |
| 九宫格键盘坐标识别链 | 第 19 章 |
| OCR 渐进式子串匹配 | 第 19 章 |
| VLM 大模型坐标兜底 | 第 19 章 |
| 空格键相邻行距推算 | 第 19 章 |
| KeyboardCoordCache 设备级缓存 | 第 19 章 |
| 拼音转换 + 多音字覆盖 | 第 19 章 |
| 分段输入策略 | 第 19 章 |

## 触控行为仿真引擎

| 技术点 | 章节 |
|--------|------|
| sendevent 内核级注入 | 第 14 章 |
| 完整触摸协议帧 | 第 14 章 |
| 指纹随机化 | 第 14 章 |
| 二进制事件编码 | 第 14 章 |
| input tap 降级 | 第 14 章 |
| getevent -lp 能力探测 | 第 15 章 |
| IDevice 适配器架构 | 第 15 章 |
| DeviceCacheManager 懒加载 | 第 15 章 |
| 幂函数曲线拟合 | 第 16 章 |
| 钟形偏移垂直轨迹 | 第 16 章 |
| 高斯时间拟合 | 第 16 章 |
| 动态帧数控制 | 第 16 章 |
| 录制脚本回放 | 第 16 章 |
| 应用启动仿真 | 第 17 章 |
| 返回手势仿真 | 第 17 章 |
| 模板匹配坐标随机化 | 第 17 章 |
| 不花屏工程 (1 次 fork) | 第 17 章 |
| 临时文件安全 (PID + trap) | 第 17 章 |

## 机器视觉与元素定位

| 技术点 | 章节 |
|--------|------|
| ScreenMatchUtil 统一入口 | 第 20 章 |
| OpenCV 模板匹配 | 第 20 章 |
| Tesseract4 OCR 渐进识别 | 第 20 章 |
| UiAutomator XML + XPath | 第 20 章 |
| 页面识别策略矩阵 | 第 20 章 |
| 匹配点坐标随机化 | 第 20 章 |

## 设备发现与并发治理

| 技术点 | 章节 |
|--------|------|
| jadb 轮询式设备发现 | 第 2 章 |
| ADB server 自愈 | 第 2 章 |
| DeviceCacheManager 统一缓存 | 第 2 章 |
| ThreadLocal 设备绑定 | 第 2 章 |
| invalidateOffline 批量清理 | 第 2 章 |

## 授权与安全体系

| 技术点 | 章节 |
|--------|------|
| HMAC 自包含机器码 | 第 21 章 |
| RSA-2048 授权码验签 | 第 21 章 |
| 三路冗余试用存储 | 第 21 章 |
| 云端心跳 (三态响应) | 第 21 章 |
| AuthState 反射防护 | 第 21 章 |
| LicenseFilter 三级拦截 | 第 21 章 |
| class-winter AES 加密 | 第 22 章 |
| jarsigner 签名校验 | 第 22 章 |
| 启动器令牌验证 | 第 22 章 |
| Agent 注入检测 | 第 22 章 |
| Named Pipe 密码传递 | 第 22 章 |
| HTTPS 证书固定 | 第 23 章 |
| HMAC 请求签名 | 第 23 章 |
| H2 冷备份 + 损坏恢复 | 第 24 章 |
| 演示模式引擎 | 第 24 章 |

---

> 本文是《adb-bot 技术内幕》系列附录 B。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 附录 A Selenium Web 自动化引擎](a-selenium-engine.md) ｜ [目录](../README.md) ｜ [附录 C 术语表 →](c-glossary.md)
