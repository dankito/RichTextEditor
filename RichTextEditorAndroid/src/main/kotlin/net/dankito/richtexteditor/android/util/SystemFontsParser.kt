package net.dankito.richtexteditor.android.util

import org.slf4j.LoggerFactory
import java.io.File


class SystemFontsParser {

    companion object {
        private val log = LoggerFactory.getLogger(SystemFontsParser::class.java)
    }


    // TODO: for a real parsing of file see https://stackoverflow.com/a/29533686
    fun parseSystemFonts(): List<FontInfo> {
        val fontInfos = ArrayList<FontInfo>()

        fontInfos.addAll(parseFontInfosFromFontsXml()) // newer Androids

        fontInfos.addAll(parseFontInfosFromSystemFontsXml()) // older systems

        determineBestPickForAliasNames(fontInfos)

        return fontInfos
    }


    private fun parseFontInfosFromFontsXml(): Collection<FontInfo> {
        val fontInfoMap = HashMap<String, FontInfo>()

        try {
            val fontsFile = File("/system/etc/fonts.xml")
            if(fontsFile.exists()) {
                fontsFile.forEachLine { line ->
                    tryToParseFontsXmlLineToFontName(line)?.let { fontName ->
                        fontInfoMap.put(fontName, FontInfo(fontName))
                    }

                    tryToParseFontsXmlAlias(line, fontInfoMap)
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/fonts.xml", e) }

        return fontInfoMap.values
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

    private fun tryToParseFontsXmlAlias(line: String, fontInfoMap: HashMap<String, FontInfo>) {
        try {
            if(line.trim().startsWith("<alias name=\"")) {
                var aliasName = line.substring(line.indexOf("<alias name=\"") + "<alias name=\"".length)
                aliasName = aliasName.substring(0, aliasName.indexOf('\"'))

                var fontName = line.substring(line.indexOf("to=\"") + "to=\"".length)
                fontName = fontName.substring(0, fontName.indexOf('\"'))

                fontInfoMap[fontName]?.let { fontInfo ->
                    fontInfo.addAliasName(aliasName)
                }
            }
        } catch(e: Exception) { log.error("Could not parse line $line to font name alias", e) }
    }


    private fun parseFontInfosFromSystemFontsXml(): Collection<FontInfo> {
        val fontInfos = ArrayList<FontInfo>()

        try {
            val fontsFile = File("/system/etc/system_fonts.xml")
            if(fontsFile.exists()) {
                val lines = fontsFile.readLines()
                for(i in 0..lines.size - 1) {
                    tryToParseSystemFontsXmlLineToFontInfo(lines, i)?.let { fontInfo ->
                        fontInfos.add(fontInfo)
                    }
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/system_fonts.xml", e) }

        return fontInfos
    }

    private fun tryToParseSystemFontsXmlLineToFontInfo(lines: List<String>, index: Int): FontInfo? {
        try {
            val line = lines[index]
            if(line.trim() == "<nameset>") {
                val fontNameIndex = index + 1

                tryToParseSystemFontsXmlLineToFontName(lines, fontNameIndex)?.let { fontName ->
                    val fontInfo = FontInfo(fontName)

                    parseSystemFontsXmlAliases(fontInfo, lines, fontNameIndex)

                    return fontInfo
                }
            }
        } catch (e: Exception) {
            log.error("Could not parse line", e)
        }

        return null
    }

    private fun parseSystemFontsXmlAliases(fontInfo: FontInfo, lines: List<String>, fontNameIndex: Int) {
        var nextIndex = fontNameIndex
        var nextFontName: String?

        do {
            nextFontName = tryToParseSystemFontsXmlLineToFontName(lines, ++nextIndex)

            nextFontName?.let { fontInfo.addAliasName(it) }
        } while (nextFontName != null)
    }

    private fun tryToParseSystemFontsXmlLineToFontName(lines: List<String>, index: Int): String? {
        try {
            val line = lines[index]

            if(line.trim().startsWith("<name>")) {
                var fontName = line.substring(line.indexOf("<name>") + "<name>".length)
                fontName = fontName.substring(0, fontName.indexOf("</name>"))

                return fontName
            }
        } catch(e: Exception) { log.error("Could not parse line for index $index", e) }

        return null
    }


    private fun determineBestPickForAliasNames(fontInfos: ArrayList<FontInfo>) {
        fontInfos.forEach { fontInfo ->
            when(fontInfo.fontName.toLowerCase()) {
                "sans-serif" -> getBestAlias(fontInfo, "Arial", "Helvetica", "Tahoma", "Verdana")
                "serif" -> getBestAlias(fontInfo, "Times new roman", "Palatino", "Georgia", "Times")
                "monospace" -> getBestAlias(fontInfo, "Courier New", "Courier", "Monaco")
            }
        }
    }

    private fun getBestAlias(fontInfo: FontInfo, vararg possibleAliases: String) {
        val lowerCaseAliases = fontInfo.getAliasNames().map { it.toLowerCase() }

        for(possibleAlias in possibleAliases) {
            if(lowerCaseAliases.contains(possibleAlias.toLowerCase())) {
                fontInfo.bestAliasPick = possibleAlias
                break
            }
        }
    }

}