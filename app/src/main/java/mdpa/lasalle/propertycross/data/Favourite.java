package mdpa.lasalle.propertycross.data;

import java.util.ArrayList;

/**
 * Created by Pol on 08/05/2017.
 */

public class Favourite {
    private String _id;
    private String name;
    private String address;
    private int price;
    private int area;
    private ArrayList<String> images;

    public Favourite(String _id, String name, String address, int price, int area, ArrayList<String> images) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.area = area;
        this.images = images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
