package funia.maker.pip.picjoke.scopic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Rect implements Serializable {

    @SerializedName("left")
    @Expose
    private String left;
    @SerializedName("top")
    @Expose
    private String top;
    @SerializedName("right")
    @Expose
    private String right;
    @SerializedName("bottom")
    @Expose
    private String bottom;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }


    public Rect(String left, String top, String right, String bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
