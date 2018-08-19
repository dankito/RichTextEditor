package net.dankito.richtexteditor.android.command.dialogs

import android.Manifest
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.dialog_edit_image.*
import kotlinx.android.synthetic.main.dialog_edit_image.view.*
import net.dankito.filechooserdialog.FileChooserDialog
import net.dankito.filechooserdialog.model.ExtensionsFilter
import net.dankito.filechooserdialog.model.FileChooserDialogConfig
import net.dankito.richtexteditor.android.R
import net.dankito.utils.GenericTextWatcher
import net.dankito.richtexteditor.model.DownloadImageConfig
import net.dankito.richtexteditor.model.DownloadImageUiSetting
import net.dankito.richtexteditor.util.ImageDownloader
import net.dankito.utils.permissions.PermissionsService
import net.dankito.utils.io.AndroidFolderUtils
import net.dankito.utils.web.UrlUtil
import java.io.File

class EditImageDialog : DialogFragment() {

    companion object {
        val DialogTag = EditImageDialog::class.java.name
    }


    private val urlUtil = UrlUtil()


    private lateinit var permissionsService: PermissionsService

    private var downloadImageConfig: DownloadImageConfig? = null

    private var imageUrlEnteredListener: ((imageUrl: String, alternateText: String) -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_edit_image, container)

        view?.let {
            view.btnCancel.setOnClickListener { dismiss() }

            view.btnOk?.setOnClickListener { enteringImageUrlDone() }

            view.btnSelectLocalFile.setOnClickListener { selectLocalImage() }

            view.edtxtImageUrl.setOnEditorActionListener { _, actionId, keyEvent -> handleEditTextUrlAction(actionId, keyEvent) }
            view.edtxtImageUrl.addTextChangedListener(GenericTextWatcher( { _, _, _, _ -> setDownloadOptionsState() } ) )
            view.edtxtImageUrl.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
            }
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) // so that keyboard doesn't cover OK and Cancel buttons
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    override fun onResume() {
        super.onResume()

        setDownloadOptionsState()
    }


    fun show(fragmentManager: FragmentManager, permissionsService: PermissionsService, downloadImageConfig: DownloadImageConfig? = null,
             imageUrlEnteredListener: (imageUrl: String, alternateText: String) -> Unit) {
        this.permissionsService = permissionsService
        this.downloadImageConfig = downloadImageConfig
        this.imageUrlEnteredListener = imageUrlEnteredListener

        this.show(fragmentManager, DialogTag)
    }


    private fun handleEditTextUrlAction(actionId: Int, keyEvent: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_DONE || (actionId == EditorInfo.IME_NULL && keyEvent?.action == KeyEvent.ACTION_DOWN)) {
            enteringImageUrlDone()

            return true
        }

        return false
    }

    private fun selectLocalImage() {
        val config = FileChooserDialogConfig(ExtensionsFilter.WebViewSupportedImages.filter, getCurrentDirectory())

        FileChooserDialog().showOpenSingleFileDialog(activity, permissionsService, config) { didUserSelectFile, selectedFile ->
            selectedFile?.let {
                edtxtImageUrl.setText(selectedFile.absolutePath)
            }
        }
    }

    private fun getCurrentDirectory(): File {
        val folderUtils = AndroidFolderUtils(context)

        var currentDirectory = folderUtils.getCameraPhotosDirectory()
        val imageUrl = enteredImageUrl

        if (imageUrl.isNotBlank()) {
            try {
                currentDirectory = File(imageUrl).parentFile
            } catch (ignored: Exception) { }
        }

        return currentDirectory
    }

    private fun enteringImageUrlDone() {
        val imageUrl = enteredImageUrl

        if (chkbxDownloadImage.isChecked) {
            downloadImageAndFireImageUrlEntered(imageUrl)
        }
        else {
            fireImageUrlEnteredAndDismiss(imageUrl)
        }
    }

    private fun downloadImageAndFireImageUrlEntered(imageUrl: String) {
        val downloader = ImageDownloader()
        val targetFolder = downloader.selectDownloadFolder(downloadImageConfig, context.filesDir)

        if (isInternalFile(targetFolder)) {
            downloadImageAndFireImageUrlEnteredWithPermissionGranted(imageUrl, targetFolder, downloader)
        }
        else {
            permissionsService.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.dialog_edit_image_download_image_write_external_storage_rational) { _, isPermitted ->
                if (isPermitted) {
                    downloadImageAndFireImageUrlEnteredWithPermissionGranted(imageUrl, targetFolder, downloader)
                }
                else {
                    fireImageUrlEnteredAndDismiss(imageUrl) // user didn't allow to download file -> keep remote file and close
                }
            }
        }
    }

    private fun downloadImageAndFireImageUrlEnteredWithPermissionGranted(imageUrl: String, targetFolder: File, downloader: ImageDownloader) {
        targetFolder.mkdirs()

        val fileName = downloadImageConfig?.fileNameSelectorCallback?.invoke(imageUrl) ?: urlUtil.getFileName(imageUrl)
        val targetFile = File(targetFolder, fileName)

        downloader.downloadImageAsync(imageUrl, targetFile) { isSuccessful ->
            if (isSuccessful) {
                fireImageUrlEnteredAndDismiss(targetFile.absolutePath)
            } else { // TODO: show error message
                dismiss()
            }
        }
    }

    private fun isInternalFile(file: File): Boolean {
        return file.startsWith(context.filesDir.absoluteFile)
    }

    private fun fireImageUrlEnteredAndDismiss(imageUrl: String) {
        var alternateText = edtxtAlternateText.text.toString().trim()
        if (alternateText.isBlank()) {
            alternateText = imageUrl
        }

        imageUrlEnteredListener?.invoke(imageUrl, alternateText)

        dismiss()
    }

    private val enteredImageUrl: String
        get() = edtxtImageUrl.text.toString().trim() // SwiftKey app enters ' ' at end which causes line break -> trim // TODO: is this reasonable in all cases?


    private fun setDownloadOptionsState() {
        if (downloadImageConfig == null || downloadImageConfig?.uiSetting == DownloadImageUiSetting.Disallow) {
            lytDownloadImage.visibility = View.GONE
        }
        else {
            lytDownloadImage.visibility = View.VISIBLE

            chkbxDownloadImage.isEnabled = urlUtil.isHttpUri(enteredImageUrl)

            // TODO: implement AllowRestrictPossibleDownloadFolders and AllowLetUserSelectDownloadFolder
        }
    }

}