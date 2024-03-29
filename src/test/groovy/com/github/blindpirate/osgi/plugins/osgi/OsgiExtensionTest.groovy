/*
 * Copyright 2010 the original author or authors.
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

import com.github.blindpirate.osgi.internal.plugins.osgi.DefaultOsgiManifest
import com.github.blindpirate.osgi.internal.plugins.osgi.OsgiHelper
import org.gradle.api.Action
import org.gradle.api.plugins.JavaBasePlugin
import spock.lang.Issue

class OsgiExtensionTest extends AbstractProjectBuilderSpec {

    OsgiExtension extension

    def setup() {
        extension = new OsgiExtension(project)
        project.pluginManager.apply(JavaBasePlugin)
    }

    def osgiManifestWithNoClosure() {
        OsgiManifest osgiManifest = extension.osgiManifest()

        expect:
        matchesExpectedConfig(osgiManifest)
    }

    def osgiManifestWithClosure() {
        OsgiManifest osgiManifest = extension.osgiManifest {
            description = 'myDescription'
        }

        expect:
        matchesExpectedConfig(osgiManifest)
        osgiManifest.description == 'myDescription'
    }

    def osgiManifestWithAction() {
        OsgiManifest osgiManifest = extension.osgiManifest({ OsgiManifest manifest ->
            manifest.description = 'myDescription'
        } as Action<OsgiManifest>)

        expect:
        matchesExpectedConfig(osgiManifest)
        osgiManifest.description == 'myDescription'
    }

    @Issue("GRADLE-1670")
    def "doesn't assume that project version is a String"() {
        project.version = new Object() {
            String toString() {
                "2.1"
            }
        }
        def manifest = extension.osgiManifest()

        expect:
        manifest.version == "2.1"
    }

    @Issue("GRADLE-1670")
    def "computes its defaults lazily"() {
        def manifest = extension.osgiManifest()
        def i = 0
        project.version = "${-> ++i}"
        project.group = "my.group"
        project.archivesBaseName = "myarchive"

        expect:
        manifest.version == "1"
        manifest.version == "2"
        manifest.name == "myarchive"
        manifest.symbolicName == "my.group.myarchive"

        when:
        project.group = "changed.group"
        project.archivesBaseName = "changedarchive"

        then:
        manifest.name == "changedarchive"
        manifest.symbolicName == "changed.group.changedarchive"
    }

    void matchesExpectedConfig(DefaultOsgiManifest osgiManifest) {
        OsgiHelper osgiHelper = new OsgiHelper()
        assert osgiManifest.version == osgiHelper.getVersion((String) project.version)
        assert osgiManifest.name == project.archivesBaseName
        assert osgiManifest.symbolicName == osgiHelper.getBundleSymbolicName(project)
    }
}
