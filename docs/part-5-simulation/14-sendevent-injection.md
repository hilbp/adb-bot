# 第 14 章 sendevent 点击注入

> 绕过 InputManager Java 层，直写 /dev/input/eventN，在内核子系统注入完整触摸事件。

---

Android 的 `input tap` 命令走的是 `InputManager` Java 层注入路径。这条路径产生的触摸事件有明显特征：单事件、无 track id、无 BTN_TOOL_FINGER 标记、瞬时无面积。主流风控 SDK 只要检测这几个维度中的任何一个，就能判定为机器点击。

adb-bot 需要一个更底层的注入方案，让事件流和真人触摸在内核层面无差异。

## 核心设计

```mermaid
flowchart TD
    A[点击请求] --> B{root?}
    B -- 否 --> C[input tap 降级]
    B -- 是 --> D{设备已适配?}
    D -- 否 --> E[getevent 自动探测]
    E --> F[缓存 DeviceProfile]
    D -- 是 --> F
    F --> G[生成 sendevent 脚本]
    G --> H[二进制编码 + cat 单次写入]
    H --> I[/dev/input/eventN]
```

### 注入层级的选择

Linux input 子系统分四层：应用层 → InputManager → evdev 驱动 → 硬件中断。`input tap` 在第二层，`sendevent` 直写第三层（evdev 的 `/dev/input/eventN` 设备节点）。

这带来一个本质差异：InputManager 注入的事件会被打上 `FLAG_VIRTUAL_HARDWARE_KEY` 标记，部分风控 SDK 能检测到；而 sendevent 写入的事件和硬件中断产生的事件在 evdev 层面完全一致。

### 完整触摸协议帧

真人触摸一次屏幕，内核会收到一组完整事件：

| 事件 | 含义 | 随机化 |
|------|------|--------|
| ABS_MT_TRACKING_ID (0x35) | 触点 track id | 1~65534 随机 |
| BTN_TOOL_FINGER (0x325) | 手指接触标记 | 有/无（按设备能力） |
| ABS_MT_POSITION_X/Y (0x35/0x36) | 坐标 | ×scale 缩放 |
| ABS_MT_TOUCH_MAJOR (0x30) | 触摸面积 | 5 或 6 随机 |
| SYN_REPORT (0x0) | 帧同步 | — |

track id 是关键：`input tap` 不带 track id，风控检测"无 track id 的触摸事件"即可判定。我们每次点击随机生成 track id，让事件流看起来来自不同的物理触点。

### 二进制编码：从 50 次 fork 到 1 次

sendevent 命令逐条执行，每次产生一个 shell 进程。一次滑动需要 50~100 条事件，意味着 50~100 次 fork。这在 scrcpy 投屏同时运行时会严重抢占 CPU，导致画面花屏。

解决方案是把事件列表编码为 `input_event` 结构体（24 字节/事件，小端序），Base64 编码后用 `echo ... | base64 -d | cat > /dev/input/eventN` 一次性写入。50 次压到 1 次 fork。

### 降级策略

| 场景 | 降级到 | 触发条件 |
|------|--------|---------|
| 模拟器 | `input tap` | `ro.kernel.qemu=1` |
| 非 root 设备 | `input tap` | `su` 不可用 |
| root 但未适配 | `input tap` | getevent 探测失败 |

降级是自动的，上层调用方无需感知。但输入事件会暴露，模拟器和非 root 场景下防检测能力有限。

## 关键代码

| 方法 | 说明 |
|------|------|
| `AdbCmdUtil.clickBySendEvent()` | 点击入口，root 判断 + 降级分派 |
| `AutoDetectDevice.click()` | sendevent 协议帧生成（含随机化） |
| `AdbCmdUtil.suShell()` | su 包装 + 二进制编码选择 |
| `AdbCmdUtil.eventsToBase64()` | 事件列表 → 24B 结构体 → Base64 |

---

> 本文是《adb-bot 技术内幕》系列第 14 章。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 第 13 章 生命周期与调度](../part-4-process/13-lifecycle-and-schedule.md) ｜ [目录](../README.md) ｜ [第 15 章 设备自动适配 →](15-device-autodetect.md)
