package com.yuk.miuihome.view.utils.ktx

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.app.UiAutomation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.*
import android.view.KeyEvent
import android.view.MotionEvent
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.view.utils.Config
import com.yuk.miuihome.view.utils.HomeContext
import com.yuk.miuihome.view.utils.LogUtil
import java.lang.reflect.*

object ActivityHelper {

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun initSubActivity() {
        try {
            val cActivityThread = Class.forName("android.app.ActivityThread")
            val fCurrentActivityThread = cActivityThread.getStaticFiledByClass("sCurrentActivityThread")
            val sCurrentActivityThread = fCurrentActivityThread.get(null)!!
            val fmInstrumentation = cActivityThread.getFieldByClassOrObject("mInstrumentation")
            val mGetInstrumentation = cActivityThread.getDeclaredMethod("getInstrumentation")
            val mInstrumentation = mGetInstrumentation.invoke(sCurrentActivityThread)!! as Instrumentation
            fmInstrumentation.set(sCurrentActivityThread, MyInstrumentation(mInstrumentation))

            var cActivityManager: Class<*>
            var fgDefault: Field
            try {
                cActivityManager = Class.forName("android.app.ActivityManagerNative")
                fgDefault = cActivityManager.getStaticFiledByClass("gDefault")
            } catch (e1: Exception) {
                try {
                    cActivityManager = Class.forName("android.app.ActivityManager")
                    fgDefault = cActivityManager.getStaticFiledByClass("IActivityManagerSingleton")
                } catch (e2: Exception) {
                    LogUtil.e(e1)
                    LogUtil.e(e2)
                    return
                }
            }

            val fmH = cActivityThread.getFieldByClassOrObject("mH")
            val originHandler = fmH.get(sCurrentActivityThread) as Handler
            val fHandlerCallback = Handler::class.java.getFieldByClassOrObject("mCallback")
            val currHCallback = fHandlerCallback.get(originHandler) as Handler.Callback?
            if (currHCallback == null || currHCallback::class.java.name != MyHandler::class.java.name) {
                fHandlerCallback.set(originHandler, MyHandler(currHCallback))
            }

            val gDefault = fgDefault.get(null)
            val cSingleton = Class.forName("android.util.Singleton")
            val fmInstance = cSingleton.getFieldByClassOrObject("mInstance")
            val mInstance = fmInstance.get(gDefault)
            val proxy = Proxy.newProxyInstance(XposedInit::class.java.classLoader, arrayOf(Class.forName("android.app.IActivityManager")), IActivityManagerHandler(mInstance!!))
            fmInstance.set(gDefault, proxy)
            try {
                val cActivityTaskManager = Class.forName("android.app.ActivityTaskManager")
                val singleton = cActivityTaskManager.getStaticObjectField("IActivityTaskManagerSingleton")
                cSingleton.getMethod("get").invoke(singleton)
                val mDefaultTaskMgr = fmInstance.get(singleton)
                val proxy2 = Proxy.newProxyInstance(XposedInit::class.java.classLoader, arrayOf(Class.forName("android.app.IActivityTaskManager")), IActivityManagerHandler(mDefaultTaskMgr!!))
                fmInstance.set(singleton, proxy2)
            } catch (ignored: Exception) {
            }
        } catch (e: Exception) {
            LogUtil.e(e)
        }
    }


}

class MyInstrumentation(private val mBase: Instrumentation) : Instrumentation() {

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        try {
            return mBase.newActivity(cl, className, intent)
        } catch (e: Exception) {
            if (className!!.startsWith(Config.packageName)) {
                return ActivityHelper::class.java.classLoader!!.loadClass(className).newInstance() as Activity
            }
            throw e
        }
    }

    override fun onCreate(arguments: Bundle?) {
        mBase.onCreate(arguments)
    }

    override fun start() {
        mBase.start()
    }

    override fun onStart() {
        mBase.onStart()
    }

    override fun onException(obj: Any?, e: Throwable?): Boolean {
        return mBase.onException(obj, e)
    }

    override fun sendStatus(resultCode: Int, results: Bundle?) {
        mBase.sendStatus(resultCode, results)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun addResults(results: Bundle?) {
        mBase.addResults(results)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        mBase.finish(resultCode, results)
    }

    override fun setAutomaticPerformanceSnapshots() {
        mBase.setAutomaticPerformanceSnapshots()
    }

    override fun startPerformanceSnapshot() {
        mBase.startPerformanceSnapshot()
    }

    override fun endPerformanceSnapshot() {
        mBase.endPerformanceSnapshot()
    }

    override fun onDestroy() {
        mBase.onDestroy()
    }

    override fun getContext(): Context {
        return mBase.context
    }

    override fun getComponentName(): ComponentName {
        return mBase.componentName
    }

    override fun getTargetContext(): Context {
        return mBase.targetContext
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getProcessName(): String {
        return mBase.processName
    }

    override fun isProfiling(): Boolean {
        return mBase.isProfiling
    }

    override fun startProfiling() {
        mBase.startProfiling()
    }

    override fun stopProfiling() {
        mBase.stopProfiling()
    }

    override fun setInTouchMode(inTouch: Boolean) {
        mBase.setInTouchMode(inTouch)
    }

    override fun waitForIdle(recipient: Runnable?) {
        mBase.waitForIdle(recipient)
    }

    override fun waitForIdleSync() {
        mBase.waitForIdleSync()
    }

    override fun runOnMainSync(runner: Runnable?) {
        mBase.runOnMainSync(runner)
    }

    override fun startActivitySync(intent: Intent?): Activity {
        return mBase.startActivitySync(intent)
    }

    @TargetApi(Build.VERSION_CODES.P)
    override fun startActivitySync(intent: Intent, options: Bundle?): Activity {
        return mBase.startActivitySync(intent, options)
    }

    override fun addMonitor(monitor: ActivityMonitor?) {
        mBase.addMonitor(monitor)
    }

    override fun addMonitor(
        cls: String?,
        result: ActivityResult?,
        block: Boolean
    ): ActivityMonitor {
        return mBase.addMonitor(cls, result, block)
    }

    override fun addMonitor(
        filter: IntentFilter?,
        result: ActivityResult?,
        block: Boolean
    ): ActivityMonitor {
        return mBase.addMonitor(filter, result, block)
    }

    override fun checkMonitorHit(monitor: ActivityMonitor?, minHits: Int): Boolean {
        return mBase.checkMonitorHit(monitor, minHits)
    }

    override fun waitForMonitor(monitor: ActivityMonitor?): Activity {
        return mBase.waitForMonitor(monitor)
    }

    override fun waitForMonitorWithTimeout(monitor: ActivityMonitor?, timeOut: Long): Activity {
        return mBase.waitForMonitorWithTimeout(monitor, timeOut)
    }

    override fun removeMonitor(monitor: ActivityMonitor?) {
        mBase.removeMonitor(monitor)
    }

    override fun invokeContextMenuAction(
        targetActivity: Activity?,
        id: Int,
        flag: Int
    ): Boolean {
        return mBase.invokeContextMenuAction(targetActivity, id, flag)
    }

    override fun invokeMenuActionSync(targetActivity: Activity?, id: Int, flag: Int): Boolean {
        return mBase.invokeMenuActionSync(targetActivity, id, flag)
    }

    override fun sendCharacterSync(keyCode: Int) {
        mBase.sendCharacterSync(keyCode)
    }

    override fun sendKeyDownUpSync(key: Int) {
        mBase.sendKeyDownUpSync(key)
    }

    override fun sendKeySync(event: KeyEvent?) {
        mBase.sendKeySync(event)
    }

    override fun sendPointerSync(event: MotionEvent?) {
        mBase.sendPointerSync(event)
    }

    override fun sendStringSync(text: String?) {
        mBase.sendStringSync(text)
    }

    override fun sendTrackballEventSync(event: MotionEvent?) {
        mBase.sendTrackballEventSync(event)
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return mBase.newApplication(cl, className, context)
    }

    override fun callApplicationOnCreate(app: Application?) {
        mBase.callApplicationOnCreate(app)
    }

    override fun newActivity(
        clazz: Class<*>?,
        context: Context?,
        token: IBinder?,
        application: Application?,
        intent: Intent?,
        info: ActivityInfo?,
        title: CharSequence?,
        parent: Activity?,
        id: String?,
        lastNonConfigurationInstance: Any?
    ): Activity {
        return mBase.newActivity(
            clazz,
            context,
            token,
            application,
            intent,
            info,
            title,
            parent,
            id,
            lastNonConfigurationInstance
        )
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun inject(
        activity: Activity,
        icicle: Bundle?
    ) {
        val clzName = activity.javaClass.name
        if (icicle != null && clzName.startsWith(Config.packageName)) {
            icicle.classLoader = XposedInit::class.java.classLoader
        }
        if (clzName.startsWith(Config.packageName)) {
            val assets = activity.resources.assets
            val method = assets.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
            method.invoke(assets, XposedInit.modulePath)
        }
    }

    override fun callActivityOnCreate(
        activity: Activity,
        icicle: Bundle?,
        persistentState: PersistableBundle?
    ) {
        inject(activity, icicle)
        mBase.callActivityOnCreate(activity, icicle, persistentState)
    }

    override fun callActivityOnCreate(activity: Activity, icicle: Bundle?) {
        inject(activity, icicle)
        mBase.callActivityOnCreate(activity, icicle)
    }

    override fun callActivityOnDestroy(activity: Activity?) {
        mBase.callActivityOnDestroy(activity)
    }

    override fun callActivityOnRestoreInstanceState(
        activity: Activity,
        savedInstanceState: Bundle
    ) {
        mBase.callActivityOnRestoreInstanceState(activity, savedInstanceState)
    }

    override fun callActivityOnRestoreInstanceState(
        activity: Activity,
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        mBase.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState)
    }

    override fun callActivityOnPostCreate(activity: Activity, savedInstanceState: Bundle?) {
        mBase.callActivityOnPostCreate(activity, savedInstanceState)
    }

    override fun callActivityOnPostCreate(
        activity: Activity,
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        mBase.callActivityOnPostCreate(activity, savedInstanceState, persistentState)
    }

    override fun callActivityOnNewIntent(activity: Activity?, intent: Intent?) {
        mBase.callActivityOnNewIntent(activity, intent)
    }

    override fun callActivityOnStart(activity: Activity?) {
        mBase.callActivityOnStart(activity)
    }

    override fun callActivityOnRestart(activity: Activity?) {
        mBase.callActivityOnRestart(activity)
    }

    override fun callActivityOnPause(activity: Activity?) {
        mBase.callActivityOnPause(activity)
    }

    override fun callActivityOnResume(activity: Activity?) {
        mBase.callActivityOnResume(activity)
    }

    override fun callActivityOnStop(activity: Activity?) {
        mBase.callActivityOnStop(activity)
    }

    override fun callActivityOnUserLeaving(activity: Activity?) {
        mBase.callActivityOnUserLeaving(activity)
    }

    override fun callActivityOnSaveInstanceState(activity: Activity, outState: Bundle) {
        mBase.callActivityOnSaveInstanceState(activity, outState)
    }

    override fun callActivityOnSaveInstanceState(
        activity: Activity,
        outState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        mBase.callActivityOnSaveInstanceState(activity, outState, outPersistentState)
    }

    @TargetApi(Build.VERSION_CODES.R)
    override fun callActivityOnPictureInPictureRequested(activity: Activity) {
        mBase.callActivityOnPictureInPictureRequested(activity)
    }

    @Suppress("DEPRECATION")
    override fun startAllocCounting() {
        mBase.startAllocCounting()
    }

    @Suppress("DEPRECATION")
    override fun stopAllocCounting() {
        mBase.stopAllocCounting()
    }

    override fun getAllocCounts(): Bundle {
        return mBase.allocCounts
    }

    override fun getBinderCounts(): Bundle {
        return mBase.binderCounts
    }

    override fun getUiAutomation(): UiAutomation {
        return mBase.uiAutomation
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun getUiAutomation(flags: Int): UiAutomation {
        return mBase.getUiAutomation(flags)
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun acquireLooperManager(looper: Looper?): TestLooperManager {
        return mBase.acquireLooperManager(looper)
    }
}

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
class MyHandler(private val mDefault: Handler.Callback?) : Handler.Callback {
    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            100 -> {
                try {
                    val record = msg.obj
                    val fIntent = record::class.java.getFieldByClassOrObject("intent")
                    val intent = fIntent.get(record)!! as Intent
                    //获取bundle
                    var bundle: Bundle? = null
                    try {
                        val fExtras = Intent::class.java.getFieldByClassOrObject("mExtras")
                        bundle = fExtras.get(intent) as Bundle?
                    } catch (e: Exception) {
                        LogUtil.e(e)
                    }
                    //设置
                    bundle?.let {
                        it.classLoader = HomeContext.context.classLoader
                        if (intent.hasExtra("com_yuk_miuihome_intent_proxy")) {
                            val rIntent = intent.getParcelableExtra<Intent>("com_yuk_miuihome_intent_proxy")
                            fIntent.set(record, rIntent)
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.e(e)
                }
            }
            159 -> {
                val clientTranslation = msg.obj
                try {
                    clientTranslation?.let { cTrans ->
                        //获取列表
                        val mGetCallbacks = Class.forName("android.app.servertransaction.ClientTransaction").getDeclaredMethod("getCallbacks")
                        mGetCallbacks.isAccessible = true
                        val cTransItems = mGetCallbacks.invoke(cTrans) as List<*>?
                        if (!cTransItems.isNullOrEmpty()) {
                            for (item in cTransItems) {
                                val clz = item?.javaClass
                                if (clz?.name?.contains("LaunchActivityItem") == true) {
                                    val fmIntent = clz.getFieldByClassOrObject("mIntent")
                                    val wrapper = fmIntent.get(item) as Intent
                                    //获取Bundle
                                    var bundle: Bundle? = null
                                    try {
                                        val fExtras = Intent::class.java.getFieldByClassOrObject("mExtras")
                                        bundle = fExtras.get(wrapper) as Bundle?
                                    } catch (e: Exception) {
                                        LogUtil.e(e)
                                    }
                                    //设置
                                    bundle?.let {
                                        it.classLoader = HomeContext.classLoader
                                        if (wrapper.hasExtra("com_yuk_miuihome_intent_proxy")) {
                                            val rIntent = wrapper.getParcelableExtra<Intent>("com_yuk_miuihome_intent_proxy")
                                            fmIntent.set(item, rIntent)
                                            if (Build.VERSION.SDK_INT >= 31) {
                                                val cActivityThread = Class.forName("android.app.ActivityThread")
                                                val currentActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread")
                                                currentActivityThread.isAccessible = true
                                                val activityThread = currentActivityThread.invoke(null)
                                                val acr = activityThread.javaClass.getMethod("getLaunchingActivity", IBinder::class.java).invoke(activityThread, cTrans.javaClass.getMethod("getActivityToken").invoke(cTrans))
                                                if (acr != null) {
                                                    val fAcrIntent = acr.javaClass.getDeclaredField("intent")
                                                    fAcrIntent.isAccessible = true
                                                    fAcrIntent[acr] = rIntent
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.e(e)
                }
            }
        }
        return mDefault?.handleMessage(msg) ?: false
    }
}

class IActivityManagerHandler(private val mOrigin: Any) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any? {
        if ("startActivity" == method!!.name) {
            var index = -1
            args?.let {
                for (i in it.indices) {
                    if (args[i] is Intent) {
                        index = i
                        break
                    }
                }
            }
            if (index != -1) {
                args?.let {
                    val raw = it[index] as Intent
                    val component = raw.component
                    if (component != null &&
                        HomeContext.context.packageName == component.packageName &&
                        component.className.startsWith(Config.packageName)
                    ) {
                        val wrapper = Intent()
                        wrapper.setClassName(component.packageName, "com.miui.home.settings.DefaultHomeSettings")
                        wrapper.putExtra("com_yuk_miuihome_intent_proxy", raw)
                        it[index] = wrapper
                    }
                }
            }
        }
        try {
            return if (args != null) method.invoke(mOrigin, *args) else method.invoke(mOrigin)
        } catch (e: InvocationTargetException) {
            throw e.targetException
        }
    }
}