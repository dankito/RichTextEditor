package net.dankito.richtexteditor.android.util

import org.slf4j.LoggerFactory
import java.io.File


class SystemFontsParser {

    companion object {
        private val log = LoggerFactory.getLogger(SystemFontsParser::class.java)
    }


    // TODO: for a real parsing of file see https://stackoverflow.com/a/29533686
    fun parseSystemFonts(): List<String> {
        val fontNames = ArrayList<String>()

        fontNames.addAll(parseFontNamesFromFontsXml()) // newer Androids

        fontNames.addAll(parseFontNamesFromSystemFontsXml()) // older systems

        return fontNames
    }


    private fun parseFontNamesFromFontsXml(): Collection<String> {
        val fontNames = ArrayList<String>()

        try {
            val fontsFile = File("/system/etc/fonts.xml")
            if(fontsFile.exists()) {
                fontsFile.forEachLine { line ->
                    tryToParseFontsXmlLineToFontName(line)?.let { fontName ->
                        fontNames.add(fontName)
                    }
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/fonts.xml", e) }

        return fontNames
    }

    private fun tryToParseFontsXmlLineToFontName(line: String): String? {
        try {
            if(line.trim().startsWith("<family name=\"")) {
                var fontName = line.substring(line.indexOf("<family name=\"") + "<family name=\"".length)
                fontName = fontName.substring(0, fontName.indexOf('\"'))

                return fontName
            }
        } catch (e: Exception) {
            log.error("Could not parse line $line", e)
        }

        return null
    }


    private fun parseFontNamesFromSystemFontsXml(): Collection<String> {
        val fontNames = ArrayList<String>()

        try {
            val fontsFile = File("/system/etc/system_fonts.xml")
            if(fontsFile.exists()) {
                val lines = fontsFile.readLines()
                for(i in 0..lines.size - 1) {
                    tryToParseSystemFontsXmlLineToFontName(lines, i)?.let { fontName ->
                        fontNames.add(fontName)
                    }
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/system_fonts.xml", e) }

        return fontNames
    }

    private fun tryToParseSystemFontsXmlLineToFontName(lines: List<String>, index: Int): String? {
        try {
            val line = lines[index]
            if(line.trim() == "<nameset>") {
                val firstLineInNameSet = lines[index + 1]
                var fontName = firstLineInNameSet.substring(firstLineInNameSet.indexOf("<name>") + "<name>".length)
                fontName = fontName.substring(0, fontName.indexOf("</name>"))

                return fontName
            }
        } catch (e: Exception) {
            log.error("Could not parse line", e)
        }

        return null
    }

}