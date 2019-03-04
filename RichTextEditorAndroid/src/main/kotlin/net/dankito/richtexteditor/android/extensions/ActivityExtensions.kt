package net.dankito.richtexteditor.android.extensions

import android.app.Activity
import net.dankito.richtexteditor.android.theme.ThemeConstants
import net.dankito.utils.android.extensions.themeName


/**
 * Returns true if attribute [net.dankito.utils.android.R.attr.themeName] is set on theme / style resource and
 * its value equals [net.dankito.richtexteditor.android.theme.ThemeConstants.LightRichTextEditorThemeName].
 */
val Activity.hasLightRichTextEditorTheme: Boolean
    get() {
        return ThemeConstants.LightRichTextEditorThemeName == themeName
    }

/**
 * Returns true if attribute [net.dankito.utils.android.R.attr.themeName] is set on theme / style resource and
 * its value equals [net.dankito.richtexteditor.android.theme.ThemeConstants.DarkRichTextEditorThemeName].
 */
val Activity.hasDarkRichTextEditorTheme: Boolean
    get() {
        return ThemeConstants.DarkRichTextEditorThemeName == themeName
    }