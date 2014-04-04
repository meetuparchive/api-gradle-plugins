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
