package com.meetup.gradle.phraseapp

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class PhraseappPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin(AppPlugin)) {
            throw new ProjectConfigurationException("The android plugin must be applied to the project.", null)
        }
        project.extensions.create("phraseapp", PhraseappPluginExtension)
        project.afterEvaluate {
            PhraseappPluginExtension phraseapp = project.phraseapp

            project.android.sourceSets.each { AndroidSourceSet s ->
                def dir = s.res.srcDirs.find{ new File(it, phraseapp.mainFile).exists() }
                if (dir) {
                    def file = new File(dir, phraseapp.mainFile)
                    def suffix = s.name == "main" ? "" : s.name.capitalize()

                    def upTask = project.tasks.create("phraseappUpload${suffix}", PhraseappUploadTask)
                    upTask.configure {
                        authToken phraseapp.authToken
                        mainLocale phraseapp.mainLocale
                        resources file
                        updateTranslations phraseapp.updateTranslations
                        skipUnverification phraseapp.skipUnverification
                        skipUploadTags phraseapp.skipUploadTags
                    }
                    upTask.description = "Upload strings from the ${s.name} source set to PhraseApp"

                    def downTask = project.tasks.create("phraseappDownload${suffix}", PhraseappDownloadTask)
                    downTask.configure {
                        authToken = phraseapp.authToken
                        mainLocale = phraseapp.mainLocale
                        otherLocales = phraseapp.otherLocales
                        outputPath = dir
                    }
                    downTask.description = "Download strings from the ${s.name} source set to PhraseApp"
                }
            }
        }
    }
}

class PhraseappPluginExtension {
    String authToken
    String mainLocale = "en-US"
    String mainFile = "values/strings.xml"
    boolean updateTranslations = false
    boolean skipUnverification = false
    boolean skipUploadTags = false
    private Set<String> mOtherLocales

    void otherLocale(String locale) {
        if (mOtherLocales == null)
            mOtherLocales = new HashSet<>()

        mOtherLocales.add(locale)
    }

    void otherLocales(String... locales) {
        if (mOtherLocales == null)
            mOtherLocales = new HashSet<>()

        mOtherLocales.addAll(locales)
    }

    void otherLocales(Collection<String> locales) {
        if (mOtherLocales == null)
            mOtherLocales = new HashSet<>()

        mOtherLocales.addAll(locales)
    }

    public Collection<String> getOtherLocales() {
        if (mOtherLocales == null)
            mOtherLocales = new HashSet<>()

        return mOtherLocales
    }
}