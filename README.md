# Shopiroller-uikit-android


![Platform](https://img.shields.io/badge/platform-Android-orange.svg)
![Languages](https://img.shields.io/badge/language-Kotlin-orange.svg)
<br>

Social
---

[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/shopiroller)
[![Instagram](https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/shopiroller/)




Table of Contents
===
<!--ts-->
   * [Introduction](#introduction) 
   * [Before getting started](#before-getting-started)
   * [Getting Started](#getting-started)

<!--te-->

## Introduction

It is now very easy to have your own mobile application, we are introducing a new sdk for android. This Shopiroller SDK let you to closer to your users. 

Shopiroller UIKit for Android is a development kit with an user interface, you can customize layout or colors for your use case wahetever you want.

- **shopiroller** is where you can find the open source code.
- **root App** is a ecommerce a main app to build shopiroller SDK.
- Other details are explained below.

<br />

## Before getting started
---

This section shows you the prerequisites you need for testing Shopiroller UIKit for Android sample apps.

Requirements
The minimum requirements for UIKit for Android are:

* Android 5.0 (API level 21) or higher
* Java 8 or higher
* Android Gradle plugin 4.0.1 or higher

### Initialize With Credentials
<br>

In Shopiroller Listener you can handle user actions with listener. You can implement this listener to your activity or fragment to set user actions when the user clicks the corresponding button.

```kotlin
interface ShopirollerListener {
    fun onBadgeCountChanged(@NonNull count: Int) //ShoppingCart Count Changed
    fun loginNeeded() //UserLogin Needed Handle
    fun onStartShoppingClicked() //Start Order Flow
    fun onDefaultAddressChanged(billingAddressId: String, shippingAddressId: String) //Set Default Address
    fun onNewShippingAddressClicked(activity: FragmentActivity) //Add New Shipping Address
    fun onNewBillingAddressClicked(activity: FragmentActivity) //Add New Billing Address
    fun onEditShippingAddressClicked(activity: FragmentActivity, shippingAddress: UserShippingAddressModel) //Edit Shippinh Address
    fun onEditBillingAddressClicked(activity: FragmentActivity, billingAddressModel: UserBillingAddressModel) //Edit Billing Address
    fun getDefaultAddresses(listener: ShopirollerCallBackListener<DefaultAddressList>) //Get User's default address list
    fun getBillingAddresses(listener: ShopirollerCallBackListener<List<UserBillingAddressModel>>) //Get User's Billing Address List
    fun getShippingAddresses(listener: ShopirollerCallBackListener<List<UserShippingAddressModel>>) //Get User's Shipping Address List
}
```

In Shopiroller Interface 
* You can set **userLoginStatus** to use **loginNeeded()** function in ShopirollerListener
* You need to change **userId** variable with your **userId**
* You need to change **userEmail** variable with your **userEmail**
* You need to change **userFullName** variable with your **userFullName**
* You can change your app's colors with edit **Theme Class**

First you need implement **ShopirollerAdapter** to your application like below code:

```kotlin
class SampleApplication: Application(), ShopirollerAdapter {

    companion object {
        var shopirollerAdapter: ShopirollerAdapter? = null
    }

    override fun onCreate() {
        super.onCreate()
        shopirollerAdapter = this
    }

    override fun getAppKey(): String {
        return "yourAppKey"
    }

    override fun getApiKey(): String {
        return "yourApiKey"
    }

    override fun getAliasKey(): String? {
        return "yourAliasKey"
    }

    override fun getDefaultLang(): String {
        return "preferredDefaultLanguage"
    }

    override fun getLocale(): String {
        return "pereferredLocale"
    }

    override fun getApplyzeBaseURL(): String {
        return "userManagementBaseUrl"
    }

    override fun getShopirollerBaseURL(): String {
        return "shopirollerBaseUrl"
    }

    override fun getActionBarColor(): Int {
        return Color.someColor
    }
    
}
```

<br>

After that you need to create instance with init function of Shopiroller Interface you need to add adapter and context as parameter.

```kotlin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SampleApplication.shopirollerAdapter?.let { init(it, this@MainActivity) } //We created instance of Shopiroller Interface
        setContentView(R.layout.activity_main)
    }
}
```
Like the code above created instance for shopirollerAdapter in SampleApplication to use our credentials in app.

After that you can create shopirollerFragment in here , like below code : 

```kotlin
    val shopirollerFragment = ShopirollerFragment()

    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(
        R.id.shopiroller_frame_layout,
        shopirollerFragment
        )
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
```

<br>

## Getting Started
<br>

### Create a project

Go to your `Android Studio` and create a project for UIKit for Android in the **Project window** as follows:

1. In the **Welcome to Android Studio** window, click **Start a new Android Studio project**.
2. In the **Select a Project Template** window, select **Empty Activity**, and click **Next**.
3. Enter your project name in the **Name** field in the **Configure your project** window.
4. Select your language as either **Java** or **Kotlin** from the **Language** drop-down menu.
6. Select minimum API level as 21 or higher.
<br>

### Install Shopiroller UIKit for Android

You should add code blocks below in the project's top-level `build.gradle` :

Then, open the `build.gradle` file at the application level. For `Java` and `Kotlin`, add code blocks and dependencies as below:

> Note: View binding should be enabled in your `build.gradle` file.

```gradle
apply plugin: 'com.android.application'

android {    
    buildFeatures {
        viewBinding true
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'com.shopiroller.uikit:shopiroller:0.0.2'
}
```

After saving your `build.gradle` file, click the **Sync** button to apply all the changes. 

<br />