package work.sample.workmanager;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

import static work.sample.workmanager.Constants.NOTIFICATION_MSG;
import static work.sample.workmanager.Constants.NOTIFICATION_WORK_NAME;

public class MainActivity extends AppCompatActivity {

    private WorkManager mWorkManager;
    private TextView status;
    private WorkContinuation continuation;
    private OneTimeWorkRequest notificationWork;
    private Constraints constraints;
    private OneTimeWorkRequest notificationWork2;
    private PeriodicWorkRequest notificationWorkSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWorkManager = WorkManager.getInstance();
        status = findViewById(R.id.status);

        findViewById(R.id.oneTimeRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWork();
            }
        });

        findViewById(R.id.cancelRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationWorkSingle != null) cancelWork(notificationWorkSingle.getId());
            }
        });
        findViewById(R.id.continuationRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContinuationWork();
            }
        });
        findViewById(R.id.cancelcontinuationRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationWork != null)cancelWork(notificationWork.getId());
                if (notificationWork2 != null)cancelWork(notificationWork2.getId());
            }
        });
        findViewById(R.id.cancelAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationWorkSingle != null) cancelWork(notificationWorkSingle.getId());
                if (notificationWork != null)cancelWork(notificationWork.getId());
                if (notificationWork2 != null)cancelWork(notificationWork2.getId());
            }
        });
    }

    /**
     * Observer for the Activity
     * @param id Work Request id
     */
    private void setObserver(UUID id) {

        mWorkManager.getStatusById(id).observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
                try {
                    switch (workStatus.getState()) {
                        case ENQUEUED:
                            status.setText("ENQUEUED");
                            break;
                        case FAILED:
                            status.setText("FAILED");
                            break;
                        case BLOCKED:
                            status.setText("BLOCKED");
                            break;
                        case RUNNING:
                            status.setText("RUNNING");
                            break;
                        case SUCCEEDED:
                            status.setText("SUCCEEDED");
                            break;
                        case CANCELLED:
                            status.setText("CANCELLED");
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * Method to create chain of Work
     * Method contains two simple Work with input data
     */
    void createContinuationWork() {

        OneTimeWorkRequest.Builder notificationWorkBuilder =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .setConstraints(getConstraint());

        notificationWorkBuilder.setInputData(createInputData("continuation work 1 "));
        notificationWork = notificationWorkBuilder.build();

        OneTimeWorkRequest.Builder notificationWorkBuilder2 =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .setConstraints(getConstraint());

        notificationWorkBuilder2.setInputData(createInputData("continuation work 2"));
        notificationWork2 = notificationWorkBuilder2.build();

//        Adding notification Work 1
        continuation = mWorkManager
                .beginUniqueWork(NOTIFICATION_WORK_NAME,ExistingWorkPolicy.KEEP,notificationWork);

//        Adding notification Work 2
        continuation.then(notificationWork2).enqueue();

        continuation.getStatuses().observe(this, new Observer<List<WorkStatus>>() {
            @Override
            public void onChanged(@Nullable List<WorkStatus> workStatuses) {
                String tmp = null;
                for (WorkStatus mWorkStatus : workStatuses) {
                    tmp = mWorkStatus.getTags().toString() + "\t" + mWorkStatus.getState().name() + "\n";
                }
                if (tmp == null) {
                    status.setText("Continuation Work Waiting");
                } else {
                    status.setText(tmp);
                }
            }
        });
    }

    /***
     * Method to create Periodic Work
     * Minimum  Time interval is 900000
     */
    private void createWork() {
        PeriodicWorkRequest.Builder notificationWorkBuilder =
                new PeriodicWorkRequest.Builder(NotificationWorker.class,900000,TimeUnit.SECONDS)
                        .setConstraints(getConstraint());

        notificationWorkBuilder.setInputData(createInputData("Periodic Work Request"));
        notificationWorkSingle = notificationWorkBuilder.build();
        mWorkManager.enqueue(notificationWorkSingle);
        setObserver(notificationWorkSingle.getId());
    }

    /**
     * Method to create Data
     * @param msg String for notification
     * @return Data
     */
    private Data createInputData(String msg) {
        return new Data.Builder().putString(NOTIFICATION_MSG, msg).build();
    }

    /**
     * Cancel the work
     */
    void cancelWork(UUID id) {
        mWorkManager.cancelWorkById(id);
    }

    /**
     * Method to Create Constraint
     * @return Constraints
     */
    public Constraints getConstraint() {
        if (constraints == null)
            constraints = new Constraints.Builder()
                    // For Work that requires Internet Connectivity
                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                // For work that require minimum battery level i.e Rendering video
//                .setRequiresBatteryNotLow(true)
                    .build();
        return constraints;
    }
}
