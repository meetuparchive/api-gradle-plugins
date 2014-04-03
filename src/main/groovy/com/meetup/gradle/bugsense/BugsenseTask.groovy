package com.meetup.gradle.bugsense

import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
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
            MultipartEntity entity = new MultipartEntity()
            entity.addPart('app_version', new StringBody(appVersion))
            entity.addPart('file', new FileBody(mappingFile))
            req.entity = entity
        }
    }
}
