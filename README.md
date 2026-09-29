# Samsung Region Compatibility

这是一个面向三星跨地区设备组合的 LSPosed/Xposed 兼容模块。v3 已包含三星智能卡、三星健康步数同步插件和三星相册云同步的兼容修复，后续版本会继续按目标应用增加外版适配。

## 适用范围

- 三星智能卡：`com.samsung.android.samsungpay.gear`，已验证 `5.1.46.25106`
- 三星健康：`com.sec.android.app.shealth`，已验证 `7.00.6.011`
- 三星健康步数同步插件：`com.samsung.android.swsportplugin`，已验证 `2.0.00.37`
- 三星相册云同步：`com.samsung.android.agent.storage`，已验证 `1.6.09.31`
- 模块版本：`v3`
- 模块包名：`dev.smartcard.regionfix`

三星智能卡进程内会读取到中国大陆 CSC 和国家代码，并跳过错误的 Wear OS 中国版伴侣应用检查。三星健康进程内只对步数同步插件放行 SDK 签名策略。三星相册云同步进程内会通过 Google Media Sync SDK 的系统应用检查，并兼容授权网页完成后的显示回调。所有 Hook 都限制在目标进程内。

## 安装

1. 安装 Release 中的 `SamsungRegionCompatibility-v3.apk`。
2. 在 LSPosed 中启用 **Samsung Region Compatibility**。
3. 作用域勾选需要适配的应用：三星智能卡、三星健康、三星相册云同步。
4. 强制停止对应应用后重新打开。

Release 同时提供已验证的三星智能卡、三星健康步数同步插件和三星相册云同步安装包。步数同步插件本身无需加入 LSPosed 作用域；三星相册云同步无需放入 `priv-app`。

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

## 打赏

感谢老板们的使用，如果觉得做得不错可以点个 Star，也可请我杯奶茶。欢迎提出意见、建议或反馈 Bug，您的支持是我最大的动力。

<p align="center">
  <img src="docs/donate/wechat-pay.jpg" alt="微信支付打赏码" width="360">
</p>
