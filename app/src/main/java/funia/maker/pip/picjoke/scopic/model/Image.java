package funia.maker.pip.picjoke.scopic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Image implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("rect")
    @Expose
    private Rect rect;
    @SerializedName("rotate")
    @Expose
    private String rotate;
    @SerializedName("aspectRatioX")
    @Expose
    private String aspectRatioX;
    @SerializedName("aspectRatioY")
    @Expose
    private String aspectRatioY;

    public Image(String id, String name, String url, String thumbnail, String width, String height, Rect rect, String rotate, String aspectRatioX, String aspectRatioY, String textureName, String saturation) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.thumbnail = thumbnail;
        this.width = width;
        this.height = height;
        this.rect = rect;
        this.rotate = rotate;
        this.aspectRatioX = aspectRatioX;
        this.aspectRatioY = aspectRatioY;
        this.textureName = textureName;
        this.saturation = saturation;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    @SerializedName("textureName")
    @Expose
    private String textureName;

    public String getSaturation() {

        return saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    @SerializedName("saturation")
    @Expose
    private String saturation;

    public String getAspectRatioX() {
        return aspectRatioX;
    }

    public void setAspectRatioX(String aspectRatioX) {
        this.aspectRatioX = aspectRatioX;
    }

    public String getAspectRatioY() {
        return aspectRatioY;
    }

    public void setAspectRatioY(String aspectRatioY) {
        this.aspectRatioY = aspectRatioY;
    }

    public String getRotate() {

        return rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

}
