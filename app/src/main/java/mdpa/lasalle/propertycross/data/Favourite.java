package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Favourite implements Serializable{
    @SerializedName("_id") private String _id;
    @SerializedName("name") private String name;
    @SerializedName("address") private String address;
    @SerializedName("price") private int price;
    @SerializedName("m2") private int area;
    @SerializedName("type") private String propertyType;
    @SerializedName("location") private Location location;
    @SerializedName("images") private ArrayList<String> images;

    public Favourite(String _id, String name, String address, int price, int area, String propertyType, Location location, ArrayList<String> images) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.area = area;
        this.propertyType = propertyType;
        this.location = location;
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", images=" + images +
                '}';
    }
}
