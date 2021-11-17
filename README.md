# Steuerbot Android SDK

## Integrate the SDK in your project

### 1) Add maven dependency

Use maven url in `settings.gradle`. For example it can look like this:

```groovy
import org.gradle.api.initialization.resolve.RepositoriesMode
...
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/steuerbot/android-sdk")
            credentials {
                username = "friedolinfoerder"
                // Safe to share the password since it is a `read:package` scoped token.
                password = "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"
            }
        }
        // add these next two lines for subdependencies
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
...
```

### 2) Add steuerbot to your dependencies

In your main project `build.gradle` at the steuerbot dependency. For example in `app/build.gradle`:

```groovy
dependencies {
    ...
    implementation 'com.steuerbot.sdk:sdk:0.0.1'
    ...
}
```

### 3) Extend your application

For the sdk to resolve all dependencies, you have to add this block to the `android` section of your application gradle module:

```groovy
android {
    ...
    packagingOptions {
        pickFirst 'lib/x86/libc++_shared.so'
        pickFirst 'lib/x86_64/libjsc.so'
        pickFirst 'lib/arm64-v8a/libjsc.so'
        pickFirst 'lib/arm64-v8a/libc++_shared.so'
        pickFirst 'lib/x86_64/libc++_shared.so'
        pickFirst 'lib/armeabi-v7a/libc++_shared.so'
        exclude '**/libjsc*.so'
        exclude '**/libhermes-inspector.so'
        exclude '**/libhermes-executor-debug.so'
        exclude '**/libhermes-executor-common-debug.so'
    }
    ...
}
```

### 4) Start the steuerbot activity

Start the steuerbot activity in your app when you wan't your users to switch to steuerbot tax integration.
For example in a click handler in an activity `MainActivity.kt` it can look like this:

```kotlin
import com.steuerbot.sdk.Steuerbot

    ...
    fun startReactApp(view: View) {
        Steuerbot.startActivity(this)
    }
    ...
```

If you wan't to customize the user experience, you can provide a config. Here an example for kotlin:

```kotlin
val config = Bundle();
config.putString("test", "Hello from native app")
Steuerbot.startActivity(this, config)
```

## Advanced: Load Android SDK as dynamic module

Create a feature module with the upper instructions based on this [https://developer.android.com/guide/playcore/feature-delivery](documentation). 

The call of the SteuerbotActivity could look like this:

```kotlin
// Creates an instance of SplitInstallManager.
val splitInstallManager = SplitInstallManagerFactory.create(this)

// Creates a request to install a module.
val request =
    SplitInstallRequest
        .newBuilder()
        // You can download multiple on demand modules per
        // request by invoking the following method for each
        // module you want to install.
        .addModule("steuerbot")
        .build()

splitInstallManager
    // Submits the request to install the module through the
    // asynchronous startInstall() task. Your app needs to be
    // in the foreground to submit the request.
    .startInstall(request)
    // You should also be able to gracefully handle
    // request state changes and errors. To learn more, go to
    // the section about how to Monitor the request state.
    .addOnSuccessListener { sessionId ->
        val config = Bundle();
        config.putString("test", "Hello from native app")
        startActivity(
            Intent()
                .setClassName("your.app.package", "com.steuerbot.sdk.SteuerbotActivity")
                .putExtras(config)
        )
    }
```
