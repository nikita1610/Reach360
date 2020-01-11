package com.reach360.reach360;

import java.util.Comparator;

import android.graphics.drawable.Drawable;

public class CustomComparator implements Comparator<CustomComparator>,
        Comparable<CustomComparator> {

    String label;
    String name;
    Drawable icon;

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(CustomComparator another) {
// TODO Auto-generated method stub
        return (this.label).compareTo(another.label);
    }

    @Override
    public int compare(CustomComparator lhs, CustomComparator rhs) {
// TODO Auto-generated method stub
        return 0;
    }

}