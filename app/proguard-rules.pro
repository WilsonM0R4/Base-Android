# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lisachui/Library/Android/sdk/tools/proguard/proguard-android.txt
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



#com.squareup.otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

#com.google.guava:guava
#-injars path/to/myapplication.jar
#-injars lib/guava-r07.jar
#-libraryjars lib/jsr305.jar
#-outjars myapplication-dist.jar

-dontoptimize
-dontobfuscate
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

#com.mcxiaoke.volley:library
-keepclasseswithmembernames  interface com.android.volley.** { *; }
-keep class org.json.** {*;}
-keep class android.accounts.**{*;}
-keep class java.io.**{*;}
-keep class android.net.**{*;}
-keep class org.apache.http.**{*;}
-keep class android.net.http.AndroidHttpClient{*;}
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
-keep class org.apache.commons.logging.**

-dontwarn javax.management.**
-dontwarn java.lang.management.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.slf4j.**
-dontwarn org.json.**
-dontwarn org.apache.http.**


#gms
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
-keep interface android.support.v7.** { *; }
-keep class com.google.** { *; }
-keep class com.facebook.** { *; }
-keep class org.apache.** { *; }

-dontwarn com.google.**

#com.google.common.collect
-keep class com.google.common.** {*;}
-keep class com.google.common.collect.** {*;}

-keep class org.apache.http.params.** {*;}
-dontwarn javax.annotation.Nullable

#retrofit
-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit.** { *; }
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#samsung
-dontwarn com.samsung.**

-keep class com.samsung.** {*;}

#okio
-dontwarn com.squareup.okhttp.internal.huc.**
-dontwarn retrofit.appengine.UrlFetchClient
-dontwarn rx.**
-dontwarn okio.**

#webview
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

#xmlpull
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry