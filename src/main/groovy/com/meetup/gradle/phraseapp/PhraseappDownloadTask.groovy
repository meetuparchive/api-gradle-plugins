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
import org.apache.tools.ant.BuildException
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class PhraseappDownloadTask extends AbstractPhraseappTask {
    @Input
    Collection<String> otherLocales

    @Input
    File outputPath

    @TaskAction
    void download() {
        def logger = Logging.getLogger(PhraseappDownloadTask)
        Collection<String> targets

        if (otherLocales == null || otherLocales.isEmpty()) {
            targets = get("locales", ContentType.JSON)*.get("code") - mainLocale
        } else {
            targets = otherLocales
        }

        targets.each { locale ->
            String lang, country
            if (locale.contains('-')) {
                def split = locale.split('-')
                lang = split[0]
                country = split[1]
            } else {
                lang = locale
                country = null
            }
            def outputDir = new File(outputPath, "values-${lang}-r${country}")
            if (!outputDir.exists()) {
                outputDir = new File(outputPath, "values-${lang}")
                if (!outputDir.exists()) {
                    throw new BuildException("couldn't figure out where to save values for language ${locale}")
                }
            }
            def outputFile = new File(outputDir, "strings.xml")
            logger.info("Saving ${locale} translations to ${outputFile}")
            def xml = get("locales/${locale}.xml", ContentType.TEXT) // text, not XML, so groovy doesn't try to parse it
            outputFile.withWriter{ out -> out.write(xml.text) }
        }
    }
}
