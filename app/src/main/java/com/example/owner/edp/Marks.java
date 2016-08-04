package com.example.owner.edp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Krishna N. Deoram on 2016-03-18.
 * Gumi is love. Gumi is life.
 */
public class Marks implements Parcelable {

    private int id;
    private int mark;
    private String updatedLast;
    private String updatedBy;
    private String course;
    private int section;
    private String firstName;
    private String lastName;

    public Marks(int id, int mark, String updatedLast, String updatedBy, String course, int section, String firstName, String lastName) {
        this.id = id;
        this.mark = mark;
        this.updatedLast = updatedLast;
        this.updatedBy = updatedBy;
        this.course = course;
        this.section = section;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected Marks(Parcel in) {
        id = in.readInt();
        mark = in.readInt();
        updatedLast = in.readString();
        updatedBy = in.readString();
        course = in.readString();
        section = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<Marks> CREATOR = new Creator<Marks>() {
        @Override
        public Marks createFromParcel(Parcel in) {
            return new Marks(in);
        }

        @Override
        public Marks[] newArray(int size) {
            return new Marks[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getUpdatedLast() {
        return updatedLast;
    }

    public void setUpdatedLast(String updatedLast) {
        this.updatedLast = updatedLast;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return (this.getId() + " " + this.getCourse() + " " + this.getSection() + " " + this.getMark() + " " + this.getUpdatedBy() + " " + this.getUpdatedLast());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mark);
        dest.writeString(updatedLast);
        dest.writeString(updatedBy);
        dest.writeString(course);
        dest.writeInt(section);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
