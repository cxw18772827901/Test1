# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/chenxiaowu/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#基本组件
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class **.R$* { *;
}
# Keep native methods
#-dontwarn com.hgd.hgdcomic.**
#-keep class com.hgd.hgdcomic.** {*;}
-dontwarn com.hgd.hgdcomic.model.**
-keep class com.hgd.hgdcomic.model.** {*;}
-dontwarn com.hgd.hgdcomic.db.bean.**
-keep class com.hgd.hgdcomic.db.bean.** {*;}
-keepclassmembers class * {
    native <methods>;
}
#-dontwarn android.support.**
#-keep class android.support.** {*;}
#反射
-keepattributes Signature
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keep public class com.google.gson.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.google.gson.**
-dontwarn sun.misc.Unsafe
-dontwarn com.google.gson.examples.android.model.**
#apache
-keep class org.apache.http.** {*;}
-keep class android.net.** { *; }
-keep class com.android.internal.http.multipart.** { *; }
-keep class org.apache.** { *; }
-keep class org.apache.commons.codec.binary.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.**
-dontwarn org.apache.commons.codec.binary.**
#volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
#systenbar
-keep class com.readystatesoftware.systembartint.** { *; }
-dontwarn com.readystatesoftware.systembartint.**
#glide
-keep class com.bumptech.gile.** {*;}
-dontwarn com.bumptech.gile.**
#gif
-keep class pl.droidsonroids.gif.** {*;}
-dontwarn pl.droidsonroids.gif.**
#eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#支付混淆
-keep class cn.trinea.android.common.** {*;}
-dontwarn cn.trinea.android.common.**

-keep class com.example.livepay.** {*;}
-dontwarn com.example.livepay.**
#友盟混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#QQ混淆
-keep class com.tencent.** { *; }
-dontwarn com.tencent.**
#微信
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
#xutil
-keep class com.lidroid.xutils.**{*;}
-dontwarn com.lidroid.xutils.**
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#自定义注解
#-keep @com.hgd.hgdcomic.util.inject.ViewInject class * {*;}
-keep,allowobfuscation @interface  com.hgd.hgdcomic.util.inject.ViewInject
#-keep class * {
#    @com.hgd.hgdcomic.util.inject.ViewInject <fields>;
#}
-keepclassmembers class * {
    @com.hgd.hgdcomic.util.inject.ViewInject *;
}

#-keep @com.hgd.hgdcomic.util.inject.ViewInjectClick class * {*;}
-keep,allowobfuscation @interface  com.hgd.hgdcomic.util.inject.ViewInjectClick
#-keep class * {
#    @com.hgd.hgdcomic.util.inject.ViewInjectClick <fields>;
#}
-keepclassmembers class * {
    @com.hgd.hgdcomic.util.inject.ViewInjectClick *;
}

#广告
-keep class com.ly.adpoymer.**{ *; }
-keep class com.qq.e.** {
    public protected *;
}
-dontwarn  org.apache.**

-keep class android.support.v4.app.NotificationCompat**{
    public *;
}
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }

-keep class com.deceive.** { *; }
-keep class com.discuss.** { *; }
# Demo工程里用到了AQuery库，因此需要添加下面的配置
# 请开发者根据自己实际情况给第三方库的添加相应的混淆设置
-dontwarn com.androidquery.**
-keep class com.androidquery.** { *;}

-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.androidquery.callback.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}

-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}


-dontwarn com.bytedance.article.common.nativecrash.**
-keep class com.bytedance.article.common.nativecrash.** { *;}
#高磊混淆
-dontwarn com.ib.mob.**
-keep class com.ib.mob.** { *; }
#七牛上传
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings



-keep class com.ly.adpoymer.**{ *; }
    -keep class com.qq.e.** {
        public protected *;
    }
    -dontwarn  org.apache.**

   -keep class android.support.v4.app.NotificationCompat**{
        public *;
    }
   -keep class com.baidu.mobads.** { *; }
   -keep class com.baidu.mobad.** { *; }

  -keep class com.deceive.** { *; }
  -keep class com.discuss.** { *; }

   # Demo工程里用到了AQuery库，因此需要添加下面的配置
   # 请开发者根据自己实际情况给第三方库的添加相应的混淆设置
  -dontwarn com.androidquery.**
  -keep class com.androidquery.** { *;}

  -keep class com.bytedance.sdk.openadsdk.** { *; }
  -keep class com.androidquery.callback.** {*;}
  -keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
  -keep class android.support.v4.**{
      public *;
   }
  -keep class android.support.v7.**{
      public *;
   }

