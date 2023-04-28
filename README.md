# Steuerbot Android SDK

Integrate the Steuerbot SDK in your Android App. Let your users declare their tax declaration in minutes!

🚀&nbsp;&nbsp;Fast integration<br>
🎨&nbsp;&nbsp;Fully customizable<br>
🎚️&nbsp;&nbsp;Many additional settings<br>
🤗&nbsp;&nbsp;Happy customers<br>

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
                password = "ghp_" + "dj0iF9vxUlUXfWcDaQ9Awb91MkEtBS4HaQ3k"
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
    implementation 'com.steuerbot.sdk:sdk:2.13.0'
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

### 4) Starting Steuerbot

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
            .setPartnerName("YourPartnerName")
            .setToken("user-token")
            .setUser(
                User("user@email.com", "Max")
                    // optional
                    .setSurname("Power")
                    .setTaxId("user-tax-id") // a 11-digit number
                    .setIban("user-iban") // format must follow ISO 13616:2020 standard
                    .setBirthday("YYYY-MM-DD") // format has to be YYYY-MM-DD
                    .setGender(Gender.MALE)
                    .setPhoneNumber("+49 849284727563") // ITU E.123 standard, has to start with area code, e.g. +49 for germany
                    .setAddress(
                        Address()
                            // required
                            .setStreet("Welfenstraße")
                            .setNumber("19")
                            .setZip("70736")
                            .setCity("Fellbach")
                            .setState("BW") // short-form of state, for example BW, BY, NRW, ...
                            .setCountry("DE") // Country code, "DE" for Germany
                            .setCountryLong("Deutschland") 
                            // optional
                            .setLatitude(48.823538643594055)
                            .setLongitude(9.273727307951013)
                            .setNumberAddition("a") // for example "a", "/1", "-2"
                            .setSupplement("apartment") // for example apartment, building, floor
                    )
            )
            .setPaymentLink("yourApp://payment") // set your payment link, used during payment process
            // optional
            .setApiUrl("https://api.test2.steuerbot.com") // default: https://api.test2.steuerbot.com
            .setLanguage(Language.EN) // default: Language.DE
            .setLightTheme("{\"name\":\"ExampleLight\",\"colors\":{\"primary100\":\"#880D1E\",\"primary90\":\"#DD2D4A\",\"primary80\":\"#F26A8D\",\"primary60\":\"#F49CBB\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setDarkTheme("{\"name\":\"ExampleDark\",\"colors\":{\"gray0\":\"#202121\",\"gray10\":\"#000000\",\"gray20\":\"#626263\",\"gray30\":\"#79797A\",\"gray40\":\"#9A9A9B\",\"gray50\":\"#BBBBBC\",\"gray60\":\"#DCDCDD\",\"gray70\":\"#E5E5E6\",\"gray80\":\"#EEEEEF\",\"gray90\":\"#F8F8F9\",\"gray100\":\"#FFFFFF\",\"primary100\":\"#B7A9ED\",\"primary90\":\"#A07EED\",\"primary80\":\"#8947F6\",\"primary60\":\"#6C3ECC\",\"primary40\":\"#40239F\",\"primary20\":\"#29185C\",\"warning100\":\"#FEE0AA\",\"warning90\":\"#FED68A\",\"warning80\":\"#FEBD63\",\"warning60\":\"#E4AA69\",\"warning40\":\"#B78859\",\"warning20\":\"#504535\",\"danger100\":\"#FEA3A2\",\"danger90\":\"#FF7B83\",\"danger80\":\"#FE5C62\",\"danger60\":\"#D25B75\",\"danger40\":\"#AB3E44\",\"danger20\":\"#411D21\",\"success100\":\"#B1DDA6\",\"success90\":\"#A1DD8A\",\"success80\":\"#63DD6B\",\"success60\":\"#72C181\",\"success40\":\"#5A9670\",\"success20\":\"#2B4B2B\",\"info100\":\"#A8C7F9\",\"info90\":\"#92B1F9\",\"info80\":\"#7999F9\",\"info60\":\"#6884D7\",\"info40\":\"#5D75BF\",\"info20\":\"#243254\",\"accentCold80\":\"#CBEEF3\"},\"text\":{\"fontSizes\":{\"xsmall\":8,\"small\":12,\"medium\":18,\"large\":22,\"xlarge\":26}},\"button\":{\"fontSize\":2,\"lineHeight\":0,\"height\":10,\"smallHeight\":0,\"box\":{\"height\":0,\"smallHeight\":0,\"fontSize\":0,\"lineHeight\":0}}}")
            .setAction(TaxYearAction(2021)) // trigger some action on startup like going to tax year
            .activateDebugMode() // activate debug mode if you want to get more insights
            // now start Steuerbot activity
            .start(this)
    }
    ...
```

#### Partner-ID

Here you have to use your unique partner id, which you have received from us.

You don't have a partner id now. No problem, get in touch with us: Write a mail to marc@steuerbot.com.

#### Partner-Name

Set the name, which will be displayed in our integration.

#### Payment

You have to set the payment deeplink used during payment process:

```kotlin
.setPaymentLink("your-link")
```

The SDK will call that link in order to determine the price for your customer.
The parameters `purpose`, `iban`, `refund` (in cent), `offerId`, `submitId` and `botId` will be added.

#### Actions

The following actions could be set via `.setAction()`:

##### TaxYearAction

Jump to a tax year:

```kotlin
.setAction(TaxYearAction(2021))
```

##### PaymentSuccessAction

Complete the payment process:

```kotlin
.setAction(PaymentSuccessAction(999, "submit-id", "offer-id", "bot-id"))
```
Arguments:

|Position| Description                                                                  |
|---|------------------------------------------------------------------------------|
|0| Price in cent                                                                |
|1| Submit-ID, included in the query parameters of payment deeplink (`submitId`) |
|2| Offer-ID, included in the query parameters of payment deeplink (`offerId`)   |
|3| Bot-ID, included in the query parameters of payment deeplink (`botId`)       |

##### SupportAction

Jump to support screen:

```kotlin
.setAction(SupportAction())
```

##### VastAction

Jump to screen with prefilled tax declaration feature:

```kotlin
.setAction(VastAction())
```

Advanced Feature: You can also trigger actions at runtime with the following methods:

```kotlin
import com.steuerbot.sdk.Steuerbot

Steuerbot.triggerAction(SupportAction())
Steuerbot.triggerDeeplink("/support")
```

#### Theming

You can set your own custom themes! Only a light one, a dark one or both.
The following functions expect **JSON** strings.

```kotlin
.setLightTheme("{}")
.setDarkTheme("{}")
```

The specification and all details about Theming can be found on our [Wiki](https://github.com/steuerbot/android-sdk/wiki/Styling)

See [/examples/basic](/examples/basic) for a full working example using the basic solution.

## Other integrations 

### Inheritance

You can also inherit from `SteuerbotActivity` if you want to add custom code/logic to the activity class:

```kotlin
class TaxActivity : SteuerbotActivity() {
}
```

Now you can configure Steuerbot like above and start it like this:

```kotlin
.start(this, TaxActivity::class.java)
```

See [/examples/inheritance](/examples/inheritance) for a full working example using the inheritance solution.

### Fragment

You can also create a Fragment and use it. Configure Steuerbot and call this function.

```kotlin
.buildFragment()
```

See [/examples/fragment](/examples/fragment) for a full working example using the fragment solution.

### Dynamic module

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

You can have a look in the [examples values folder](/examples/dynamic/app/src/main/res/values) and copy the values from there.

See [/examples/dynamic](/examples/dynamic) for a full working example using the dynamic module solution.

Of course the different integration solutions could be mixed, e.g. you could use a fragment after loading the dynamic module.

## License

The contents of this repository are licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
