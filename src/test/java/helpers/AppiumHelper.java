package helpers;

import helpers.models.Locator;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppiumHelper {

    protected static AppiumDriver driver;
    private static Logger log = LogManager.getLogger(AppiumHelper.class);

    public static WebElement locate(Locator locator, String... replacement){
        String replacedValue = locator.getValue();
        for(int i = 0; i < replacement.length; i++) {
            replacedValue = replacedValue.replace("%{"+i+"}%", replacement[i]);
        }
        return driver.findElement(locator.getBy(), replacedValue);
    }

    public static WebElement locate(Locator locator){
        return driver.findElement(locator.getBy(), locator.getValue());
    }

    public byte[] takeScreenshot() {
        return driver.getScreenshotAs(OutputType.BYTES);
    }


    public static void launchApp() {
        driver.launchApp();
    }

    public static void closeApp() {
        driver.closeApp();
    }

    public void install() {
        log.info("Installing App");
        log.info(System.getenv(System.getenv("device_platform")+"_app_location"));
        driver.installApp(System.getenv(System.getenv("device_platform")+"_app_location"));
    }

    public void uninstall() {
        try {
            log.info("Uninstalling App");
            driver.removeApp(System.getenv(System.getenv("device_platform")+"_app_bundleID"));
        } catch (Exception e) {
            log.error("Unable to uninstall application");
            log.error(e.toString());
        }
    }

}
