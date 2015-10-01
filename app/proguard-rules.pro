# okhttp
-dontwarn okio.**

# retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# rx
-dontwarn rx.**
-keepclassmembers class rx.** { *; }

# support library
-keep class android.support.v7.widget.LinearLayoutManager { *; }
-keep class android.support.v7.widget.StaggeredGridLayoutManager { *; }
-keep class android.support.design.widget.AppBarLayout$ScrollingViewBehavior { *; }

# data binding
-keep class ooo.oxo.mr.databinding.** { *; }

# glide
-keep public class * extends com.bumptech.glide.module.GlideModule { *; }

# all in all
-keepnames class * { *; }
