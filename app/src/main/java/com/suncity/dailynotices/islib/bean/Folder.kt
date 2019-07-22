package com.suncity.dailynotices.islib.bean


import java.io.Serializable

class Folder : Serializable {

    var name: String = ""
    var path: String = ""
    var cover: Image? = null
    var images: ArrayList<Image>? = null
    override fun equals(o: Any?): Boolean {
        try {
            val other = o as Folder?

            return this.path.equals(other?.path ?: "", ignoreCase = true)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return super.equals(o)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (cover?.hashCode() ?: 0)
        result = 31 * result + (images?.hashCode() ?: 0)
        return result
    }
}