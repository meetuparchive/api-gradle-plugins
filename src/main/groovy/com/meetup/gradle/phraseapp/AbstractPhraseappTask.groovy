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
