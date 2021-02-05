package com.example.testapp

import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.functions.ExpectedCondition
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.WebDriverWait
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

fun toastMatches(text: String) = ExpectedCondition { driver ->
    println("searching for toast $text")
    try {
        driver?.findElement<WebElement?>(By.xpath("//android.widget.Toast[@text='$text']"))
            ?.let {
                println("toast $text appeared")
                true
            } ?: {
            println("toast $text did not appeared")
            false
        }()
    } catch (e: Throwable) {
        false
    }
}

fun waitForToastShown(driver: AndroidDriver<MobileElement>, text: String, waitSeconds: Long = 25) {
    println("waiting for toast $text")
    val waitAdLoad = WebDriverWait(driver, waitSeconds)
    waitAdLoad.until(toastMatches(text))
}

class ShowAppTest : Spek({
    describe("UI Test") {
        it("should run") {
            val appDir = File("app/build/outputs/apk/debug")
            val app = File(removeDuplicate(appDir.canonicalPath, "app/"), "app-debug.apk")

            val capabilities = DesiredCapabilities()
            capabilities.setCapability("app", app.absolutePath)
            capabilities.setCapability("automationName", "UiAutomator2")

            val driver =
                AndroidDriver<MobileElement>(URL("http://127.0.0.1:4723/wd/hub"), capabilities)

            val button = driver.findElementById("fab")
            button.click()

            waitForToastShown(driver, "AAA")

            driver.quit()
        }
    }
})
