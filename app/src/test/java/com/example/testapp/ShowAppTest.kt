package com.example.testapp

import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import org.junit.Assert
import org.openqa.selenium.remote.DesiredCapabilities
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File
import java.net.URL

fun removeDuplicate(key: String, s: String): String {
    val idxFirst = key.indexOf(s)
    if (idxFirst != -1) {
        val idxSecond = key.indexOf(s, idxFirst + s.length)
        if (idxSecond != -1) {
            return key.substring(0, idxSecond) +
                    key.substring(idxSecond + s.length)
        }
    }
    return key
}

class ShowAppTest : Spek({
    describe("UI Test") {
        it("should run") {
            val appDir = File("app/build/outputs/apk/debug")
            val app = File(removeDuplicate(appDir.canonicalPath, "app/"), "app-debug.apk")

            val capabilities = DesiredCapabilities()
            capabilities.setCapability("app", app.absolutePath)

            val driver =
                AndroidDriver<MobileElement>(URL("http://127.0.0.1:4723/wd/hub"), capabilities)

            Thread.sleep(1000)

            driver.quit()
        }
    }
})
