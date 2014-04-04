api-gradle-plugins
==================

This repository contains a couple of small plugins for connecting some third-party services to your Android project which is built with [gradle][new-build-system].

[new-build-system]: http://tools.android.com/tech-docs/new-build-system "Android Tools Project Site: New Build System"

Installing the plugins
----------------------

In your build script:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:0.9.2'
        classpath 'com.meetup:api-gradle-plugins:1.0'
    }
}

apply plugin: 'android'
// choose whichever plugin(s) you need:
apply plugin: 'bugsense-android'
apply plugin: 'phraseapp-android'
```