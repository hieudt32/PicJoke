package funia.maker.pip.picjoke.scopic.model;


public class PicJoke {
    private String name;
    private int image;
    private int thumb;

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public PicJoke(String name, int image, int thumb) {

        this.name = name;
        this.image = image;
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public PicJoke(String name, int image) {

        this.name = name;
        this.image = image;
    }
}
