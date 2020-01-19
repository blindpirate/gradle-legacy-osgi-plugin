## A replacement for deprecated Gradle 5 osgi plugin

`OSGI` plugin has been removed from Gradle core plugins since Gradle 6, yet some builds still depend on it.

This repo extracts the legacy `OSGI` plugin (and unit test) from Gradle 5.6.4 source code.

If you're still using the deprecated `OSGI` Gradle plugin ('osgi'), you need to migrate to using ('com.github.blindpirate.osgi').

## How to use

Just replace the original `apply plugin: 'osgi'` with `apply plugin: 'com.github.blindpirate.osgi'`.

See [it on Gradle plugin portal](https://plugins.gradle.org/plugin/com.github.blindpirate.osgi). 

