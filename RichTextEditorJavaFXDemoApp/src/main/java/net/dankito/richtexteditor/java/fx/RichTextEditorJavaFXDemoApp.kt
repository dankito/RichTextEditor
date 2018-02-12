package net.dankito.richtexteditor.java.fx

import javafx.application.Application
import javafx.stage.Stage
import net.dankito.richtexteditor.java.fx.localization.UTF8ResourceBundleControl
import net.dankito.richtexteditor.java.fx.window.main.MainWindow
import tornadofx.*
import java.util.*

class RichTextEditorJavaFXDemoApp : App(MainWindow::class) {


    override fun start(stage: Stage) {
        setupMessagesResources() // has to be done before creating / injecting first instances as some of them already rely on Messages (e.g. CalculatedTags)

        super.start(stage)
    }


    private fun setupMessagesResources() {
        ResourceBundle.clearCache() // at this point default ResourceBundles are already created and cached. In order that ResourceBundle created below takes effect cache has to be clearedbefore
        FX.messages = ResourceBundle.getBundle("Editor_Messages", UTF8ResourceBundleControl())
    }


    @Throws(Exception::class)
    override fun stop() {
        super.stop()
        System.exit(0) // otherwise Window would be closed but application still running in background
    }

}



fun main(args: Array<String>) {
    Application.launch(RichTextEditorJavaFXDemoApp::class.java, *args)
}
