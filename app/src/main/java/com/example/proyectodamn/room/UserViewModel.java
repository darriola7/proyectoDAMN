package com.example.proyectodamn.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<UserTable> mUser;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mUser = mRepository.getAll();
    }

    public LiveData<UserTable> getUser() {
        return mUser;
    }

    public void insert(UserTable user) {
        mRepository.insert(user);
    }

}
