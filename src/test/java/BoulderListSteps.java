import com.thoughtworks.gauge.Step;
import helpers.models.Locator;

import static helpers.AppiumHelper.*;

public class BoulderListSteps {

    private Locator activityTitle = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/toolbar']/android.widget.TextView[@text='%{0}%']");
    private Locator boulderRow = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/row_item']//android.widget.TextView[@text='%{0}%']");

    @Step("Boulder List: Ready?")
    public void ready() {
        locate(activityTitle, "Boulder World");
    }

    @Step("Boulder List: Tap on Boulder <boulderItem>")
    public void tapOnBoulder(String boulderItem) {
        locate(boulderRow, boulderItem).click();
    }

}
