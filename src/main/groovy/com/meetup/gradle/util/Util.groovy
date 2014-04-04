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

package com.meetup.gradle.util

import com.google.common.base.Charsets
import groovyx.net.http.HTTPBuilder
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.HttpClients

class Util {
    static String boolToInt(boolean b) {
        return b ? "1" : "0"
    }

    static MultipartEntity makeEntity(Map<String, ?> data) {
        MultipartEntity entity = new MultipartEntity();
        data.each { k, v ->
            if (v instanceof File) {
                entity.addPart(k, new FileBody(v))
            } else {
                entity.addPart(k, new StringBody(v.toString(), Charsets.UTF_8))
            }
        }
        entity
    }

    static HTTPBuilder http(Object defaultURI) {
        def http = new HTTPBuilder(defaultURI)
        http.client = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSocketFactory()).build()
        http
    }
}
