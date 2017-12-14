package funia.maker.pip.picjoke.scopic.model;


public class MoreApp {
    String strThumb;
    String strName;
    String strDescription;
    String strPackageName;

    public MoreApp() {
    }

    public MoreApp(String strThumb, String strName, String strDescription, String packageName) {
        this.strThumb = strThumb;
        this.strName = strName;
        this.strDescription = strDescription;
        this.strPackageName = packageName;
    }

    public String getStrThumb() {
        return strThumb;
    }

    public void setStrThumb(String strThumb) {
        this.strThumb = strThumb;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrPackageName() {
        return strPackageName;
    }

    public void setStrPackageName(String strPackageName) {
        this.strPackageName = strPackageName;
    }
}
