package com.meetup.gradle.bugsense

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class BugsensePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin(AppPlugin)) {
            throw new ProjectConfigurationException("The android plugin must be applied to the project.", null)
        }
        project.extensions.create("bugsense", BugsensePluginExtension)
        project.afterEvaluate {
            project.android.applicationVariants.all { ApplicationVariant variant ->
                if (variant.proguard) {
                    def task = project.tasks.create("bugsenseUpload${variant.name.capitalize()}", BugsenseTask)
                    task.configure {
                        mappingFile new File("${project.buildDir}/proguard/${variant.dirName}/mapping.txt")
                        apiKey project.bugsense.apiKey
                        token project.bugsense.token
                        appVersion variant.versionName
                    }
                    task.description = 'Uploads proguard symbols from the ' + variant.name.capitalize() + ' build to Bugsense.'
                    task.dependsOn variant.proguard
                }
            }
        }
    }
}

class BugsensePluginExtension {
    String apiKey
    String token
}