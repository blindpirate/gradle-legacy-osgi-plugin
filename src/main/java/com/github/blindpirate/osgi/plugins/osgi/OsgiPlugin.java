/*
 * Copyright 2016 the original author or authors.
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
package com.github.blindpirate.osgi.plugins.osgi;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.Sync;
import org.gradle.api.tasks.bundling.Jar;

import java.io.File;

/**
 * A {@link Plugin} which extends the {@link JavaPlugin} to add OSGi meta-information to the project Jars.
 */
public class OsgiPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        project.getPluginManager().apply(JavaBasePlugin.class);

        final OsgiExtension extension = project.getExtensions().create("osgi", OsgiExtension.class, project);

        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {

            // When creating the OSGi manifest, we must have a single view of all the classes included in the jar.
            Sync prepareOsgiClasses = project.getTasks().create("osgiClasses", Sync.class);
            FileCollection classes = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main").getOutput().getClassesDirs();
            File singleClassesDirectory = project.getLayout().getBuildDirectory().file("osgi-classes").get().getAsFile();
            prepareOsgiClasses.setDescription("Prepares a single classes directory required for OSGi analysis.");
            prepareOsgiClasses.from(classes);
            prepareOsgiClasses.into(singleClassesDirectory);

            Jar jarTask = (Jar) project.getTasks().getByName("jar");
            jarTask.dependsOn(prepareOsgiClasses);
            OsgiManifest osgiManifest = extension.osgiManifest();
            osgiManifest.setClassesDir(singleClassesDirectory);
            osgiManifest.setClasspath(project.getConfigurations().getByName("runtimeClasspath"));

            jarTask.setManifest(osgiManifest);
        });
    }
}
