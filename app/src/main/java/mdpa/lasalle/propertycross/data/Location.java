package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Location implements Serializable {
    @SerializedName("type") private String type;
    @SerializedName("coordinates") private double coordinates[];

    public Location(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Location{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
