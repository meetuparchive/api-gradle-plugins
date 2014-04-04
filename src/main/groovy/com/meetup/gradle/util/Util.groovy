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
