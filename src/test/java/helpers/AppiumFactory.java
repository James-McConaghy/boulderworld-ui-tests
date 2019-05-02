package helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


public class AppiumFactory {

    private static AppiumFactory instance = null;
    private static Logger log = LogManager.getLogger(AppiumFactory.class);
    private AppiumDriverLocalService service;
    private Process deviceProcess;

    public static AppiumFactory getInstance() {
        if(instance == null) {
            instance = new AppiumFactory();
        }
        return instance;
    }

    public AppiumDriver connect() {
        try {
            if (service != null && service.isRunning()) {
                String appiumURL = "http://%s:%s/wd/hub";
                String appiumHost = System.getenv("appium_host");
                String appiumPort = System.getenv("appium_port");
                //AppiumDriver driver = new AppiumDriver(new URL(String.format(appiumURL, appiumHost, appiumPort)), getDesiredCapabilities());

                AppiumDriver driver = new AppiumDriver(service, getDesiredCapabilities());
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                return driver;
            } else {
                throw new AppiumServerHasNotBeenStartedLocallyException("Appium Server Has not Been Started");
            }
        } catch (Exception e) {
            log.fatal("Unable to connect to device, appium logs would be useful here. Check desired capabilities matches available devices");
            log.fatal(e.toString());
            return null;
        }
    }

    private DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName",    System.getenv(System.getenv("device_platform")+"_appium_automator"));
        capabilities.setCapability("platformName",      System.getenv("device_platform"));
        capabilities.setCapability("platformVersion",   System.getenv(System.getenv("device_platform")+"_device_version"));
        capabilities.setCapability("deviceName",        System.getenv(System.getenv("device_platform")+"_device_name"));
        capabilities.setCapability("app",               System.getenv(System.getenv("device_platform")+"_app_location"));
        capabilities.setCapability("fullReset",         System.getenv(System.getenv("device_platform")+"_appium_reset"));
        capabilities.setCapability("newCommandTimeout", 30);

        if (System.getenv("device_platform").equals("Android")) {
            capabilities.setCapability("noReset",    true);
            capabilities.setCapability("autoLaunch", false);
        } else {
            capabilities.setCapability("udid",               System.getenv(System.getenv("device_platform")+"_device_udid"));
            capabilities.setCapability("updatedWDABundleId", "io.appium.WebDriverAgentRunner");
            capabilities.setCapability("xcodeOrgId",         "x");
            capabilities.setCapability("xcodeSigningId",     "x");
        }

        return capabilities;
    }

    public void bootAndroidDevice() {
        log.info("Booting android device..");
        String[] android = {System.getProperty("user.home") + "/Android/Sdk/emulator/emulator", "-avd", System.getenv(System.getenv("device_platform") + "_device_name")};
        if(System.getenv(System.getenv("device_platform")+"_device_emulated").equals("true")) {
            try {
                deviceProcess = new ProcessBuilder(android).start();
                sleep(25000);
            } catch (Exception e) {
                log.fatal("Ensure android device emulator exists and path is correct case sensitive, `emulator -list-avds`");
                log.error(e.toString());
            }
        }
    }

    public void createAppiumProcess() {
        log.info("Starting appium..");
        try {
            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            builder.withIPAddress(System.getenv("appium_host"));
            builder.usingPort(Integer.valueOf(System.getenv("appium_port")));
            builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
            builder.withArgument(GeneralServerFlag.LOG_LEVEL,"debug");
            service = AppiumDriverLocalService.buildService(builder);
            service.start();
        } catch (Exception e) {
            log.fatal("Ensure appium is installed on the host via terminal");
            throw new AppiumServerHasNotBeenStartedLocallyException(e.toString());
        }
    }

    public void killAppiumProcess() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    public void killDeviceProcess() {
        if (deviceProcess != null) {
            deviceProcess.destroy();
        }
    }

}
