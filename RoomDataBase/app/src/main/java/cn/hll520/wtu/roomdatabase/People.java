package cn.hll520.wtu.roomdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "People")
public class People {
    //主键自增加
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "name")
    private String name = "";
    @ColumnInfo(name = "college")
    private String college = "未设置";


    public People() {
    }



    public People(String name, String college) {
        this.name = name;
        this.college = college;
    }


    int get_id() {
        return _id;
    }

    void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getCollege() {
        return college;
    }

    void setCollege(String college) {
        this.college = college;
    }

}
