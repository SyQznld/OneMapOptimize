package com.oneMap.module.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GatherData implements Parcelable{

    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    private String gatherName;
    private String gatherPolygon;
    private String gatherRemark;
    private String gatherTime;
    private String gatherPeople;
    private String gatherPhotos;
    @Generated(hash = 624445771)
    public GatherData(Long Id, String gatherName, String gatherPolygon,
            String gatherRemark, String gatherTime, String gatherPeople,
            String gatherPhotos) {
        this.Id = Id;
        this.gatherName = gatherName;
        this.gatherPolygon = gatherPolygon;
        this.gatherRemark = gatherRemark;
        this.gatherTime = gatherTime;
        this.gatherPeople = gatherPeople;
        this.gatherPhotos = gatherPhotos;
    }
    @Generated(hash = 201137975)
    public GatherData() {
    }

    protected GatherData(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readLong();
        }
        gatherName = in.readString();
        gatherPolygon = in.readString();
        gatherRemark = in.readString();
        gatherTime = in.readString();
        gatherPeople = in.readString();
        gatherPhotos = in.readString();
    }

    public static final Creator<GatherData> CREATOR = new Creator<GatherData>() {
        @Override
        public GatherData createFromParcel(Parcel in) {
            return new GatherData(in);
        }

        @Override
        public GatherData[] newArray(int size) {
            return new GatherData[size];
        }
    };

    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getGatherName() {
        return this.gatherName;
    }
    public void setGatherName(String gatherName) {
        this.gatherName = gatherName;
    }
    public String getGatherPolygon() {
        return this.gatherPolygon;
    }
    public void setGatherPolygon(String gatherPolygon) {
        this.gatherPolygon = gatherPolygon;
    }
    public String getGatherRemark() {
        return this.gatherRemark;
    }
    public void setGatherRemark(String gatherRemark) {
        this.gatherRemark = gatherRemark;
    }
    public String getGatherTime() {
        return this.gatherTime;
    }
    public void setGatherTime(String gatherTime) {
        this.gatherTime = gatherTime;
    }
    public String getGatherPeople() {
        return this.gatherPeople;
    }
    public void setGatherPeople(String gatherPeople) {
        this.gatherPeople = gatherPeople;
    }
    public String getGatherPhotos() {
        return this.gatherPhotos;
    }
    public void setGatherPhotos(String gatherPhotos) {
        this.gatherPhotos = gatherPhotos;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Id);
        }
        dest.writeString(gatherName);
        dest.writeString(gatherPolygon);
        dest.writeString(gatherRemark);
        dest.writeString(gatherTime);
        dest.writeString(gatherPeople);
        dest.writeString(gatherPhotos);
    }


    @Override
    public String toString() {
        return "GatherData{" +
                "Id=" + Id +
                ", gatherName='" + gatherName + '\'' +
                ", gatherPolygon='" + gatherPolygon + '\'' +
                ", gatherRemark='" + gatherRemark + '\'' +
                ", gatherTime='" + gatherTime + '\'' +
                ", gatherPeople='" + gatherPeople + '\'' +
                ", gatherPhotos='" + gatherPhotos + '\'' +
                '}';
    }
}
