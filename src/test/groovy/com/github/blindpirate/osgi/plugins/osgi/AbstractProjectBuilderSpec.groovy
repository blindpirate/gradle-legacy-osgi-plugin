package com.github.blindpirate.osgi.plugins.osgi

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.TempDir

class AbstractProjectBuilderSpec extends Specification {
    @TempDir
    File dir

    ProjectInternal project

    def setup() {
        project = ProjectBuilder
            .builder()
            .withProjectDir(dir)
            .build()
    }
}
