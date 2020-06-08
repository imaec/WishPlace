package com.imaec.wishplace

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun multipleSpannableTest() {
        val content = "123456781234sdafasdf123asdfgasdfasdfk12sadfasdfjkhnjkhn123gfaedfasd"
        val keyword = "123"
        var lastIndex = 0

        while (true) {
            if (content.indexOf(keyword, lastIndex) == -1) break

            val start = content.indexOf(keyword, lastIndex)
            val end = start + keyword.length

            println("$start / $end")
            println(content.substring(start, end))

            lastIndex = end
        }

        assert(true)
    }

    @Test
    fun varargTest() {
        vararg1("a", "b", "c", "d", "e", "f", "g")
    }

    private fun vararg1(vararg str: String) {
        vararg2(*str)
    }

    private fun vararg2(vararg str: String) {
        str.forEach {
            println(it)
        }
    }
}
