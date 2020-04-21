package cn.hll520.wtu.roomdatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PeopleViewModel extends AndroidViewModel {
    private PeopleRepository repository;

    public PeopleViewModel(@NonNull Application application) {
        super(application);
        repository = new PeopleRepository(application);
    }

    LiveData<List<People>> getAllPeos() {
        return repository.getAllPeos();
    }

    LiveData<List<People>> findPeosForName(String name) {
        return repository.findPeosForname(name);
    }
    void insertPeo(People... people) {
        repository.insertPeo(people);
    }

    void delPeo(People... people) {
        repository.delPeo(people);
    }

    void updatePeo(People... people) {
        repository.updatePeo(people);
    }

    void delAll() {
        repository.delAll();
    }


}
