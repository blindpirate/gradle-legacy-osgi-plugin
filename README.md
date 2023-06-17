## A replacement for deprecated Gradle 5 osgi plugin

`osgi` plugin has been removed from Gradle core plugins since Gradle 6, yet some builds still depend on it.

This repo extracts the legacy `osgi` plugin (and unit test) from Gradle 5.6.4 source code.

If you're still using the deprecated `osgi` Gradle plugin in Gradle 6+, you can use this plugin (`com.github.blindpirate.osgi`) as an option.

## How to use

[![Download](https://img.shields.io/gradle-plugin-portal/v/com.github.blindpirate.osgi)](https://plugins.gradle.org/plugin/com.github.blindpirate.osgi)

Just replace the original `apply plugin: 'osgi'` with `apply plugin: 'com.github.blindpirate.osgi'`.
