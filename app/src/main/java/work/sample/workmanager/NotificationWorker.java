package work.sample.workmanager;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;

import static work.sample.workmanager.Constants.NOTIFICATION_MSG;

public class NotificationWorker extends Worker {
    @NonNull
    @Override
    public WorkerResult doWork() {
        Context applicationContext = getApplicationContext();
        WorkerUtils.sleep();
        WorkerUtils.makeStatusNotification( getInputData().getString(NOTIFICATION_MSG,"default"), applicationContext);
        return WorkerResult.SUCCESS;
    }
}
