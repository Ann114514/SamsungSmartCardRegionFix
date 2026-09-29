# Samsung Region Compatibility

这是一个面向三星跨地区设备组合的 LSPosed/Xposed 兼容模块。v2 已包含三星智能卡区域适配和三星健康步数同步插件兼容修复，后续版本会继续按目标应用增加外版适配。

## 适用范围

- 三星智能卡：`com.samsung.android.samsungpay.gear`，已验证 `5.1.46.25106`
- 三星健康：`com.sec.android.app.shealth`，已验证 `7.00.6.011`
- 三星健康步数同步插件：`com.samsung.android.swsportplugin`，已验证 `2.0.00.37`
- 模块版本：`v2`
- 模块包名：`dev.smartcard.regionfix`

三星智能卡进程内会读取到中国大陆 CSC 和国家代码，并跳过错误的 Wear OS 中国版伴侣应用检查。三星健康进程内只对步数同步插件放行 SDK 签名策略。所有 Hook 都有目标包和调用方保护。

## 安装

1. 安装 Release 中的 `SamsungRegionCompatibility-v2.apk`。
2. 在 LSPosed 中启用 **Samsung Region Compatibility**。
3. 作用域勾选三星智能卡和三星健康。
4. 强制停止对应应用后重新打开。

Release 同时提供已验证的三星智能卡和三星健康步数同步插件安装包。步数同步插件本身无需加入 LSPosed 作用域。

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
