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

This section shows you the prerequisites you need for testing Sendbird UIKit for Android sample apps.

Requirements
The minimum requirements for UIKit for Android are:

* Android 5.0 (API level 21) or higher
* Java 8 or higher
* Support androidx only
* Android Gradle plugin 4.0.1 or higher
* Sendbird Chat SDK for Android 4.0.3 and later

### Try the sample app using your data
<br>

If you want to try the sample app , you need to change variables in SampleApplication 

<br>

```kotlin
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
```

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