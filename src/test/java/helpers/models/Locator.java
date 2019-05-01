package helpers.models;

public class Locator {

    String by;
    String value;

    public Locator(String by, String value) {
        this.by = by;
        this.value = value;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
