package helpers;

import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.thoughtworks.gauge.screenshot.ICustomScreenshotGrabber;
import helpers.models.Locator;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AppiumHelper implements ICustomScreenshotGrabber {

    protected static AppiumDriver driver;
    public static DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
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

    public static List<WebElement> locateAll(Locator locator) {
        return driver.findElements(locator.getBy(), locator.getValue());
    }

    public static void waitWhileVisible(Locator locator, int waitTimeSeconds ) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeSeconds);
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(locate(locator))));
        } catch (StaleElementReferenceException e) {
            log.info("Element {} no longer visible", locator.getValue());
        }
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
