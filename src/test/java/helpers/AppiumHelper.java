package helpers;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppiumHelper {

    protected AppiumDriver driver;
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public byte[] takeScreenshot() {
        return driver.getScreenshotAs(OutputType.BYTES);
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