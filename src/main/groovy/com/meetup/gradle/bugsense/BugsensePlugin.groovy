package com.meetup.gradle.bugsense

import org.gradle.api.Plugin
import org.gradle.api.Project

class BugsensePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("bugsense", BugsensePluginExtension)
    }
}

class BugsensePluginExtension {
    String apiKey
}