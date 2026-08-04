# 第 4 章 前端解码与追帧策略

> JMuxer 软解码 H.264、延迟自动跳帧、会话复用、批量并行启动。

---

后端推过来的 H.264 裸流需要在前端解码播放。浏览器可以用 WebCodecs API 硬解，但兼容性参差不齐。adb-bot 选择 JMuxer——一个轻量的纯 JavaScript H.264 软解码器，通过 MSE (Media Source Extensions) 喂给 video 元素。

## 核心设计

### JMuxer 解码

```javascript
const muxer = new JMuxer({
    node: videoElement,
    mode: 'video',
    fps: 30,
    clearBuffer: true
});
// WebSocket 收到 H.264 帧后直接 feed
ws.onmessage = (e) => muxer.feed({ data: new Uint8Array(e.data) });
```

JMuxer 接收原始 H.264 NALU，解析 SPS/PPS 初始化解码器，通过 MSE 的 SourceBuffer 播放。`clearBuffer: true` 自动清理已播放的旧 buffer，防止内存增长。

### 追帧策略

投屏场景的核心体验指标是延迟。但网络抖动不可避免：某一刻网络慢了，buffer 里积压了几秒的视频，如果不处理，用户看到的画面就越来越滞后。

```javascript
video.addEventListener('timeupdate', () => {
    const buffered = video.buffered;
    if (buffered.length > 0) {
        const lag = buffered.end(0) - video.currentTime;
        if (lag > 1.5) {
            video.currentTime = buffered.end(0) - 0.1;
        }
    }
});
```

当缓冲区末尾与当前播放位置的差值超过 1.5 秒时，直接跳到最新位置。用户感知到的是偶尔的轻微跳帧，而不是持续的延迟累积。

### 会话管理

| 场景 | 处理 |
|------|------|
| 首次打开 | 启动 scrcpy + 建立 WebSocket |
| 多标签页同设备 | 复用 scrcpy，新建 WebSocket |
| 关闭最后一个标签页 | 停止 reader 线程，kill scrcpy |
| 启动时残留 | 扫描 `adb forward --list` 清理 scrcpy 残留规则 |

批量并行启动用 `CompletableFuture`，已运行的设备跳过。

### screenrecord 降级

scrcpy 不可用时（旧设备、特殊 ROM），降级到 `screenrecord`：每 180 秒自动重启（screenrecord 时限），重启前滑动屏幕强制产生 I 帧（防黑屏）。体验差于 scrcpy，但保证基本可用。

## 关键代码

| 文件/方法 | 说明 |
|----------|------|
| `mirror.js` | JMuxer 初始化 + WebSocket 订阅 + 追帧 |
| `ScreenMirrorSession.isRunning()` | 会话复用判断 |
| `VideoWebSocketConfig` | WebSocket 端点 `/ws/video/{serial}` |

---

> 本文是《adb-bot 技术内幕》系列第 4 章。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 第 3 章 scrcpy 视频流链路](03-scrcpy-pipeline.md) ｜ [目录](../README.md) ｜ [第 5 章 操控交互转发 →](05-input-forwarding.md)
