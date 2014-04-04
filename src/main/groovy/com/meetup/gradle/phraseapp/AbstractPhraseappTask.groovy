package com.meetup.gradle.phraseapp

import groovyx.net.http.ContentType
import groovyx.net.http.Method
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

import static com.meetup.gradle.util.Util.*

abstract class AbstractPhraseappTask extends DefaultTask {
    public static final String API_BASE = "https://phraseapp.com/api/v1/"

    @Input
    String authToken

    @Input
    String mainLocale

    protected def postApiFile(String path, Map<String, Object> params) {
        def http = http(API_BASE)
        http.request(Method.POST, ContentType.JSON) { req ->
            uri.path = path
            requestContentType = 'multipart/form-data'
            req.entity = makeEntity(params + [auth_token: authToken])
        }
    }

    protected def get(String path, ContentType type) {
        get(path, type, [:])
    }

    protected def get(String path, ContentType type, Map<String, String> params) {
        def http = http(API_BASE)
        http.request(Method.GET, type) { req ->
            uri.path = path
            uri.query = params + [auth_token: authToken]
        }
    }
}
