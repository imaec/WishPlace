package com.imaec.wishplace

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.entity.CategoryEntity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val TAG = this::class.java.simpleName

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dao: CategoryDao = AppDatabase.getInstance(context).categoryDao()

        Log.e(TAG, "    ## ${context.packageName}")
        Log.e(TAG, "    ## ${dao.insert(CategoryEntity(category = "테스트"))}")

        assertEquals("com.imaec.wishplace", context.packageName)
    }
}
