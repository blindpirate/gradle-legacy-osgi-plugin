/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.blindpirate.osgi.plugins.osgi

import org.gradle.api.plugins.JavaPlugin

class OsgiPluginTest extends AbstractProjectBuilderSpec {
    private OsgiPlugin osgiPlugin = new OsgiPlugin()

    void appliesTheJavaPlugin() {
        osgiPlugin.apply(project)

        expect:
        project.plugins.hasPlugin('java-base')
        project.extensions.getByName('osgi') instanceof OsgiExtension
    }

    void addsAnOsgiManifestToTheDefaultJar() {
        project.apply(plugin: 'java')
        osgiPlugin.apply(project)

        expect:
        OsgiManifest osgiManifest = project.jar.manifest
        osgiManifest.classpath == project.configurations."$JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME"
        osgiManifest.classesDir == new File(project.rootDir, "build/osgi-classes")
    }
}
