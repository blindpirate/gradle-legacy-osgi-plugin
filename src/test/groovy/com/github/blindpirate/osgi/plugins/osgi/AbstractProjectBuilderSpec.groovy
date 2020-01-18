package com.github.blindpirate.osgi.plugins.osgi

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class AbstractProjectBuilderSpec extends Specification {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    ProjectInternal project

    def setup() {
        project = ProjectBuilder
                .builder()
                .withProjectDir(temporaryFolder.newFolder())
                .build()
    }


}
