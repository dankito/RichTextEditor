package net.dankito.richtexteditor.java.fx

import net.dankito.richtexteditor.Icon


class JavaFXIcon(val url: String) : Icon() {

    companion object {

        fun fromResourceName(iconResourceName: String): JavaFXIcon {
            return JavaFXIcon(javaClass.classLoader.getResource("icons/" + iconResourceName).toExternalForm())
        }

    }

}