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

import com.meetup.gradle.util.Util
import groovyx.net.http.HTTPBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

class BugsenseTask extends DefaultTask {
    @Input
    String apiKey

    @Input
    String token

    @InputFile
    File mappingFile

    @Input
    String appVersion

    @TaskAction
    def uploadBugsense() {
        def http = new HTTPBuilder("https://www.bugsense.appspot.com")
        http.request(POST, JSON) { req ->
            uri.path = "/api/v1/project/${apiKey}/mappings.json"
            headers.'X-BugSense-Auth-Token' = token
            requestContentType = 'multipart/form-data'
            req.entity = Util.makeEntity([
                app_version: appVersion,
                file: mappingFile
            ])
        }
    }
}
