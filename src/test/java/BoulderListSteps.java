import com.thoughtworks.gauge.Step;
import helpers.AppiumHelper;
import helpers.models.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static helpers.AppiumHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BoulderListSteps {

    private static Logger log = LogManager.getLogger(BoulderListSteps.class);
    private String STORED_GRADE_FILTERS = "appliedGradeFilters";
    private Locator activityTitle               = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/toolbar']/android.widget.TextView[@text='%{0}%']");
    private Locator filterGradeList             = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/filterRecyclerListView']");
    private Locator filterGradeListOptionText   = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/filterRecyclerListView']/android.widget.Button[@text='%{0}%']");
    private Locator boulderList                 = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/recyclerListView']");
    private Locator boulderListRefresh          = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/swipeRefreshLayout']/android.widget.ImageView");
    private Locator boulderRow                  = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/recyclerListView']/android.view.ViewGroup");
    private Locator boulderRowByGrade           = new Locator("xpath", "//*[@resource-id='"+System.getenv("Android_app_bundleID")+":id/recyclerListView']/android.view.ViewGroup//android.widget.TextView[@text='%{0}%']");
    private String boulderRowGrade = "textBoulderGrade";



    @Step("Boulder List: Ready?")
    public void ready() {
        locate(activityTitle, "Boulder World");
        waitWhileVisible(boulderListRefresh, 10);
        locate(filterGradeList);
        locate(boulderList);
    }

    @Step("Boulder List: Tap on Boulder <boulderItem>")
    public void tapOnBoulder(String boulderItem) {
        locate(boulderRow, boulderItem).click();
    }

    @Step("Boulder List: Apply a <filter> boulder grade filter")
    public void applyBoulderGradeFilter(String filter) {
        ArrayList<String> appliedGradeFilters = new ArrayList<>();

        // todo
        // extract scenario store getters/setters
        try {
            appliedGradeFilters = (ArrayList<String>) scenarioStore.get(STORED_GRADE_FILTERS);
            if (appliedGradeFilters == null) {
                appliedGradeFilters = new ArrayList<>();
            }
        } catch (Exception e) {
            appliedGradeFilters = new ArrayList<>();
        }

        locate(filterGradeListOptionText, filter).click();
        assertThat(locate(filterGradeListOptionText, filter).isSelected()).isTrue();
        appliedGradeFilters.add(filter);
        scenarioStore.put(STORED_GRADE_FILTERS, appliedGradeFilters);
    }

    @Step("Boulder List: Ensure only boulders with the applied boulder grade filters appear in the list")
    public void assertBoulderGradeFilter() {
        ArrayList<String> appliedGradeFilters = (ArrayList<String>) scenarioStore.get(STORED_GRADE_FILTERS);
        List<WebElement> boulders = locateAll(boulderRow);

        for (WebElement boulder : boulders) {
            String boulderText = boulder.findElement(By.id(boulderRowGrade)).getText();
            assertThat(appliedGradeFilters).contains(boulderText);
            log.info("Found {} boulder in list", boulderText);
        }

    }

}
