# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Google Generative AI
-keep class com.google.ai.client.generativeai.** { *; }
-keep class com.google.firebase.vertexai.** { *; }

# Models
-keep class com.example.aura.domain.model.** { *; }
-keep class com.example.aura.data.local.entity.** { *; }
