package com.meetup.gradle.phraseapp

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

import static com.meetup.gradle.util.Util.boolToInt

class PhraseappUploadTask extends AbstractPhraseappTask {
    @InputFile
    File resources

    @Input
    boolean updateTranslations

    @Input
    boolean skipUnverification

    @Input
    boolean skipUploadTags

    @TaskAction
    def upload() {
        postApiFile("file_imports", [
                'file_import[locale_code]': mainLocale,
                'file_import[file]': resources,
                'file_import[format]': 'xml',
                'file_import[update_translations]': boolToInt(updateTranslations),
                'file_import[skip_verification]': boolToInt(skipUnverification),
                'file_import[skip_upload_tags]': boolToInt(skipUploadTags)
        ])
    }
}
