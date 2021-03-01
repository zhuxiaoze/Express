package bean;

import dao.Location;

import java.io.Serializable;
import java.util.Objects;

public class Express implements Comparable<Express>,Serializable {
    private int code;
    private int number;
    private String Company;
    private Location location;

    public Express() {
    }

    public Express(int number, String company) {
        this.number = number;
        Company = company;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Express express = (Express) o;
        return code == express.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(Express o) {
        return Integer.compare(this.number, o.number);
    }

//    @Override
//    public String toString() {
//        return "Express{" +
//                "取件码:" + code +
//                ", 单号:" + number +
//                ", 公司:" + Company +
//                ", location:" + location +
//                '}';
//    }


    @Override
    public String toString() {
        return "Express{" +
                "code=" + code +
                ", number=" + number +
                ", Company='" + Company + '\'' +
                ", location=" + location +
                '}' + "\n";
    }
}
