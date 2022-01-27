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
                password = "ghp_" + "aWbyRnfk5Vca2cNgXeQWAUsT0Cke2r1q6ZF7"
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
    implementation 'com.steuerbot.sdk:sdk:0.0.3-SNAPSHOT'
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
import com.steuerbot.sdk.User
import com.steuerbot.sdk.Language
import com.steuerbot.sdk.Gender
import com.steuerbot.sdk.action.TaxYearAction
import com.steuerbot.sdk.Address

    ...
    fun startReactApp(view: View) {
        Steuerbot()
            // required
            .setPartnerId("your-partner-id")
            .setToken("your-user-token")
            .setUser(
                User("user@email.com", "Max")
                    // optional
                    .setSurname("Power")
                    .setTaxId("user-tax-id")
                    .setIban("user-iban")
                    .setBirthday("YYYY-MM-DD")
                    .setGender(Gender.MALE)
                    .setPhoneNumber("user-phone-number")
                    .setAddress(
                        Address()
                            // required
                            .setStreet("user-street")
                            .setNumber("user-street-number")
                            .setZip("user-zip")
                            .setCity("user-city")
                            .setState("user-state")
                            .setCountry("user-country")
                            .setCountryLong("user-country-long")
                            .setLatitude(48.823538643594055)
                            .setLongitude(9.273727307951013)
                            // optional
                            .setNumberAddition("A")
                            .setSupplement("b")
                    )
            )
            // optional
            .setLanguage(Language.EN)
            .setLightTheme("{\"name\":\"ExampleLight\",\"colors\":{\"primary100\":\"#880D1E\",\"primary90\":\"#DD2D4A\",\"primary80\":\"#F26A8D\",\"primary60\":\"#F49CBB\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setDarkTheme("{\"name\":\"ExampleDark\",\"colors\":{\"gray0\":\"#202121\",\"gray10\":\"#000000\",\"gray20\":\"#626263\",\"gray30\":\"#79797A\",\"gray40\":\"#9A9A9B\",\"gray50\":\"#BBBBBC\",\"gray60\":\"#DCDCDD\",\"gray70\":\"#E5E5E6\",\"gray80\":\"#EEEEEF\",\"gray90\":\"#F8F8F9\",\"gray100\":\"#FFFFFF\",\"primary100\":\"#B7A9ED\",\"primary90\":\"#A07EED\",\"primary80\":\"#8947F6\",\"primary60\":\"#6C3ECC\",\"primary40\":\"#40239F\",\"primary20\":\"#29185C\",\"warning100\":\"#FEE0AA\",\"warning90\":\"#FED68A\",\"warning80\":\"#FEBD63\",\"warning60\":\"#E4AA69\",\"warning40\":\"#B78859\",\"warning20\":\"#504535\",\"danger100\":\"#FEA3A2\",\"danger90\":\"#FF7B83\",\"danger80\":\"#FE5C62\",\"danger60\":\"#D25B75\",\"danger40\":\"#AB3E44\",\"danger20\":\"#411D21\",\"success100\":\"#B1DDA6\",\"success90\":\"#A1DD8A\",\"success80\":\"#63DD6B\",\"success60\":\"#72C181\",\"success40\":\"#5A9670\",\"success20\":\"#2B4B2B\",\"info100\":\"#A8C7F9\",\"info90\":\"#92B1F9\",\"info80\":\"#7999F9\",\"info60\":\"#6884D7\",\"info40\":\"#5D75BF\",\"info20\":\"#243254\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setAction(TaxYearAction(2021))
            .start(this)
    }
    ...
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
        Steuerbot()
            // required
            .setPartnerId("your-partner-id")
            .setToken("your-user-token")
            .setUser(User("user@email.com", "Max").setSurname("Power"))
            // optional
            .setLanguage(Language.EN)
            .setLightTheme("{\"name\":\"ExampleLight\",\"colors\":{\"primary100\":\"#880D1E\",\"primary90\":\"#DD2D4A\",\"primary80\":\"#F26A8D\",\"primary60\":\"#F49CBB\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setDarkTheme("{\"name\":\"ExampleDark\",\"colors\":{\"gray0\":\"#202121\",\"gray10\":\"#000000\",\"gray20\":\"#626263\",\"gray30\":\"#79797A\",\"gray40\":\"#9A9A9B\",\"gray50\":\"#BBBBBC\",\"gray60\":\"#DCDCDD\",\"gray70\":\"#E5E5E6\",\"gray80\":\"#EEEEEF\",\"gray90\":\"#F8F8F9\",\"gray100\":\"#FFFFFF\",\"primary100\":\"#B7A9ED\",\"primary90\":\"#A07EED\",\"primary80\":\"#8947F6\",\"primary60\":\"#6C3ECC\",\"primary40\":\"#40239F\",\"primary20\":\"#29185C\",\"warning100\":\"#FEE0AA\",\"warning90\":\"#FED68A\",\"warning80\":\"#FEBD63\",\"warning60\":\"#E4AA69\",\"warning40\":\"#B78859\",\"warning20\":\"#504535\",\"danger100\":\"#FEA3A2\",\"danger90\":\"#FF7B83\",\"danger80\":\"#FE5C62\",\"danger60\":\"#D25B75\",\"danger40\":\"#AB3E44\",\"danger20\":\"#411D21\",\"success100\":\"#B1DDA6\",\"success90\":\"#A1DD8A\",\"success80\":\"#63DD6B\",\"success60\":\"#72C181\",\"success40\":\"#5A9670\",\"success20\":\"#2B4B2B\",\"info100\":\"#A8C7F9\",\"info90\":\"#92B1F9\",\"info80\":\"#7999F9\",\"info60\":\"#6884D7\",\"info40\":\"#5D75BF\",\"info20\":\"#243254\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .start(this)
    }
```

**The example uses the helper classes located at [/resources/helper](/resources/helper)**

You have to provide the following default values:
* `DeviceCredentialHandlerTheme` in `res/values/themes.xml`
* `google_play_services_version` in `res/values/steuerbot.xml`

You can have a look in the examples folder and copy the values from there.