package helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class AppiumFactory {

    private static AppiumFactory instance = null;
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private Process appiumProcess;

    public static AppiumFactory getInstance() {
        if(instance == null) {
            instance = new AppiumFactory();
        }
        return instance;
    }

    public AppiumDriver connect() {
        try {
            String appiumURL = "http://%s:%s/wd/hub";
            String appiumHost = System.getenv("appium_host");
            String appiumPort = System.getenv("appium_port");
            AppiumDriver driver = new AppiumDriver(new URL(String.format(appiumURL, appiumHost, appiumPort)), getDesiredCapabilities());
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            return driver;
        } catch (Exception e) {
            log.error("Unable to connect to device, appium logs would be useful here. Check desired capabilities matches available devices");
            log.error(e.toString());
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

    public void createAppiumProcess() {
        log.info("Starting appium..");
        String[] command = {"appium", "-p", System.getenv("appium_port"), "-a", System.getenv("appium_host"), "--log-level", "debug"};
        try {
            appiumProcess = new ProcessBuilder(command).start();
            final Thread ioThread = new Thread() {
                @Override
                public void run() {
                    try {
                        final BufferedReader reader = new BufferedReader(
                                new InputStreamReader(appiumProcess.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                        reader.close();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ioThread.start();

            sleep(10000);
        } catch (Exception e) {
            log.error("Ensure appium is installed on the host via terminal");
            throw new AppiumServerHasNotBeenStartedLocallyException(e.toString());
        }
    }

    public void killAppiumProcess() {
        appiumProcess.destroy();
    }

}
