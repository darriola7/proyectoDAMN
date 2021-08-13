package com.example.proyectodamn.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

class UserRepository {
    private final UserDao mUserDao;
    private final LiveData<UserTable> mUser;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mUser = mUserDao.getAlphabetizedUsers();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<UserTable> getAll() {
        return mUser;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(UserTable word) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(word);
        });
    }
}
