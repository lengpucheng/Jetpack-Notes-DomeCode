package cn.hll520.wtu.roomdatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {People.class}, version = 18)
public abstract class PeopleDatabase extends RoomDatabase {
    //参数 从版本  A to B 添加字段
    static final Migration MIGRATION_16_17 = new Migration(16, 17) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //添加一列叫 flag 缺省为1  （boolean 在sql中以int存储）
            database.execSQL("ALTER TABLE People ADD COLUMN home TEXT");
        }
    };
    //删除字段
    static final Migration MIGRATION_17_18 = new Migration(17, 18) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //创建临时表
            database.execSQL("CREATE TABLE people_temp(_id INTEGER PRIMARY KEY NOT NULL , name TEXT , college TEXT )");
            //迁移复制数据
            database.execSQL("INSERT INTO people_temp(_id,name,college) " +
                    "SELECT _id,name,college FROM People");
            //删除旧表
            database.execSQL("DROP TABLE People");
            //临时表改名
            database.execSQL("ALTER TABLE people_temp RENAME TO People");
        }
    };
    private static PeopleDatabase INSTANCE;

    static synchronized PeopleDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PeopleDatabase.class, "peo_data")
                    .addMigrations(MIGRATION_17_18)
//                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract PeopleDao getPeoDao();
}

//.fallbackToDestructiveMigration() 强制升级，清空现有全部数据