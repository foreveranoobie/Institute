package entity;

import java.util.ArrayList;
import java.util.List;

public class Image {
    private String name;
    private List<Attribute> attributeList;

    public Image(List<Attribute> attributeList) {
        this.attributeList = new ArrayList<>(attributeList);
    }

    public Image(String name, List<Attribute> attributeList) {
        this.name = name;
        this.attributeList = new ArrayList<>(attributeList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
}
