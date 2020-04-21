package cn.hll520.wtu.roomdatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PeopleDao {

    @Insert
    void insertPeo(People... people);

    @Update
    void updatePeo(People... people);

    @Delete
    void deletePeo(People... people);

//    @Query("SELECT * FROM People")
//    List<People> getPeoAll();


    @Query("SELECT * FROM People")
    LiveData<List<People>> getPeoAll();

    @Query("DELETE FROM People")
    void delAllPeo();

    //条件查询 ：后接参数
    @Query("SELECT * FROM People WHERE name LIKE :peoname ORDER BY _ID DESC")
    LiveData<List<People>> findPeo(String peoname);
}
