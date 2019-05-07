import com.thoughtworks.gauge.Step;

import static helpers.AppiumHelper.*;

public class AppSteps {

    @Step("App: Launch")
    public void launch() {
        launchApp();

    }

    @Step("App: Close")
    public void close() {
        closeApp();
    }

}
