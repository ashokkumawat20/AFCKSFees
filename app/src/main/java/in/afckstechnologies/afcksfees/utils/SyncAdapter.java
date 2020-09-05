package in.afckstechnologies.afcksfees.utils;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.BatchesForStudentsDAO;
import in.afckstechnologies.afcksfees.provider.StudentContract;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    private Context context;
    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[]{
            StudentContract.Entry._ID,
            StudentContract.Entry.COLUMN_NAME_ENTRY_ID,
            StudentContract.Entry.COLUMN_NAME_SYNC_STATUS};

    private static final String[] PROJECTION_LOC = new String[]{
            StudentContract.StudentLocation._ID,
            StudentContract.StudentLocation.COLUMN_NAME_S_ID,
            StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS};
    private static final String[] PROJECTION_COURSE = new String[]{
            StudentContract.StudentCourse._ID,
            StudentContract.StudentLocation.COLUMN_NAME_S_ID,
            StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS};

    private static final String[] PROJECTION_TEMPLATE = new String[]{
            StudentContract.TrainersTemplate._ID,
            StudentContract.TrainersTemplate.COLUMN_NAME_ENTRY_ID,
            StudentContract.TrainersTemplate.COLUMN_NAME_TEMPLATE_TEXT,
            StudentContract.TrainersTemplate.COLUMN_NAME_SUBJECT,
            StudentContract.TrainersTemplate.COLUMN_NAME_TAG,};
    private static final String[] PROJECTION_BRANCHES = new String[]{
            StudentContract.Branches._ID,
            StudentContract.Branches.COLUMN_NAME_ENTRY_ID,
    };
    private static final String[] PROJECTION_COURSES = new String[]{
            StudentContract.Courses._ID,
            StudentContract.Courses.COLUMN_NAME_ENTRY_ID,
    };

    private static final String[] PROJECTION_COURSES_TYPE = new String[]{
            StudentContract.CourseType._ID,
            StudentContract.CourseType.COLUMN_NAME_ENTRY_ID,
    };

    private static final String[] PROJECTION_DAYPREFRENCE = new String[]{
            StudentContract.DayPrefrence._ID,
            StudentContract.DayPrefrence.COLUMN_NAME_ENTRY_ID,
    };
    private static final String[] PROJECTION_USERSDAYPREFRENCE = new String[]{
            StudentContract.UserDayPrefrence._ID,
            StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID,
            StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS
    };
    private static final String[] PROJECTION_STUDENTBATCHES = new String[]{
            StudentContract.StudentBatchdetails._ID,
            StudentContract.StudentBatchdetails.COLUMN_NAME_SBD_ID,
            StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID,
    };
    private static final String[] PROJECTION_COMINGBATCHES = new String[]{
            StudentContract.ComingBatchdetails._ID,
            StudentContract.ComingBatchdetails.COLUMN_NAME_ENTRY_ID,
    };
    private static final String[] PROJECTION_STUDENTATTENDANCE = new String[]{
            StudentContract.StudentAttendance._ID,
            StudentContract.StudentAttendance.COLUMN_NAME_ENTRY_ID,
    };

    private static final String[] PROJECTION_STUDENTCOMMENTS = new String[]{
            StudentContract.StudentComments._ID,
            StudentContract.StudentComments.COLUMN_NAME_ENTRY_ID,
            StudentContract.StudentComments.COLUMN_NAME_SYNC_STATUS
    };
    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME_ENTRY_ID = 1;
    public static final int COLUMN_NAME_SYNC_STATUS = 2;
    public static final int COLUMN_NAME_LAST_NAME = 3;

    public static final int COLUMN_NAME_S_NO = 1;
    public static final int COLUMN_NAME_SBD_ID = 1;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.context = context;
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, final SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        if (AppStatus.getInstance(context).isOnline()) {
            synchronized (SyncAdapter.class) {
                JSONObject json = new JSONObject();
                try {
                    json.put("device_id", "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WebServiceUtility utility = new WebServiceUtility(context);
             /*   utility.getString(Config.URL_GETALLUSERS, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalStudentData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });
                utility.getString(Config.URL_GETALLBRANCHESDETAILS, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalBranchesData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });

                utility.getString(Config.URL_GETALLCOURSESDETAILS, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalCoursesData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });
                utility.getString(Config.URL_GETALLCOURSESTYPE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalCoursesTypeData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });
*/
               /* utility.getString(Config.URL_GETALLDAYPREFRENCE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalDayPrefrenceData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });*/

                /*utility.getString(Config.URL_GETALLUSERDAYPREFRENCE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalUsersDayPrefrenceData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });*/
               /* utility.getString(Config.URL_GETALLVWLOCATIONSDEMANDEDUSERWISE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalStudentLocData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });

                utility.getString(Config.URL_GETALLVWCOURSEDEMANDEDUSERWISE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalStudentCourseData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });

                utility.getString(Config.URL_GETALLTRAINERSTEMPLATE, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalTrainerTemplateData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });
                utility.getString(Config.URL_GETALLVWDETAILSSTUDENTSINBATCH, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalStudentBatchData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });

               */
        /*utility.getString(Config.URL_GETALLVWVIEWSTUDENTATTENDANCE, json, new WebServiceUtility.VolleyCallback() {
            @Override
            public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                updateLocalStudentAttendanceData(result, syncResult);

            }

            @Override
            public void onRequestError(VolleyError errorMessage) {

            }
        });*/

               /* utility.getString(Config.URL_GETALLSTUDENTCOMMENTS, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalStudentCommentsData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });*/

                utility.getString(Config.URL_GETALLVWBATCHSTARTINGDETAILSF, json, new WebServiceUtility.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) throws OperationApplicationException, RemoteException {
                        updateLocalComingBatchData(result, syncResult);

                    }

                    @Override
                    public void onRequestError(VolleyError errorMessage) {

                    }
                });
            }
        }
        Log.i(TAG, "Network synchronization complete");
    }

    /*private void updateLocalStudentData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<UsersDAO> entries = jsonHelper.usersList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, UsersDAO> entryMap = new HashMap<String, UsersDAO>();
        for (UsersDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.Entry.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String userId;
        int sync_status;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;


            id = c.getInt(COLUMN_ID);
            userId = c.getString(COLUMN_NAME_ENTRY_ID);
            sync_status = c.getInt(COLUMN_NAME_SYNC_STATUS);

            UsersDAO match = entryMap.get(userId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(userId);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.Entry.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(userId))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.Entry.COLUMN_NAME_FIRST_NAME, match.getFirst_name().substring(0, 1).toUpperCase() + match.getFirst_name().substring(1))
                            .withValue(StudentContract.Entry.COLUMN_NAME_LAST_NAME, match.getLast_name().substring(0, 1).toUpperCase() + match.getLast_name().substring(1))
                            .withValue(StudentContract.Entry.COLUMN_NAME_MOBILE_NO, match.getMobile_no())
                            .withValue(StudentContract.Entry.COLUMN_NAME_EMAIL_ID, match.getEmail_id())
                            .withValue(StudentContract.Entry.COLUMN_NAME_GENDER, match.getGender())
                            .withValue(StudentContract.Entry.COLUMN_NAME_FCM_ID, match.getFcm_id())
                            .withValue(StudentContract.Entry.COLUMN_NAME_CREATED_AT, match.getCreated_at())
                            .withValue(StudentContract.Entry.COLUMN_NAME_STATUS, match.getStatus())
                            .withValue(StudentContract.Entry.COLUMN_NAME_NOTES, match.getNotes())
                            .withValue(StudentContract.Entry.COLUMN_NAME_PREFERENCE, match.getPreference())
                            .withValue(StudentContract.Entry.COLUMN_NAME_CALLBACK, match.getCallBack())
                            .withValue(StudentContract.Entry.COLUMN_NAME_ENQNOTES, match.getEnqNotes())
                            .withValue(StudentContract.Entry.COLUMN_NAME_NICK_NAME, match.getNick_name())
                            .withValue(StudentContract.Entry.COLUMN_NAME_NATIONALITY, match.getNationality())
                            .withValue(StudentContract.Entry.COLUMN_NAME_DOB, match.getDOB())
                            .withValue(StudentContract.Entry.COLUMN_NAME_MARITAL_STATUS, match.getMarital_status())
                            .withValue(StudentContract.Entry.COLUMN_NAME_PROFILE_PIC, match.getProfile_pic())
                            .withValue(StudentContract.Entry.COLUMN_NAME_JOB_SEARCH_STATUS, match.getJob_search_status())
                            .withValue(StudentContract.Entry.COLUMN_NAME_JOB_PROGRAM_STATUS, match.getJob_program_status())
                            .withValue(StudentContract.Entry.COLUMN_NAME_CURRENT_CTC, match.getCurrent_ctc())
                            .withValue(StudentContract.Entry.COLUMN_NAME_EXPECTED_FROM_CTC, match.getExpected_from_ctc())
                            .withValue(StudentContract.Entry.COLUMN_NAME_EXPECTED_TO_CTC, match.getExpected_to_ctc())
                            .withValue(StudentContract.Entry.COLUMN_NAME_U_TIMESTAMP, match.getU_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                if (sync_status == 1) {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = StudentContract.Entry.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
        }
        c.close();

        // Add new items
        for (UsersDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.Entry.CONTENT_URI)
                    .withValue(StudentContract.Entry.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.Entry.COLUMN_NAME_FIRST_NAME, e.getFirst_name().substring(0, 1).toUpperCase() + e.getFirst_name().substring(1))
                    .withValue(StudentContract.Entry.COLUMN_NAME_LAST_NAME, e.getLast_name().substring(0, 1).toUpperCase() + e.getLast_name().substring(1))
                    .withValue(StudentContract.Entry.COLUMN_NAME_MOBILE_NO, e.getMobile_no())
                    .withValue(StudentContract.Entry.COLUMN_NAME_EMAIL_ID, e.getEmail_id())
                    .withValue(StudentContract.Entry.COLUMN_NAME_GENDER, e.getGender())
                    .withValue(StudentContract.Entry.COLUMN_NAME_FCM_ID, e.getFcm_id())
                    .withValue(StudentContract.Entry.COLUMN_NAME_CREATED_AT, e.getCreated_at())
                    .withValue(StudentContract.Entry.COLUMN_NAME_STATUS, e.getStatus())
                    .withValue(StudentContract.Entry.COLUMN_NAME_NOTES, e.getNotes())
                    .withValue(StudentContract.Entry.COLUMN_NAME_PREFERENCE, e.getPreference())
                    .withValue(StudentContract.Entry.COLUMN_NAME_CALLBACK, e.getCallBack())
                    .withValue(StudentContract.Entry.COLUMN_NAME_ENQNOTES, e.getEnqNotes())
                    .withValue(StudentContract.Entry.COLUMN_NAME_NICK_NAME, e.getNick_name())
                    .withValue(StudentContract.Entry.COLUMN_NAME_NATIONALITY, e.getNationality())
                    .withValue(StudentContract.Entry.COLUMN_NAME_DOB, e.getDOB())
                    .withValue(StudentContract.Entry.COLUMN_NAME_MARITAL_STATUS, e.getMarital_status())
                    .withValue(StudentContract.Entry.COLUMN_NAME_PROFILE_PIC, e.getProfile_pic())
                    .withValue(StudentContract.Entry.COLUMN_NAME_JOB_SEARCH_STATUS, e.getJob_search_status())
                    .withValue(StudentContract.Entry.COLUMN_NAME_JOB_PROGRAM_STATUS, e.getJob_program_status())
                    .withValue(StudentContract.Entry.COLUMN_NAME_CURRENT_CTC, e.getCurrent_ctc())
                    .withValue(StudentContract.Entry.COLUMN_NAME_EXPECTED_FROM_CTC, e.getExpected_from_ctc())
                    .withValue(StudentContract.Entry.COLUMN_NAME_EXPECTED_TO_CTC, e.getExpected_to_ctc())
                    .withValue(StudentContract.Entry.COLUMN_NAME_U_TIMESTAMP, e.getU_timestamp())
                    .withValue(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, 1)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.Entry.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalStudentLocData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<StCenterDAO> entries = jsonHelper.usersLocList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, StCenterDAO> entryMap = new HashMap<String, StCenterDAO>();
        for (StCenterDAO e : entries) {
            entryMap.put(e.getS_no(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.StudentLocation.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_LOC, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;
        int sync_status;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_S_NO);
            sync_status = c.getInt(COLUMN_NAME_SYNC_STATUS);
            StCenterDAO match = entryMap.get(s_no);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.StudentLocation.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getS_no() != null && match.getS_no().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.StudentLocation.COLUMN_NAME_BRANCH_NAME, match.getBranch_name())
                            .withValue(StudentContract.StudentLocation.COLUMN_NAME_USER_ID, match.getUser_id())

                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                if (sync_status == 1) {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = StudentContract.StudentLocation.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
        }
        c.close();

        // Add new items
        for (StCenterDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.StudentLocation.CONTENT_URI)
                    .withValue(StudentContract.StudentLocation.COLUMN_NAME_S_ID, e.getS_no())
                    .withValue(StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.StudentLocation.COLUMN_NAME_BRANCH_NAME, e.getBranch_name())
                    .withValue(StudentContract.StudentLocation.COLUMN_NAME_USER_ID, e.getUser_id())
                    .withValue(StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS, 1)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.StudentLocation.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalStudentCourseData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<StCoursesDAO> entries = jsonHelper.usersCourseList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, StCoursesDAO> entryMap = new HashMap<String, StCoursesDAO>();
        for (StCoursesDAO e : entries) {
            entryMap.put(e.getS_no(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.StudentCourse.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_COURSE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;
        int sync_status;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_S_NO);
            sync_status = c.getInt(COLUMN_NAME_SYNC_STATUS);
            StCoursesDAO match = entryMap.get(s_no);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.StudentCourse.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getS_no() != null && match.getS_no().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.StudentCourse.COLUMN_NAME_TYPE_ID, match.getType_name_id())
                            .withValue(StudentContract.StudentCourse.COLUMN_NAME_TYPE_NAME, match.getType_name())
                            .withValue(StudentContract.StudentCourse.COLUMN_NAME_COURSE_NAME, match.getCourse_name())
                            .withValue(StudentContract.StudentCourse.COLUMN_NAME_COURSE_CODE, match.getCourse_code())
                            .withValue(StudentContract.StudentCourse.COLUMN_NAME_USER_ID, match.getUser_id())

                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                if(sync_status==1) {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = StudentContract.StudentCourse.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
        }
        c.close();

        // Add new items
        for (StCoursesDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.StudentCourse.CONTENT_URI)
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_S_ID, e.getS_no())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_TYPE_ID, e.getType_name_id())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_TYPE_NAME, e.getType_name())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_COURSE_NAME, e.getCourse_name())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_COURSE_CODE, e.getCourse_code())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_USER_ID, e.getUser_id())
                    .withValue(StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS, 1)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.StudentCourse.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalTrainerTemplateData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<TemplatesContactDAO> entries = jsonHelper.usersTrainerTempList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, TemplatesContactDAO> entryMap = new HashMap<String, TemplatesContactDAO>();
        for (TemplatesContactDAO e : entries) {
            entryMap.put(e.getID(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.TrainersTemplate.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_TEMPLATE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            TemplatesContactDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.TrainersTemplate.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getID() != null && match.getID().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_SUBJECT, match.getSubject())
                            .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_TEMPLATE_TEXT, match.getTemplate_Text())
                            .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_TAG, match.getTag())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.TrainersTemplate.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (TemplatesContactDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.TrainersTemplate.CONTENT_URI)
                    .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_ENTRY_ID, e.getID())
                    .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_SUBJECT, e.getSubject())
                    .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_TEMPLATE_TEXT, e.getTemplate_Text())
                    .withValue(StudentContract.TrainersTemplate.COLUMN_NAME_TAG, e.getTag())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.TrainersTemplate.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalStudentBatchData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<StudentsInBatchListDAO> entries = jsonHelper.studentsList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, StudentsInBatchListDAO> entryMap = new HashMap<String, StudentsInBatchListDAO>();
        for (StudentsInBatchListDAO e : entries) {
            entryMap.put(e.getSbd_id(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.StudentBatchdetails.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_STUDENTBATCHES, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String userId;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;


            id = c.getInt(COLUMN_ID);
            userId = c.getString(COLUMN_NAME_SBD_ID);

            StudentsInBatchListDAO match = entryMap.get(userId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(userId);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.StudentBatchdetails.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getSbd_id() != null && match.getSbd_id().equals(userId))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_FIRST_NAME, match.getFirst_name())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_LAST_NAME, match.getLast_name())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_MOBILE_NO, match.getMobile_no())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_EMAIL_ID, match.getEmail_id())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_GENDER, match.getGender())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_ID, match.getBatch_code())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_CODE, match.getBatch_code())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENT_BATCH_CAT, match.getStudent_batch_cat())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS, match.getStatus())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_PREVIOUS_ATTENDANCE, match.getPrevious_attendance())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_NAME, match.getCourse_name())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_CODE, match.getCourse_code())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_NOTES_ID, match.getNotes_id())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_DISCONTINUE_REASON, match.getDiscontinue_reason())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENTS_NAME, match.getStudent_Name())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_FEES, match.getFees())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BASEFEES, match.getBaseFees())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_DUE_AMOUNT, match.getDue_amount())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_START_DATE, match.getStart_date())
                            .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_SBD_ID, match.getSbd_id())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.StudentBatchdetails.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (StudentsInBatchListDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.StudentBatchdetails.CONTENT_URI)
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_FIRST_NAME, e.getFirst_name())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_LAST_NAME, e.getLast_name())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_MOBILE_NO, e.getMobile_no())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_EMAIL_ID, e.getEmail_id())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_GENDER, e.getGender())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_ID, e.getBatch_code())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_CODE, e.getBatch_code())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENT_BATCH_CAT, e.getStudent_batch_cat())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS, e.getStatus())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_PREVIOUS_ATTENDANCE, e.getPrevious_attendance())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_NAME, e.getCourse_name())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_CODE, e.getCourse_code())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_NOTES_ID, e.getNotes_id())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_DISCONTINUE_REASON, e.getDiscontinue_reason())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENTS_NAME, e.getStudent_Name())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_FEES, e.getFees())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_BASEFEES, e.getBaseFees())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_DUE_AMOUNT, e.getDue_amount())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_START_DATE, e.getStart_date())
                    .withValue(StudentContract.StudentBatchdetails.COLUMN_NAME_SBD_ID, e.getSbd_id())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.StudentBatchdetails.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }
*/
    private void updateLocalComingBatchData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<BatchesForStudentsDAO> entries = jsonHelper.comingBatchList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, BatchesForStudentsDAO> entryMap = new HashMap<String, BatchesForStudentsDAO>();
        for (BatchesForStudentsDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.ComingBatchdetails.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_COMINGBATCHES, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            BatchesForStudentsDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.ComingBatchdetails.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_TYPE, match.getBatchtype())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_NAME, match.getBranch_name())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_NAME, match.getCourse_name())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_DURATION, match.getDuration())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FACULTY_NAME, match.getFaculty_Name())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FEES, match.getFees())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FREQUENCY, match.getFrequency())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_START_DATE, match.getStart_date())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_NOTES, match.getNotes())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TIMINGS, match.getTimings())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_ID, match.getCourse_id())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_END_DATE, match.getBatch_end_date())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COMPLETION_STATUS, match.getCompletion_status())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MAIL_ID, match.getTrainer_mail_id())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MOBILE_NO, match.getTrainer_mobile_no())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_ID, match.getBranch_id())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE, match.getBatch_code())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_ATTENDANCE_MARKED, match.getAttendance_marked())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_REF_BATCH, match.getRef_batch())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_CODE, match.getCode())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_MEETING_LINK, match.getMeeting_link())
                            .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_WA_INVITE_LINK, match.getWa_invite_link())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.ComingBatchdetails.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (BatchesForStudentsDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.ComingBatchdetails.CONTENT_URI)
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_TYPE, e.getBatchtype())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_NAME, e.getBranch_name())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_NAME, e.getCourse_name())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_DURATION, e.getDuration())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FACULTY_NAME, e.getFaculty_Name())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FEES, e.getFees())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_FREQUENCY, e.getFrequency())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_START_DATE, e.getStart_date())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_NOTES, e.getNotes())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TIMINGS, e.getTimings())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_ID, e.getCourse_id())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_END_DATE, e.getBatch_end_date())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_COMPLETION_STATUS, e.getCompletion_status())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MAIL_ID, e.getTrainer_mail_id())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MOBILE_NO, e.getTrainer_mobile_no())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_ID, e.getBranch_id())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE, e.getBatch_code())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_ATTENDANCE_MARKED, e.getAttendance_marked())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_REF_BATCH, e.getRef_batch())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_MEETING_LINK, e.getMeeting_link())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_WA_INVITE_LINK, e.getWa_invite_link())
                    .withValue(StudentContract.ComingBatchdetails.COLUMN_NAME_CODE, e.getCode())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.ComingBatchdetails.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

   /* private void updateLocalStudentAttendanceData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<StudentsAttendanceDetailsDAO> entries = jsonHelper.studentAttendanceList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, StudentsAttendanceDetailsDAO> entryMap = new HashMap<String, StudentsAttendanceDetailsDAO>();
        for (StudentsAttendanceDetailsDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.StudentAttendance.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_STUDENTATTENDANCE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            StudentsAttendanceDetailsDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.StudentAttendance.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.StudentAttendance.COLUMN_NAME_BATCH_ID, match.getBatch_id())
                            .withValue(StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCE, match.getAttendance())
                            .withValue(StudentContract.StudentAttendance.COLUMN_NAME_STUDENT_NAME, match.getStudent_name())
                            .withValue(StudentContract.StudentAttendance.COLUMN_NAME_BATCH_DATE, match.getBatch_date())
                            .withValue(StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCEDATE, match.getAttendanceDate())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.ComingBatchdetails.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (StudentsAttendanceDetailsDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.StudentAttendance.CONTENT_URI)
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_BATCH_ID, e.getBatch_id())
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCE, e.getAttendance())
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_STUDENT_NAME, e.getStudent_name())
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_BATCH_DATE, e.getBatch_date())
                    .withValue(StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCEDATE, e.getAttendanceDate())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.StudentAttendance.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalBranchesData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<CenterDAO> entries = jsonHelper.branchesList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, CenterDAO> entryMap = new HashMap<String, CenterDAO>();
        for (CenterDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.Branches.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_BRANCHES, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            CenterDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.Branches.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.Branches.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.Branches.COLUMN_NAME_BRANCH_NAME, match.getBranch_name())
                            .withValue(StudentContract.Branches.COLUMN_NAME_ADDRESS, match.getAddress())
                            .withValue(StudentContract.Branches.COLUMN_NAME_BRANCH_SHORT, match.getBranch_short())
                            .withValue(StudentContract.Branches.COLUMN_NAME_LATITUDE, match.getLatitude())
                            .withValue(StudentContract.Branches.COLUMN_NAME_LONGITUDE, match.getLongitude())
                            .withValue(StudentContract.Branches.COLUMN_NAME_M_TIMESTAMP, match.getM_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.Branches.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (CenterDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.Branches.CONTENT_URI)
                    .withValue(StudentContract.Branches.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.Branches.COLUMN_NAME_BRANCH_NAME, e.getBranch_name())
                    .withValue(StudentContract.Branches.COLUMN_NAME_ADDRESS, e.getAddress())
                    .withValue(StudentContract.Branches.COLUMN_NAME_BRANCH_SHORT, e.getBranch_short())
                    .withValue(StudentContract.Branches.COLUMN_NAME_LATITUDE, e.getLatitude())
                    .withValue(StudentContract.Branches.COLUMN_NAME_LONGITUDE, e.getLongitude())
                    .withValue(StudentContract.Branches.COLUMN_NAME_M_TIMESTAMP, e.getM_timestamp())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.Branches.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalCoursesData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<CoursesDAO> entries = jsonHelper.coursesList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, CoursesDAO> entryMap = new HashMap<String, CoursesDAO>();
        for (CoursesDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.Courses.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_COURSES, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            CoursesDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.Courses.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.Courses.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID, match.getCourse_type_id())
                            .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_CODE, match.getCourse_code())
                            .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_NAME, match.getCourse_name())
                            .withValue(StudentContract.Courses.COLUMN_NAME_FEES, match.getFees())
                            .withValue(StudentContract.Courses.COLUMN_NAME_PREREQUISITE, match.getPrerequisite())
                            .withValue(StudentContract.Courses.COLUMN_NAME_RECOMMONDED, match.getRecommonded())
                            .withValue(StudentContract.Courses.COLUMN_NAME_TIME_DURATION, match.getTime_duration())
                            .withValue(StudentContract.Courses.COLUMN_NAME_YOU_TUBE_LINK, match.getYou_tube_link())
                            .withValue(StudentContract.Courses.COLUMN_NAME_SYLLABUSPATH, match.getSyllabuspath())
                            .withValue(StudentContract.Courses.COLUMN_NAME_M_TIMESTAMP, match.getM_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.Courses.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (CoursesDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.Courses.CONTENT_URI)
                    .withValue(StudentContract.Courses.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID, e.getCourse_type_id())
                    .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_CODE, e.getCourse_code())
                    .withValue(StudentContract.Courses.COLUMN_NAME_COURSE_NAME, e.getCourse_name())
                    .withValue(StudentContract.Courses.COLUMN_NAME_FEES, e.getFees())
                    .withValue(StudentContract.Courses.COLUMN_NAME_PREREQUISITE, e.getPrerequisite())
                    .withValue(StudentContract.Courses.COLUMN_NAME_RECOMMONDED, e.getRecommonded())
                    .withValue(StudentContract.Courses.COLUMN_NAME_TIME_DURATION, e.getTime_duration())
                    .withValue(StudentContract.Courses.COLUMN_NAME_YOU_TUBE_LINK, e.getYou_tube_link())
                    .withValue(StudentContract.Courses.COLUMN_NAME_SYLLABUSPATH, e.getSyllabuspath())
                    .withValue(StudentContract.Courses.COLUMN_NAME_M_TIMESTAMP, e.getM_timestamp())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.Courses.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalCoursesTypeData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<CoursesTypeDAO> entries = jsonHelper.coursesTypeList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, CoursesTypeDAO> entryMap = new HashMap<String, CoursesTypeDAO>();
        for (CoursesTypeDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.CourseType.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_COURSES_TYPE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            CoursesTypeDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.CourseType.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.CourseType.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.CourseType.COLUMN_NAME_TYPE_NAME, match.getType_name())

                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.CourseType.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (CoursesTypeDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.CourseType.CONTENT_URI)
                    .withValue(StudentContract.CourseType.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.CourseType.COLUMN_NAME_TYPE_NAME, e.getType_name())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.CourseType.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalDayPrefrenceData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<DayPrefrenceDAO> entries = jsonHelper.dayPrefrenceList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, DayPrefrenceDAO> entryMap = new HashMap<String, DayPrefrenceDAO>();
        for (DayPrefrenceDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.DayPrefrence.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_DAYPREFRENCE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);

            DayPrefrenceDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.DayPrefrence.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.DayPrefrence.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.DayPrefrence.COLUMN_NAME_PREFRENCE, match.getPrefrence())
                            .withValue(StudentContract.DayPrefrence.COLUMN_NAME_M_TIMESTAMP, match.getM_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = StudentContract.DayPrefrence.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (DayPrefrenceDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.DayPrefrence.CONTENT_URI)
                    .withValue(StudentContract.DayPrefrence.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.DayPrefrence.COLUMN_NAME_PREFRENCE, e.getPrefrence())
                    .withValue(StudentContract.DayPrefrence.COLUMN_NAME_M_TIMESTAMP, e.getM_timestamp())
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.DayPrefrence.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private void updateLocalUsersDayPrefrenceData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<DayPrefrenceDAO> entries = jsonHelper.usersDayPrefrenceList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, DayPrefrenceDAO> entryMap = new HashMap<String, DayPrefrenceDAO>();
        for (DayPrefrenceDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.UserDayPrefrence.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_USERSDAYPREFRENCE, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;
        int sync_status;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);
            sync_status = c.getInt(COLUMN_NAME_SYNC_STATUS);
            DayPrefrenceDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.UserDayPrefrence.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID, match.getUser_id())
                            .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID, match.getDayprefrence_id())
                            .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_DEL_STATUS, match.getDel_status())
                            .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_M_TIMESTAMP, match.getM_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                if(sync_status==1) {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = StudentContract.UserDayPrefrence.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
        }
        c.close();

        // Add new items
        for (DayPrefrenceDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.UserDayPrefrence.CONTENT_URI)
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID, e.getUser_id())
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID, e.getDayprefrence_id())
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_DEL_STATUS, e.getDel_status())
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_M_TIMESTAMP, e.getM_timestamp())
                    .withValue(StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS, 1)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.UserDayPrefrence.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }
    private void updateLocalStudentCommentsData(String data, SyncResult syncResult) throws OperationApplicationException, RemoteException {

        final ContentResolver contentResolver = getContext().getContentResolver();
        JsonHelper jsonHelper = new JsonHelper();

        final List<CommentModeDAO> entries = jsonHelper.studentCommentsList(data);

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, CommentModeDAO> entryMap = new HashMap<String, CommentModeDAO>();
        for (CommentModeDAO e : entries) {
            entryMap.put(e.getId(), e);
        }
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = StudentContract.StudentComments.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, PROJECTION_STUDENTCOMMENTS, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

// Find stale data
        int id;
        String s_no;
        int sync_status;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            s_no = c.getString(COLUMN_NAME_ENTRY_ID);
            sync_status = c.getInt(COLUMN_NAME_SYNC_STATUS);
            CommentModeDAO match = entryMap.get(s_no);
            if (match != null) {
// Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(s_no);
                // Check to see if the entry needs to be updated
                Uri existingUri = StudentContract.StudentComments.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();
                if ((match.getId() != null && match.getId().equals(s_no))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(StudentContract.StudentComments.COLUMN_NAME_ENTRY_ID, match.getId())
                            .withValue(StudentContract.StudentComments.COLUMN_NAME_USER_ID, match.getUser_id())
                            .withValue(StudentContract.StudentComments.COLUMN_NAME_STUDENT_COMMENT, match.getStudent_comments())
                            .withValue(StudentContract.StudentComments.COLUMN_NAME_COMMENTS_DATE, match.getDate_comments())
                            .withValue(StudentContract.StudentComments.COLUMN_NAME_M_TIMESTAMP, match.getM_timestamp())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                if(sync_status==1) {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = StudentContract.StudentComments.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
        }
        c.close();

        // Add new items
        for (CommentModeDAO e : entryMap.values()) {


            batch.add(ContentProviderOperation.newInsert(StudentContract.StudentComments.CONTENT_URI)
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_ENTRY_ID, e.getId())
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_USER_ID, e.getUser_id())
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_STUDENT_COMMENT, e.getStudent_comments())
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_COMMENTS_DATE, e.getDate_comments())
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_M_TIMESTAMP, e.getM_timestamp())
                    .withValue(StudentContract.StudentComments.COLUMN_NAME_SYNC_STATUS, 1)
                    .build());
            syncResult.stats.numInserts++;
        }

        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(StudentContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                StudentContract.StudentComments.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }
*/
}
