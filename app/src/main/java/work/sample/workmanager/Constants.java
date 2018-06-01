/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package work.sample.workmanager;

public final class Constants {

    // Notification Channel constants

    // Name of Notification Channel for verbose notifications of background work
    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static final CharSequence NOTIFICATION_TITLE = "WorkRequest Starting";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION";

    // Other keys
    public static final String OUTPUT_PATH = "blur_filter_outputs";
    public static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";
    public static final String NOTIFICATION_MSG = "KEY_IMAGE_URI";
    public static final long DELAY_TIME_MILLIS = 3000;

    // The name of the notification work
    static final String NOTIFICATION_WORK_NAME = "notification_manipulation_work";
    static final String TAG_OUTPUT = "OUTPUT";
    public static int NOTIFICATION_ID = 1;
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";

    // Ensures this class is never instantiated
    private Constants() {
    }
}