# Samsung Smart Card Region Fix

这是一个仅作用于三星智能卡进程的 LSPosed/Xposed 模块，用于国行 Galaxy Watch 与其他地区三星手机组合中的地区兼容问题。

## 适用范围

- 三星智能卡包名：`com.samsung.android.samsungpay.gear`
- 已验证版本：`5.1.46.25106`
- 模块版本：`1.0`
- 模块包名：`dev.smartcard.regionfix`

模块将目标应用读取到的 CSC 和国家代码改为中国大陆，并跳过该版本中错误的 Wear OS 中国版伴侣应用检查。所有 Hook 都有目标包保护，不会修改其他应用看到的系统属性。

## 安装

1. 安装 Release 中的 `SamsungSmartCardRegionFix-v1.0.apk`。
2. 在 LSPosed 中启用 **Samsung Smart Card Region Fix**。
3. 作用域只勾选 **三星智能卡**（`com.samsung.android.samsungpay.gear`）。
4. 强制停止三星智能卡，然后重新打开。

Release 同时提供已验证的 `SamsungSmartCard-5.1.46.25106.apk`。已有相同版本时无需重复安装。

## 回退

在 LSPosed 中停用本模块，强制停止三星智能卡并重新打开即可。也可以直接卸载模块。

## 构建

项目可用 Android Studio 或 Gradle 构建。仓库内的 `app/libs/api-82.jar` 只作为编译期 Xposed API，不会打包进模块 APK。

```text
compileSdk 36
minSdk 26
targetSdk 36
```

正式 Release 使用仓库外独立保存的签名密钥签名。
