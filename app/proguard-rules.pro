# bughd
-keepattributes Exceptions, Signature, LineNumberTable

# retrolambda
-dontwarn java.lang.invoke.*

# rx
-dontwarn rx.**
-keepclassmembers class rx.** { *; }

# support library
-keep class android.support.v7.widget.StaggeredGridLayoutManager { *; }
-keep class ooo.oxo.mr.widget.InsetsScrollingViewBehavior { *; }

# data binding
-keep class ooo.oxo.mr.binding.**
-keep class ooo.oxo.mr.databinding.** { *; }

# glide
-keep class ooo.oxo.mr.net.AVFileGlideModule { *; }

# okhttp
-dontwarn okio.**

# leancloud
-dontwarn okio.**
-dontwarn com.alibaba.fastjson.**
-dontwarn com.avos.**
-keep class com.avos.** { *;}
