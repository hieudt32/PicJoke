package funia.maker.pip.picjoke.scopic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by st on 6/6/17.
 */

public class Category implements Serializable {
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Lock")
    @Expose
    private boolean lock;

    public Category(String id, String name, boolean lock)
    {
        this.id = id;
        this.name = name;
        this.lock = lock;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getLock() {
        return this.lock;
    }
}
