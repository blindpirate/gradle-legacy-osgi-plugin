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
package com.github.blindpirate.osgi.plugins.osgi;

import com.github.blindpirate.osgi.internal.plugins.osgi.DefaultOsgiManifest;
import com.github.blindpirate.osgi.internal.plugins.osgi.OsgiHelper;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.internal.IConventionAware;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.BasePluginConvention;
import org.gradle.internal.Actions;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.util.DeprecationLogger;

import java.util.concurrent.Callable;

import static org.gradle.util.ConfigureUtil.configure;

/**
 * Is mixed into the project when applying the {@link OsgiPlugin}.
 */
public class OsgiPluginConvention {
    private final ProjectInternal project;

    /**
     * Creates an {@link OsgiPluginConvention} instance.
     *
     * @param project the project instance
     */
    public OsgiPluginConvention(ProjectInternal project) {
        this.project = project;
    }

    /**
     * Creates a new instance of {@link OsgiManifest}. The returned object is preconfigured with:
     * <pre>
     * version: project.version
     * name: project.archivesBaseName
     * symbolicName: project.group + "." + project.archivesBaseName (see below for exceptions to this rule)
     * </pre>
     *
     * The symbolic name is usually the group + "." + archivesBaseName, with the following exceptions
     * <ul>
     * <li>if group has only one section (no dots) and archivesBaseName is not null then the
     * first package name with classes is returned. eg. commons-logging:commons-logging -&gt;
     * org.apache.commons.logging</li>
     * <li>if archivesBaseName is equal to last section of group then group is returned. eg.
     * org.gradle:gradle -&gt; org.gradle</li>
     * <li>if archivesBaseName starts with last section of group that portion is removed. eg.
     * org.gradle:gradle-core -&gt; org.gradle.core</li>
     * </ul>
     *
     * @return the created OsgiManifest instance
     */
    public OsgiManifest osgiManifest() {
        return osgiManifest(Actions.<OsgiManifest>doNothing());
    }

    /**
     * Creates and configures a new instance of an  {@link OsgiManifest} . The closure configures
     * the new manifest instance before it is returned.
     *
     * @param closure the closure to configure the created OsgiManifest instance
     * @return the created OsgiManifest instance
     */
    public OsgiManifest osgiManifest(Closure closure) {
        return configure(closure, createDefaultOsgiManifest());
    }

    /**
     * Creates and configures a new instance of an  {@link OsgiManifest}. The action configures
     * the new manifest instance before it is returned.
     * @since 3.5
     * @param action the configuration action
     * @return the created OsgiManifest instance
     */
    public OsgiManifest osgiManifest(Action<? super OsgiManifest> action) {
        OsgiManifest manifest = createDefaultOsgiManifest();
        action.execute(manifest);
        return manifest;
    }

    private OsgiManifest createDefaultOsgiManifest() {
        OsgiManifest osgiManifest = project.getServices().get(Instantiator.class).newInstance(DefaultOsgiManifest.class, project.getFileResolver());
        ConventionMapping mapping = ((IConventionAware) osgiManifest).getConventionMapping();
        final OsgiHelper osgiHelper = new OsgiHelper();

        mapping.map("version", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return osgiHelper.getVersion(project.getVersion().toString());
            }
        });
        mapping.map("name", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return project.getConvention().getPlugin(BasePluginConvention.class).getArchivesBaseName();
            }
        });
        mapping.map("symbolicName", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return osgiHelper.getBundleSymbolicName(project);
            }
        });

        return osgiManifest;
    }
}
