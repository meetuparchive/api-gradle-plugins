/*
 * Copyright 2014 Meetup
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                if (variant.obfuscation) {
                    def task = project.tasks.create("bugsenseUpload${variant.name.capitalize()}", BugsenseTask)
                    task.configure {
                        def bugsense = project.extensions.getByType(BugsensePluginExtension)
                        mappingFile new File("${project.buildDir}/proguard/${variant.dirName}/mapping.txt")
                        apiKey bugsense.apiKey
                        token bugsense.token
                        appVersion variant.versionName
                    }
                    task.description = 'Uploads proguard symbols from the ' + variant.name.capitalize() + ' build to Bugsense.'
                    task.dependsOn variant.obfuscation
                }
            }
        }
    }
}

class BugsensePluginExtension {
    String apiKey
    String token
}
