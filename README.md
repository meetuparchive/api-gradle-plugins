api-gradle-plugins
==================

This repository contains a couple of small plugins for connecting some third-party services to your Android project which is built with [gradle][new-build-system].

Installing the plugins
----------------------

In your build script:

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven { url 'https://dl.bintray.com/meetup/maven' }
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:0.9.2'
        classpath 'com.meetup.gradle:api-gradle-plugins:1.0'
    }
}

apply plugin: 'android'
// choose whichever plugin(s) you need:
apply plugin: 'bugsense-android'
apply plugin: 'phraseapp-android'
```

BugSense Plugin
---------------

You can automatically upload proguard mapping files to [BugSense][]:

```groovy
apply plugin: 'bugsense-android'

bugsense {
    apiKey '12345678'
    token '1234567890abcdef1234567'
}
```

Your `apiKey` varies per project and can be found on the project settings page (or, likely, somewhere within the app). Your `token` can be found or generated on your [account page][bugsense-account]. Both are required.

Then, each build variant which is subject to proguard processing will have a "bugsenseUpload" task attached, so you can do, for example:

```
gradle bugsenseUploadRelease
```

PhraseApp Plugin
----------------

You can automatically upload your source language's `strings.xml` file to [PhraseApp][]. Only the `authToken` configuration item is needed (and probably it's all you need); the others default as shown.

```groovy
apply plugin: 'phraseapp-android'

phraseapp {
    authToken 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' // required! from the project settings page
    mainLocale 'en-US' // optional
    mainFile 'values/strings.xml' // optional, location within the `res` folder of your source strings in the main locale
    updateTranslations false // optional, if all translations in the main locale should be updated on upload
    skipUnverification false // optional, controls skipping "unverification" on upload
    skipUploadTags false // optional, controls skipping automatic creation of an upload tag
    otherLocales 'll-rr',.... // optional, default is all locales inside phraseapp except the main locale
}
```

Then, you can do `gradle phraseappUpload` to upload your source language's strings file to PhraseApp, and `gradle phraseappDownload` to download the target languages' files and store them in your source tree.

**WARNING**: Connecting to PhraseApp's server does not currently work on JDK 6 or earlier. You must be running JDK 7 or 8 on the machine which does the build.

[new-build-system]: http://tools.android.com/tech-docs/new-build-system "Android Tools Project Site: New Build System"
[BugSense]: http://www.bugsense.com/
[bugsense-account]: https://www.bugsense.com/account#myinfo
[PhraseApp]: https://phraseapp.com/
