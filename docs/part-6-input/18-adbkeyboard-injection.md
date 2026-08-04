# 第 18 章 ADBKeyBoard 广播注入

> 用广播通道绕过键盘事件层，直接注入任意文字，无坐标依赖。

---

在 Android 上通过 ADB 输入文字，最原始的方式是 `input text`，但它不支持中文。要输入中文，通常得模拟键盘按键——但这需要知道键盘上每个按键的坐标，还要处理拼音选词逻辑，极其复杂。

adb-bot 的第一输入引擎换了一个思路：不模拟按键，直接把文字"塞"进输入框。

## 核心设计

### 广播注入原理

ADBKeyBoard 是一个特殊的输入法 APK，它监听一个自定义广播 action `ADB_INPUT_TEXT`。当收到广播时，直接把文本内容写入当前聚焦的输入框。

```
am broadcast -a ADB_INPUT_TEXT --es msg "你好世界"
```

这条命令绕过了键盘事件层，不需要模拟任何按键，也不依赖键盘布局。文字直接通过 InputMethodService 的 `commitText` 上屏。

### 为什么选广播而非 InputManager

| 方案 | 路径 | 中文支持 | 复杂度 |
|------|------|---------|--------|
| `input text` | InputManager | 不支持 | 低 |
| 模拟键盘按键 | sendevent | 支持（需坐标） | 极高 |
| 广播注入 | InputMethodService.commitText | 支持 | 低 |

广播注入的代价是需要在设备上安装 ADBKeyBoard 输入法并切换为当前输入法。adb-bot 的设备初始化流程会自动完成这两步。

### 策略模式自动切换

adb-bot 有两个输入引擎：ADBKeyBoard（广播注入）和 T9 九宫格（按键仿真）。引擎选择不是手动配置的，而是运行时自动检测当前输入法：

```
查询当前输入法 → 是 ADBKeyBoard? 
  → 是：用广播注入（快、全字符集）
  → 否：用 T9 九宫格仿真（慢、仿人、事件级真实）
```

这个判断每次输入时都执行，所以用户切换输入法后无需重启，下一秒就切换引擎。

### 全字符集

广播注入走的是 `commitText`，理论上支持 Android 能显示的任何字符：中文、英文、emoji、特殊符号、混合文本。这是它最大的优势。

### 清空输入的仿人设计

清空输入框不是简单的清空，而是模拟真人长按删除：先把光标移到末尾（`MOVE_END`），再用 `input keyevent --longpress` 连发 100 次 DEL 键。这个设计是因为部分 App 的输入框在 `commitText` 清空后会恢复内容，逐字删除则不会。

## 适用场景

| 场景 | 推荐引擎 | 原因 |
|------|---------|------|
| 搜索框输入 | ADBKeyBoard | 快速、无防检测需求 |
| 表单填写 | ADBKeyBoard | 字符种类多 |
| 评论 / 话术 | T9 九宫格 | 需要事件级真实 |
| emoji / 特殊符号 | ADBKeyBoard | T9 不支持 |

## 关键代码

| 方法 | 说明 |
|------|------|
| `TextTyperFactory.type()` | 策略入口，按 IME 自动分派 |
| `AdbKeyboardTyper.type()` | 广播注入实现 |
| `AdbCmdUtil.sendMessage()` | `am broadcast ADB_INPUT_TEXT` |
| `AdbCmdUtil.clearInputText()` | MOVE_END + DEL 连发清空 |
| `AdbCmdUtil.setIme()` | 输入法切换 |

---

> 本文是《adb-bot 技术内幕》系列第 18 章。
> 更多内容见 [GitHub 仓库](../../README.md) | [官网](https://adb-bot.hilbp.com)
>
> [← 第 17 章 行为级仿真与工程优化](../part-5-simulation/17-behavior-simulation.md) ｜ [目录](../README.md) ｜ [第 19 章 T9 九宫格拼音仿真 →](19-t9-pinyin-simulation.md)
