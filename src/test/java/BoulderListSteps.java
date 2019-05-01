import com.thoughtworks.gauge.Step;
import helpers.models.Locator;

import static helpers.AppiumHelper.*;

public class BoulderListSteps {

    private Locator fragmentTitle = new Locator("xpath", "//*[@resource-id='boulderworld:id/text_title' and @text='%{0}%']");
    private Locator boulderRow = new Locator("xpath", "//*[@resource-id='boulderworld:id/row_item']//android.widget.TextView[@text='%{0}%']");

    @Step("Boulder List: Ready?")
    public void ready() {
        locate(fragmentTitle, "Boulders");
        locate(boulderRow, "Boulder 1");
        locate(boulderRow, "Boulder 2");
    }

    @Step("Boulder List: Tap on Boulder <boulderItem>")
    public void tapOnBoulder(String boulderItem) {
        locate(boulderRow, boulderItem).click();
    }

}
