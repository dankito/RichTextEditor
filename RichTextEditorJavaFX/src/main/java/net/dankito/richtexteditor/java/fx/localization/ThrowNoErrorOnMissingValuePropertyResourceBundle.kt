package net.dankito.richtexteditor.java.fx.localization


class ThrowNoErrorOnMissingValuePropertyResourceBundle(reader: java.io.Reader) : java.util.PropertyResourceBundle(reader) {

    constructor(stream: java.io.InputStream) : this(java.io.InputStreamReader(stream))

    // copied from TornadoFX Messages.kt

    /**
     * Always return true, since we want to supply a default text message instead of throwing exception
     */
    override fun containsKey(key: String) = true

    /**
     * Lookup resource in this bundle. If no value, lookup in parent bundle if defined.
     * If we still have no value, return "[key]" instead of null.
     */
    override fun handleGetObject(key: String?): Any {
        var value = super.handleGetObject(key)

        if (value == null && parent != null) {
            value = parent.getObject(key)
        }

        return value ?: "[$key]"
    }

}