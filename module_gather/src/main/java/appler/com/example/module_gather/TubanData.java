package appler.com.example.module_gather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 采集数据 实体类
 * */

public class TubanData implements Parcelable {

    public String gatherNumber;
    public String gatherName;
    public String gatherPolygon;
    public String gatherRemark;
    public String gatherDate;
    public String gatherPerson;

    protected TubanData(Parcel in) {
        gatherNumber = in.readString();
        gatherName = in.readString();
        gatherPolygon = in.readString();
        gatherRemark = in.readString();
        gatherDate = in.readString();
        gatherPerson = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gatherNumber);
        dest.writeString(gatherName);
        dest.writeString(gatherPolygon);
        dest.writeString(gatherRemark);
        dest.writeString(gatherDate);
        dest.writeString(gatherPerson);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TubanData> CREATOR = new Creator<TubanData>() {
        @Override
        public TubanData createFromParcel(Parcel in) {
            return new TubanData(in);
        }

        @Override
        public TubanData[] newArray(int size) {
            return new TubanData[size];
        }
    };

    @Override
    public String toString() {
        return "TubanData{" +
                "gatherNumber='" + gatherNumber + '\'' +
                ", gatherName='" + gatherName + '\'' +
                ", gatherPolygon='" + gatherPolygon + '\'' +
                ", gatherRemark='" + gatherRemark + '\'' +
                ", gatherDate='" + gatherDate + '\'' +
                ", gatherPerson='" + gatherPerson + '\'' +
                '}';
    }

    public TubanData() {
    }

    public TubanData(String gatherNumber, String gatherName, String gatherPolygon, String gatherRemark, String gatherDate, String gatherPerson) {
        this.gatherNumber = gatherNumber;
        this.gatherName = gatherName;
        this.gatherPolygon = gatherPolygon;
        this.gatherRemark = gatherRemark;
        this.gatherDate = gatherDate;
        this.gatherPerson = gatherPerson;
    }

    public String getGatherNumber() {
        return gatherNumber;
    }

    public void setGatherNumber(String gatherNumber) {
        this.gatherNumber = gatherNumber;
    }

    public String getGatherName() {
        return gatherName;
    }

    public void setGatherName(String gatherName) {
        this.gatherName = gatherName;
    }

    public String getGatherPolygon() {
        return gatherPolygon;
    }

    public void setGatherPolygon(String gatherPolygon) {
        this.gatherPolygon = gatherPolygon;
    }

    public String getGatherRemark() {
        return gatherRemark;
    }

    public void setGatherRemark(String gatherRemark) {
        this.gatherRemark = gatherRemark;
    }

    public String getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }

    public String getGatherPerson() {
        return gatherPerson;
    }

    public void setGatherPerson(String gatherPerson) {
        this.gatherPerson = gatherPerson;
    }
}
