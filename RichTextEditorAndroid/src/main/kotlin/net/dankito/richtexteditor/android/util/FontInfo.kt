package net.dankito.richtexteditor.android.util


class FontInfo(val fontName: String) {

    var bestAliasPick: String? = null

    private val aliasNames = ArrayList<String>()


    fun addAliasName(aliasName: String) {
        aliasNames.add(aliasName)
    }

    fun getAliasNames(): List<String> {
        return aliasNames
    }

}