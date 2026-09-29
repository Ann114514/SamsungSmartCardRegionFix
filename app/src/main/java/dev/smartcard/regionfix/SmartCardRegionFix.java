package dev.smartcard.regionfix;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class SmartCardRegionFix implements IXposedHookLoadPackage {
    private static final String SMART_CARD_PACKAGE = "com.samsung.android.samsungpay.gear";
    private static final String SAMSUNG_HEALTH_PACKAGE = "com.sec.android.app.shealth";
    private static final String STEP_SYNC_PACKAGE = "com.samsung.android.swsportplugin";
    private static final String SALES_CODE = "ro.csc.sales_code";
    private static final String COUNTRY_ISO = "ro.csc.countryiso_code";
    private static final String CHINA_ISO = "CN";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (SMART_CARD_PACKAGE.equals(lpparam.packageName)) {
            installSmartCardHooks(lpparam);
        } else if (SAMSUNG_HEALTH_PACKAGE.equals(lpparam.packageName)) {
            installSamsungHealthHooks(lpparam);
        }
    }

    private static void installSmartCardHooks(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            hookSystemProperties();
            hookStringResult(
                    lpparam.classLoader,
                    "com.samsung.android.samsungpay.gear.common.util.CountryISOSelector",
                    "getDeviceCountryISO",
                    CHINA_ISO);
            hookStringResult(
                    lpparam.classLoader,
                    "com.samsung.android.samsungpay.gear.wallet.network.walletenabler.WalletCountrySelector",
                    "getWalletCountry",
                    CHINA_ISO);
            hookStringResult(
                    lpparam.classLoader,
                    "com.samsung.android.samsungpay.gear.wallet.network.walletenabler.WalletEnablerRequester",
                    "getPhoneCountryISO$SamsungPayForGear_release",
                    CHINA_ISO);
            hookStringResult(
                    lpparam.classLoader,
                    "com.samsung.android.samsungpay.gear.wallet.network.walletenabler.WalletEnablerRequester$WatchInfoForEnable",
                    "getWatchCountry",
                    CHINA_ISO);
            hookCachedCountry();
            hookInvalidWearableCnCheck(lpparam.classLoader);
            XposedBridge.log("SmartCardRegionFix: hooks installed in " + lpparam.processName);
        } catch (Throwable error) {
            XposedBridge.log("SmartCardRegionFix: hook installation failed: " + error);
        }
    }

    private static void installSamsungHealthHooks(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> policyManager = XposedHelpers.findClassIfExists(
                    "com.samsung.android.service.health.sdkpolicy.SdkPolicyManager",
                    lpparam.classLoader);
            if (policyManager == null) {
                XposedBridge.log("SmartCardRegionFix: Samsung Health policy manager absent in "
                        + lpparam.processName);
                return;
            }

            XposedHelpers.findAndHookMethod(
                    policyManager,
                    "validateCallerSignature",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (STEP_SYNC_PACKAGE.equals(param.args[0])) {
                                param.setResult(null);
                                XposedBridge.log(
                                        "SmartCardRegionFix: allowed Samsung step sync policy access");
                            }
                        }
                    });
            XposedBridge.log("SmartCardRegionFix: Samsung Health policy hook installed in "
                    + lpparam.processName);
        } catch (Throwable error) {
            XposedBridge.log("SmartCardRegionFix: Samsung Health hook installation failed: "
                    + error);
        }
    }

    private static void hookSystemProperties() {
        Class<?> systemProperties = XposedHelpers.findClass("android.os.SystemProperties", null);
        XC_MethodHook propertyHook = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String key = (String) param.args[0];
                if (SALES_CODE.equals(key)) {
                    param.setResult("CHC");
                } else if (COUNTRY_ISO.equals(key)) {
                    param.setResult(CHINA_ISO);
                }
            }
        };

        XposedHelpers.findAndHookMethod(systemProperties, "get", String.class, propertyHook);
        XposedHelpers.findAndHookMethod(
                systemProperties, "get", String.class, String.class, propertyHook);
    }

    private static void hookCachedCountry() {
        Class<?> preferences = XposedHelpers.findClassIfExists(
                "android.app.SharedPreferencesImpl", null);
        if (preferences == null) {
            return;
        }

        XposedHelpers.findAndHookMethod(
                preferences,
                "getString",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        if ("cc2".equals(param.args[0])) {
                            param.setResult(CHINA_ISO);
                        }
                    }
                });
    }

    private static void hookInvalidWearableCnCheck(ClassLoader classLoader) {
        Class<?> baseActivity = XposedHelpers.findClassIfExists(
                "com.samsung.android.samsungpay.gear.ui.SpayBaseActivity", classLoader);
        if (baseActivity == null) {
            return;
        }

        XposedHelpers.findAndHookMethod(
                baseActivity,
                "checkIsWearableCNInstalled",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.setResult(null);
                    }
                });
    }

    private static void hookStringResult(
            ClassLoader classLoader, String className, String methodName, String value) {
        Class<?> targetClass = XposedHelpers.findClassIfExists(className, classLoader);
        if (targetClass == null) {
            return;
        }

        for (Method method : targetClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName()) && method.getReturnType() == String.class) {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(value);
                    }
                });
            }
        }
    }
}
