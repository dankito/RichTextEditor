package net.dankito.richtexteditor.java.fx.localization


/**
 * <p>
 *  By default .properties files only supports ISO-8859-1 (Latin-1) as encoding.
 *  To be able to load non Latin-1 characters, a custom ResourceBundle.Control has to be written which reads properties file in UTF-8 encoding.
 * </p>
 */
class UTF8ResourceBundleControl : SettableEncodingResourceBundleControl("UTF-8")