package helpers;

import com.thoughtworks.gauge.*;

public class SpecHelper extends AppiumHelper {

    private static AppiumFactory appiumFactory;

    @BeforeSuite
    public void initialize() {
        appiumFactory = AppiumFactory.getInstance();
        appiumFactory.createAppiumProcess();
    }

    @BeforeSpec
    public void setup() {
        driver = appiumFactory.connect();
    }


    @BeforeScenario
    public void resetApp() {
        if(System.getenv("device_platform").equals("Android")){
            //reset
        }
    }

    @AfterScenario
    public void afterScenario() {
        driver.closeApp();
    }

    @AfterSpec
    public void teardown() {
        driver.quit();
    }

    @AfterSuite
    public void afterSuite() {
        appiumFactory.killAppiumProcess();
    }

}

