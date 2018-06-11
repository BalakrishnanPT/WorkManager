package work.sample.workmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import androidx.work.Worker;

import static work.sample.workmanager.Constants.NOTIFICATION_MSG;

/**
 * Worker Class that contains Work Logic
 */
public class NotificationWorker extends Worker {
    private static final String BUNDLE_KEY = "String";
    private Messenger mActivityMessenger;
    private String TAG = BUNDLE_KEY;


    @NonNull
    @Override
    public WorkerResult doWork() {
        Context applicationContext = getApplicationContext();
//        mActivityMessenger = getInputData().
        try {
            mActivityMessenger = new Messenger(MainActivity.mHandler);
            sendMessage(1,getInputData().getString(NOTIFICATION_MSG,"default")+"Success1");
            sendMessage(1,getInputData().getString(NOTIFICATION_MSG,"default")+"Success2");
            sendMessage(1,getInputData().getString(NOTIFICATION_MSG,"default")+"Success3");
            sendMessage(1,getInputData().getString(NOTIFICATION_MSG,"default")+"Success4");
            sendMessage(1,getInputData().getString(NOTIFICATION_MSG,"default")+"Success5");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        WorkerUtils.sleep();

        WorkerUtils.makeStatusNotification( getInputData().getString(NOTIFICATION_MSG,"default"), applicationContext);
        return WorkerResult.SUCCESS;
    }

    private void sendMessage(int messageID, @Nullable String params) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = params;
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}
