package net.dankito.richtexteditor.model

import java.io.File


open class DownloadImageConfig(val uiSetting: DownloadImageUiSetting,
                          val downloadFolderIfUserIsNowAllowedToSelectFolder: File? = null,
                          val predefinedDownloadFoldersUserCanChooseFrom: List<File>? = null,
                          val fileNameSelectorCallback: ((url: String) -> String)? = null)