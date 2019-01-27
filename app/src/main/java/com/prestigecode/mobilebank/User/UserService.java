package com.prestigecode.mobilebank.User;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.prestigecode.mobilebank.User.User;

public class UserService extends Service {

    User user;
    int id;

    public UserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //Is user created?
        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT);
        //this collects and sets user as User user
        user = intent.getExtras().getParcelable("User");
        Toast.makeText(this, "User: " + user.getName(), Toast.LENGTH_SHORT);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //
        super.onDestroy();
    }
}
