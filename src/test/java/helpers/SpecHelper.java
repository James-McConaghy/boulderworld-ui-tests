package helpers;

import com.thoughtworks.gauge.*;

public class SpecHelper extends AppiumHelper {

    private static AppiumFactory appiumFactory;

    @BeforeSuite
    public void initialize() {
        appiumFactory = AppiumFactory.getInstance();
        appiumFactory.createAppiumProcess();
        appiumFactory.bootAndroidDevice();
    }

    @BeforeSpec
    public void setup() {
        driver = appiumFactory.connect();
    }


    @BeforeScenario
    public void resetApp() {
        if(System.getenv("device_platform").equals("Android")){
            uninstall();
            install();
        }
    }

    @AfterScenario
    public void afterScenario() {
        if (driver!= null) {
            driver.closeApp();
        }
    }

    @AfterSpec
    public void teardown() {
        if (driver!= null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void afterSuite() {
        appiumFactory.killAppiumProcess();
        appiumFactory.killDeviceProcess();
    }

}

