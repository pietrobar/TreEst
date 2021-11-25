package com.example.progettotreest;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.progettotreest", appContext.getPackageName());
    }

    @Test
    public void getProfile_isCorrect(){
        //non è un vero test, controllare il risultato del log
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Communication.getLines(appContext, "Cez4i87enqRWx32e");

    }
}