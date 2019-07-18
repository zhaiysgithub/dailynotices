package com.suncity.dailynotices.islib.bean

import java.io.Serializable

/**
 * Image bean
 * Created by Yancy on 2015/12/2.
 */
class Image : Serializable {

    var path: String = ""
    var name: String = ""

    constructor(path: String, name: String) {
        this.path = path
        this.name = name
    }

    constructor()


    override fun equals(o: Any?): Boolean {
        try {
            val other = o as Image?
            return this.path.equals(other?.path, ignoreCase = true)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return super.equals(o)
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}