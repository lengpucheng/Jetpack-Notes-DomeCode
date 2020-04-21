package cn.hll520.wtu.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//数据仓库
class PeopleRepository {
    private LiveData<List<People>> allPeos;
    private PeopleDao peopleDao;

    PeopleRepository(Context context) {
        PeopleDatabase peopleDatabase = PeopleDatabase.getDatabase(context.getApplicationContext());
        peopleDao = peopleDatabase.getPeoDao();
        allPeos = peopleDao.getPeoAll();
    }

    LiveData<List<People>> getAllPeos() {
        return allPeos;
    }

    LiveData<List<People>> findPeosForname(String name) {
        return peopleDao.findPeo("%" + name + "%");
    }
    void insertPeo(People... people) {
        new InsertAsyncTask(peopleDao).execute(people);
    }

    void delPeo(People... people) {
        new DeleteAsyncTask(peopleDao).execute(people);
    }

    void updatePeo(People... people) {
        new UpdateAsyncTask(peopleDao).execute(people);
    }

    void delAll() {
        new DelAllAsyncTask(peopleDao).execute();
    }

    //一个异步线程  三个参数  对象，进度，结果
    public static class InsertAsyncTask extends AsyncTask<People, Void, Void> {
        private PeopleDao peoDao;

        InsertAsyncTask(PeopleDao peoDao) {
            this.peoDao = peoDao;
        }

        @Override
        protected Void doInBackground(People... peoples) {
            peoDao.insertPeo(peoples);
            return null;
        }
    }


    static class UpdateAsyncTask extends AsyncTask<People, Void, Void> {
        private PeopleDao peoDao;

        UpdateAsyncTask(PeopleDao peoDao) {
            this.peoDao = peoDao;
        }

        @Override
        protected Void doInBackground(People... peoples) {
            peoDao.updatePeo(peoples);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<People, Void, Void> {
        private PeopleDao peoDao;

        DeleteAsyncTask(PeopleDao peoDao) {
            this.peoDao = peoDao;
        }

        @Override
        protected Void doInBackground(People... peoples) {
            peoDao.deletePeo(peoples);
            return null;
        }
    }

    //void即缺省
    static class DelAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private PeopleDao peoDao;

        DelAllAsyncTask(PeopleDao peoDao) {
            this.peoDao = peoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            peoDao.delAllPeo();
            return null;
        }
    }

}
