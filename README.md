## A replacement for Gradle 5 osgi plugin

Since Gradle 6, `osgi` plugin was removed from Gradle core plugins, but some builds still depend on this plugin.

This repo extracts the legacy `osgi` plugin (and unit test) from Gradle 5.6.4 source code.

## How to use

Just replace the original `apply plugin: 'osgi'` with `apply plugin: 'com.github.blindpirate.osgi'.

See [it on Gradle plugin portal](https://plugins.gradle.org/plugin/com.github.blindpirate.osgi). 

