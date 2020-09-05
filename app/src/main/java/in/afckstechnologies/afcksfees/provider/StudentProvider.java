/*
 * Copyright 2013 The Android Open Source Project
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

package in.afckstechnologies.afcksfees.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import in.afckstechnologies.afcksfees.common.db.SelectionBuilder;


public class StudentProvider extends ContentProvider {
    AFCKSDatabase mDatabaseHelper;

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = StudentContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /entries
     */
    public static final int ROUTE_ENTRIES = 1;

    /**
     * URI ID for route: /entries/{ID}
     */
    public static final int ROUTE_ENTRIES_ID = 2;

    /**
     * URI ID for route: /location
     */
    public static final int ROUTE_LOC_ENTRIES = 3;

    /**
     * URI ID for route: /location/{ID}
     */
    public static final int ROUTE_LOC_ENTRIES_ID = 4;
    /**
     * URI ID for route: /course
     */
    public static final int ROUTE_COURSE_ENTRIES = 5;

    /**
     * URI ID for route: /course/{ID}
     */
    public static final int ROUTE_COURSE_ENTRIES_ID = 6;

    /**
     * URI ID for route: /course
     */
    public static final int ROUTE_TEMPLATE_ENTRIES = 7;

    /**
     * URI ID for route: /course/{ID}
     */
    public static final int ROUTE_TEMPLATE_ENTRIES_ID = 8;

    /**
     * URI ID for route: /student_batchdetails
     */
    public static final int ROUTE_STUDENT_BATCHDETAILS_ENTRIES = 9;

    /**
     * URI ID for route: /student_batchdetails/{ID}
     */
    public static final int ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID = 10;

    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_COMING_BATCHDETAILS_ENTRIES = 11;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_COMING_BATCHDETAILS_ENTRIES_ID = 12;
    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_STUDENT_ATTENDANCE_ENTRIES = 13;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID = 14;
    /**
     * /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_BRANCHES_ENTRIES = 15;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_BRANCHES_ENTRIES_ID = 16;
    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_COURSES_ENTRIES = 17;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_COURSES_ENTRIES_ID = 18;
    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_COURSE_TYPE_ENTRIES = 19;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_COURSE_TYPE_ENTRIES_ID = 20;
    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_DAYPREFRENCE_ENTRIES = 21;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_DAYPREFRENCE_ENTRIES_ID = 22;
    /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_USERDAYPREFRENCE_ENTRIES = 23;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_USERDAYPREFRENCE_ENTRIES_ID = 24;
    /**
     * /**
     * URI ID for route: /coming_batchdetails
     */
    public static final int ROUTE_STUDENTCOMMENTS_ENTRIES = 25;

    /**
     * URI ID for route: /coming_batchdetails/{ID}
     */
    public static final int ROUTE_STUDENTCOMMENTS_ENTRIES_ID = 26;
    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "entries", ROUTE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "entries/*", ROUTE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "locentries", ROUTE_LOC_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "locentries/*", ROUTE_LOC_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "courses", ROUTE_COURSE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "courses/*", ROUTE_COURSE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "templates", ROUTE_TEMPLATE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "templates/*", ROUTE_TEMPLATE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "studentbatches", ROUTE_STUDENT_BATCHDETAILS_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "studentbatches/*", ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "comingbatches", ROUTE_COMING_BATCHDETAILS_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "comingbatches/*", ROUTE_COMING_BATCHDETAILS_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "studentattendance", ROUTE_STUDENT_ATTENDANCE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "studentattendance/*", ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "branchesdetails", ROUTE_BRANCHES_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "branchesdetails/*", ROUTE_BRANCHES_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "coursesdetails", ROUTE_COURSES_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "coursesdetails/*", ROUTE_COURSES_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "coursetype", ROUTE_COURSE_TYPE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "coursetype/*", ROUTE_COURSE_TYPE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "dayprefrencedetails", ROUTE_DAYPREFRENCE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "dayprefrencedetails/*", ROUTE_DAYPREFRENCE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "userdayprefrencedetails", ROUTE_USERDAYPREFRENCE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "userdayprefrencedetails/*", ROUTE_USERDAYPREFRENCE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "studentscommentdetails", ROUTE_STUDENTCOMMENTS_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "studentscommentdetails/*", ROUTE_STUDENTCOMMENTS_ENTRIES_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new AFCKSDatabase(getContext());
        return true;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_ENTRIES:
                return StudentContract.Entry.CONTENT_TYPE;
            case ROUTE_ENTRIES_ID:
                return StudentContract.Entry.CONTENT_ITEM_TYPE;
            case ROUTE_LOC_ENTRIES:
                return StudentContract.StudentLocation.CONTENT_TYPE;
            case ROUTE_LOC_ENTRIES_ID:
                return StudentContract.StudentLocation.CONTENT_ITEM_TYPE;
            case ROUTE_COURSE_ENTRIES:
                return StudentContract.StudentCourse.CONTENT_TYPE;
            case ROUTE_COURSE_ENTRIES_ID:
                return StudentContract.StudentCourse.CONTENT_ITEM_TYPE;
            case ROUTE_TEMPLATE_ENTRIES:
                return StudentContract.TrainersTemplate.CONTENT_TYPE;
            case ROUTE_TEMPLATE_ENTRIES_ID:
                return StudentContract.TrainersTemplate.CONTENT_ITEM_TYPE;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES:
                return StudentContract.StudentBatchdetails.CONTENT_TYPE;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID:
                return StudentContract.StudentBatchdetails.CONTENT_ITEM_TYPE;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES:
                return StudentContract.ComingBatchdetails.CONTENT_TYPE;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES_ID:
                return StudentContract.ComingBatchdetails.CONTENT_ITEM_TYPE;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES:
                return StudentContract.StudentAttendance.CONTENT_TYPE;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID:
                return StudentContract.StudentAttendance.CONTENT_ITEM_TYPE;
            case ROUTE_BRANCHES_ENTRIES:
                return StudentContract.Branches.CONTENT_TYPE;
            case ROUTE_BRANCHES_ENTRIES_ID:
                return StudentContract.Branches.CONTENT_ITEM_TYPE;
            case ROUTE_COURSES_ENTRIES:
                return StudentContract.Courses.CONTENT_TYPE;
            case ROUTE_COURSES_ENTRIES_ID:
                return StudentContract.Courses.CONTENT_ITEM_TYPE;
            case ROUTE_COURSE_TYPE_ENTRIES:
                return StudentContract.CourseType.CONTENT_TYPE;
            case ROUTE_COURSE_TYPE_ENTRIES_ID:
                return StudentContract.CourseType.CONTENT_ITEM_TYPE;
            case ROUTE_DAYPREFRENCE_ENTRIES:
                return StudentContract.DayPrefrence.CONTENT_TYPE;
            case ROUTE_DAYPREFRENCE_ENTRIES_ID:
                return StudentContract.DayPrefrence.CONTENT_ITEM_TYPE;
            case ROUTE_USERDAYPREFRENCE_ENTRIES:
                return StudentContract.UserDayPrefrence.CONTENT_TYPE;
            case ROUTE_USERDAYPREFRENCE_ENTRIES_ID:
                return StudentContract.UserDayPrefrence.CONTENT_ITEM_TYPE;
            case ROUTE_STUDENTCOMMENTS_ENTRIES:
                return StudentContract.StudentComments.CONTENT_TYPE;
            case ROUTE_STUDENTCOMMENTS_ENTRIES_ID:
                return StudentContract.StudentComments.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     *
     * <p>Currently supports returning all entries (/entries) and individual entries by ID
     * (/entries/{ID}).
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_ENTRIES_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(StudentContract.Entry._ID + "=?", id);
            case ROUTE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;

            case ROUTE_LOC_ENTRIES_ID:
                // Return a single entry, by ID.
                String lid = uri.getLastPathSegment();
                builder.where(StudentContract.StudentLocation._ID + "=?", lid);
            case ROUTE_LOC_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.StudentLocation.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor cl = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctxl = getContext();
                assert ctxl != null;
                cl.setNotificationUri(ctxl.getContentResolver(), uri);
                return cl;

            case ROUTE_COURSE_ENTRIES_ID:
                // Return a single entry, by ID.
                String cid = uri.getLastPathSegment();
                builder.where(StudentContract.StudentCourse._ID + "=?", cid);
            case ROUTE_COURSE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.StudentCourse.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor cc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctxc = getContext();
                assert ctxc != null;
                cc.setNotificationUri(ctxc.getContentResolver(), uri);
                return cc;

            case ROUTE_TEMPLATE_ENTRIES_ID:
                // Return a single entry, by ID.
                String tid = uri.getLastPathSegment();
                builder.where(StudentContract.TrainersTemplate._ID + "=?", tid);
            case ROUTE_TEMPLATE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.TrainersTemplate.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor tc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ttxc = getContext();
                assert ttxc != null;
                tc.setNotificationUri(ttxc.getContentResolver(), uri);
                return tc;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID:
                // Return a single entry, by ID.
                String sbid = uri.getLastPathSegment();
                builder.where(StudentContract.StudentBatchdetails._ID + "=?", sbid);
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.StudentBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor sbc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context sbtxc = getContext();
                assert sbtxc != null;
                sbc.setNotificationUri(sbtxc.getContentResolver(), uri);
                return sbc;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES_ID:
                // Return a single entry, by ID.
                String cbid = uri.getLastPathSegment();
                builder.where(StudentContract.ComingBatchdetails._ID + "=?", cbid);
            case ROUTE_COMING_BATCHDETAILS_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.ComingBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor cbc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context cbtxc = getContext();
                assert cbtxc != null;
                cbc.setNotificationUri(cbtxc.getContentResolver(), uri);
                return cbc;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID:
                // Return a single entry, by ID.
                String said = uri.getLastPathSegment();
                builder.where(StudentContract.StudentAttendance._ID + "=?", said);
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.StudentAttendance.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor sac = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context satxc = getContext();
                assert satxc != null;
                sac.setNotificationUri(satxc.getContentResolver(), uri);
                return sac;

            case ROUTE_BRANCHES_ENTRIES_ID:
                // Return a single entry, by ID.
                String bid = uri.getLastPathSegment();
                builder.where(StudentContract.Branches._ID + "=?", bid);
            case ROUTE_BRANCHES_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.Branches.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor bc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context bctxc = getContext();
                assert bctxc != null;
                bc.setNotificationUri(bctxc.getContentResolver(), uri);
                return bc;
            case ROUTE_COURSES_ENTRIES_ID:
                // Return a single entry, by ID.
                String cdid = uri.getLastPathSegment();
                builder.where(StudentContract.Courses._ID + "=?", cdid);
            case ROUTE_COURSES_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.Courses.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor cdc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context cdtxc = getContext();
                assert cdtxc != null;
                cdc.setNotificationUri(cdtxc.getContentResolver(), uri);
                return cdc;
            case ROUTE_COURSE_TYPE_ENTRIES_ID:
                // Return a single entry, by ID.
                String ctid = uri.getLastPathSegment();
                builder.where(StudentContract.CourseType._ID + "=?", ctid);
            case ROUTE_COURSE_TYPE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.CourseType.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor ctc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context cttxc = getContext();
                assert cttxc != null;
                ctc.setNotificationUri(cttxc.getContentResolver(), uri);
                return ctc;

            case ROUTE_DAYPREFRENCE_ENTRIES_ID:
                // Return a single entry, by ID.
                String dpid = uri.getLastPathSegment();
                builder.where(StudentContract.DayPrefrence._ID + "=?", dpid);
            case ROUTE_DAYPREFRENCE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.DayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor dpc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context dptxc = getContext();
                assert dptxc != null;
                dpc.setNotificationUri(dptxc.getContentResolver(), uri);
                return dpc;
            case ROUTE_USERDAYPREFRENCE_ENTRIES_ID:
                // Return a single entry, by ID.
                String udid = uri.getLastPathSegment();
                builder.where(StudentContract.UserDayPrefrence._ID + "=?", udid);
            case ROUTE_USERDAYPREFRENCE_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.UserDayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor udc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context udtxc = getContext();
                assert udtxc != null;
                udc.setNotificationUri(udtxc.getContentResolver(), uri);
                return udc;

            case ROUTE_STUDENTCOMMENTS_ENTRIES_ID:
                // Return a single entry, by ID.
                String scid = uri.getLastPathSegment();
                builder.where(StudentContract.StudentComments._ID + "=?", scid);
            case ROUTE_STUDENTCOMMENTS_ENTRIES:
                // Return all known entries.
                builder.table(StudentContract.StudentComments.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor scdc = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context sctxc = getContext();
                assert sctxc != null;
                scdc.setNotificationUri(sctxc.getContentResolver(), uri);
                return scdc;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Insert a new entry into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_ENTRIES:
                long id = db.insertOrThrow(StudentContract.Entry.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.Entry.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_LOC_ENTRIES:
                long idl = db.insertOrThrow(StudentContract.StudentLocation.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.StudentLocation.CONTENT_URI + "/" + idl);
                break;
            case ROUTE_LOC_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_COURSE_ENTRIES:
                long idc = db.insertOrThrow(StudentContract.StudentCourse.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.StudentCourse.CONTENT_URI + "/" + idc);
                break;
            case ROUTE_COURSE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_TEMPLATE_ENTRIES:
                long idt = db.insertOrThrow(StudentContract.TrainersTemplate.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.TrainersTemplate.CONTENT_URI + "/" + idt);
                break;
            case ROUTE_TEMPLATE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES:
                long idsb = db.insertOrThrow(StudentContract.StudentBatchdetails.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.StudentBatchdetails.CONTENT_URI + "/" + idsb);
                break;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_COMING_BATCHDETAILS_ENTRIES:
                long cdsb = db.insertOrThrow(StudentContract.ComingBatchdetails.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.ComingBatchdetails.CONTENT_URI + "/" + cdsb);
                break;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES:
                long csa = db.insertOrThrow(StudentContract.StudentAttendance.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.StudentAttendance.CONTENT_URI + "/" + csa);
                break;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_BRANCHES_ENTRIES:
                long cb = db.insertOrThrow(StudentContract.Branches.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.Branches.CONTENT_URI + "/" + cb);
                break;
            case ROUTE_BRANCHES_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_COURSES_ENTRIES:
                long cdb = db.insertOrThrow(StudentContract.Courses.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.Courses.CONTENT_URI + "/" + cdb);
                break;
            case ROUTE_COURSES_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_COURSE_TYPE_ENTRIES:
                long ctb = db.insertOrThrow(StudentContract.CourseType.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.CourseType.CONTENT_URI + "/" + ctb);
                break;
            case ROUTE_COURSE_TYPE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_DAYPREFRENCE_ENTRIES:
                long cdp = db.insertOrThrow(StudentContract.DayPrefrence.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.DayPrefrence.CONTENT_URI + "/" + cdp);
                break;
            case ROUTE_DAYPREFRENCE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_USERDAYPREFRENCE_ENTRIES:
                long cud = db.insertOrThrow(StudentContract.UserDayPrefrence.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.DayPrefrence.CONTENT_URI + "/" + cud);
                break;
            case ROUTE_USERDAYPREFRENCE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_STUDENTCOMMENTS_ENTRIES:
                long scd = db.insertOrThrow(StudentContract.StudentComments.TABLE_NAME, null, values);
                result = Uri.parse(StudentContract.StudentComments.CONTENT_URI + "/" + scd);
                break;
            case ROUTE_STUDENTCOMMENTS_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete an entry by database by URI.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ENTRIES:
                count = builder.table(StudentContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ENTRIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(StudentContract.Entry.TABLE_NAME)
                        .where(StudentContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;

            case ROUTE_LOC_ENTRIES:
                count = builder.table(StudentContract.StudentLocation.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_LOC_ENTRIES_ID:
                String idl = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentLocation.TABLE_NAME)
                        .where(StudentContract.StudentLocation._ID + "=?", idl)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSE_ENTRIES:
                count = builder.table(StudentContract.StudentCourse.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSE_ENTRIES_ID:
                String idc = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentCourse.TABLE_NAME)
                        .where(StudentContract.StudentCourse._ID + "=?", idc)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_TEMPLATE_ENTRIES:
                count = builder.table(StudentContract.TrainersTemplate.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_TEMPLATE_ENTRIES_ID:
                String idt = uri.getLastPathSegment();
                count = builder.table(StudentContract.TrainersTemplate.TABLE_NAME)
                        .where(StudentContract.TrainersTemplate._ID + "=?", idt)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES:
                count = builder.table(StudentContract.StudentBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID:
                String idsb = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentBatchdetails.TABLE_NAME)
                        .where(StudentContract.StudentBatchdetails._ID + "=?", idsb)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES:
                count = builder.table(StudentContract.ComingBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES_ID:
                String idcb = uri.getLastPathSegment();
                count = builder.table(StudentContract.ComingBatchdetails.TABLE_NAME)
                        .where(StudentContract.ComingBatchdetails._ID + "=?", idcb)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES:
                count = builder.table(StudentContract.StudentAttendance.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID:
                String idsa = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentAttendance.TABLE_NAME)
                        .where(StudentContract.StudentAttendance._ID + "=?", idsa)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_BRANCHES_ENTRIES:
                count = builder.table(StudentContract.Branches.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_BRANCHES_ENTRIES_ID:
                String idb = uri.getLastPathSegment();
                count = builder.table(StudentContract.Branches.TABLE_NAME)
                        .where(StudentContract.Branches._ID + "=?", idb)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSES_ENTRIES:
                count = builder.table(StudentContract.Courses.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSES_ENTRIES_ID:
                String idcd = uri.getLastPathSegment();
                count = builder.table(StudentContract.Courses.TABLE_NAME)
                        .where(StudentContract.Courses._ID + "=?", idcd)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSE_TYPE_ENTRIES:
                count = builder.table(StudentContract.CourseType.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_COURSE_TYPE_ENTRIES_ID:
                String idct = uri.getLastPathSegment();
                count = builder.table(StudentContract.CourseType.TABLE_NAME)
                        .where(StudentContract.CourseType._ID + "=?", idct)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_DAYPREFRENCE_ENTRIES:
                count = builder.table(StudentContract.DayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_DAYPREFRENCE_ENTRIES_ID:
                String iddp = uri.getLastPathSegment();
                count = builder.table(StudentContract.DayPrefrence.TABLE_NAME)
                        .where(StudentContract.DayPrefrence._ID + "=?", iddp)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_USERDAYPREFRENCE_ENTRIES:
                count = builder.table(StudentContract.UserDayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_USERDAYPREFRENCE_ENTRIES_ID:
                String idud = uri.getLastPathSegment();
                count = builder.table(StudentContract.UserDayPrefrence.TABLE_NAME)
                        .where(StudentContract.UserDayPrefrence._ID + "=?", idud)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;

            case ROUTE_STUDENTCOMMENTS_ENTRIES:
                count = builder.table(StudentContract.StudentComments.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_STUDENTCOMMENTS_ENTRIES_ID:
                String idsc = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentComments.TABLE_NAME)
                        .where(StudentContract.StudentComments._ID + "=?", idsc)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * Update an etry in the database by URI.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ENTRIES:
                count = builder.table(StudentContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ENTRIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(StudentContract.Entry.TABLE_NAME)
                        .where(StudentContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_LOC_ENTRIES:
                count = builder.table(StudentContract.StudentLocation.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_LOC_ENTRIES_ID:
                String idl = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentLocation.TABLE_NAME)
                        .where(StudentContract.StudentLocation._ID + "=?", idl)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSE_ENTRIES:
                count = builder.table(StudentContract.StudentCourse.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSE_ENTRIES_ID:
                String idc = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentCourse.TABLE_NAME)
                        .where(StudentContract.StudentCourse._ID + "=?", idc)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_TEMPLATE_ENTRIES:
                count = builder.table(StudentContract.TrainersTemplate.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_TEMPLATE_ENTRIES_ID:
                String idt = uri.getLastPathSegment();
                count = builder.table(StudentContract.TrainersTemplate.TABLE_NAME)
                        .where(StudentContract.TrainersTemplate._ID + "=?", idt)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;

            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES:
                count = builder.table(StudentContract.StudentBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_STUDENT_BATCHDETAILS_ENTRIES_ID:
                String idsb = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentBatchdetails.TABLE_NAME)
                        .where(StudentContract.StudentBatchdetails._ID + "=?", idsb)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES:
                count = builder.table(StudentContract.ComingBatchdetails.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COMING_BATCHDETAILS_ENTRIES_ID:
                String idcb = uri.getLastPathSegment();
                count = builder.table(StudentContract.ComingBatchdetails.TABLE_NAME)
                        .where(StudentContract.ComingBatchdetails._ID + "=?", idcb)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES:
                count = builder.table(StudentContract.StudentAttendance.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_STUDENT_ATTENDANCE_ENTRIES_ID:
                String idsa = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentAttendance.TABLE_NAME)
                        .where(StudentContract.StudentAttendance._ID + "=?", idsa)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;

            case ROUTE_BRANCHES_ENTRIES:
                count = builder.table(StudentContract.Branches.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_BRANCHES_ENTRIES_ID:
                String idb = uri.getLastPathSegment();
                count = builder.table(StudentContract.Branches.TABLE_NAME)
                        .where(StudentContract.Branches._ID + "=?", idb)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSES_ENTRIES:
                count = builder.table(StudentContract.Courses.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSES_ENTRIES_ID:
                String idcd = uri.getLastPathSegment();
                count = builder.table(StudentContract.Courses.TABLE_NAME)
                        .where(StudentContract.Courses._ID + "=?", idcd)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSE_TYPE_ENTRIES:
                count = builder.table(StudentContract.CourseType.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_COURSE_TYPE_ENTRIES_ID:
                String idct = uri.getLastPathSegment();
                count = builder.table(StudentContract.CourseType.TABLE_NAME)
                        .where(StudentContract.CourseType._ID + "=?", idct)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;

            case ROUTE_DAYPREFRENCE_ENTRIES:
                count = builder.table(StudentContract.DayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_DAYPREFRENCE_ENTRIES_ID:
                String iddp = uri.getLastPathSegment();
                count = builder.table(StudentContract.DayPrefrence.TABLE_NAME)
                        .where(StudentContract.DayPrefrence._ID + "=?", iddp)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_USERDAYPREFRENCE_ENTRIES:
                count = builder.table(StudentContract.UserDayPrefrence.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_USERDAYPREFRENCE_ENTRIES_ID:
                String idud = uri.getLastPathSegment();
                count = builder.table(StudentContract.UserDayPrefrence.TABLE_NAME)
                        .where(StudentContract.UserDayPrefrence._ID + "=?", idud)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_STUDENTCOMMENTS_ENTRIES:
                count = builder.table(StudentContract.StudentComments.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_STUDENTCOMMENTS_ENTRIES_ID:
                String idsc = uri.getLastPathSegment();
                count = builder.table(StudentContract.StudentComments.TABLE_NAME)
                        .where(StudentContract.StudentComments._ID + "=?", idsc)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * SQLite backend for @{link FeedProvider}.
     * <p>
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */
    public static class AFCKSDatabase extends SQLiteOpenHelper {
        /**
         * Schema version.
         */
        public static final int DATABASE_VERSION = 1;
        /**
         * Filename for SQLite file.
         */
        public static final String DATABASE_NAME = "dbafcks.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String TYPE_REAL = " REAL";
        private static final String COMMA_SEP = ",";
        /**
         * SQL statement to create "users" table.
         */
        private static final String SQL_CREATE_USERS =
                "CREATE TABLE " + StudentContract.Entry.TABLE_NAME + " (" +
                        StudentContract.Entry._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.Entry.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_FIRST_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_LAST_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_MOBILE_NO + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_EMAIL_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_GENDER + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_FCM_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_CREATED_AT + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_NOTES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_PREFERENCE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_CALLBACK + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_ENQNOTES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_NICK_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_NATIONALITY + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_DOB + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_MARITAL_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_PROFILE_PIC + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_JOB_SEARCH_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_JOB_PROGRAM_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_CURRENT_CTC + TYPE_REAL + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_EXPECTED_FROM_CTC + TYPE_REAL + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_EXPECTED_TO_CTC + TYPE_REAL + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_U_TIMESTAMP + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Entry.COLUMN_NAME_SYNC_STATUS + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "users" table.
         */

        /**
         * SQL statement to create "Location" table.
         */
        private static final String SQL_CREATE_USERS_LOC =
                "CREATE TABLE " + StudentContract.StudentLocation.TABLE_NAME + " (" +
                        StudentContract.StudentLocation._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.StudentLocation.COLUMN_NAME_S_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentLocation.COLUMN_NAME_BRANCH_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentLocation.COLUMN_NAME_USER_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentLocation.COLUMN_NAME_U_TIMESTAMP + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "location" table.
         */

        /**
         * SQL statement to create "Course" table.
         */
        private static final String SQL_CREATE_USERS_COURSE =
                "CREATE TABLE " + StudentContract.StudentCourse.TABLE_NAME + " (" +
                        StudentContract.StudentCourse._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.StudentCourse.COLUMN_NAME_S_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_TYPE_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_TYPE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_COURSE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_COURSE_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_USER_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_U_TIMESTAMP + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "Course" table.
         */

        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_TRAINER_TEMPLATE =
                "CREATE TABLE " + StudentContract.TrainersTemplate.TABLE_NAME + " (" +
                        StudentContract.TrainersTemplate._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.TrainersTemplate.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.TrainersTemplate.COLUMN_NAME_SUBJECT + TYPE_TEXT + COMMA_SEP +
                        StudentContract.TrainersTemplate.COLUMN_NAME_TEMPLATE_TEXT + TYPE_TEXT + COMMA_SEP +
                        StudentContract.TrainersTemplate.COLUMN_NAME_TAG + TYPE_TEXT + COMMA_SEP +
                        StudentContract.TrainersTemplate.COLUMN_NAME_COURSE_MAP_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.TrainersTemplate.COLUMN_NAME_LOCATION_MAP_ID + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */

        /**
         * SQL statement to create "users" table.
         */
        private static final String SQL_CREATE_STUDENT_BATCHDETAILS =
                "CREATE TABLE " + StudentContract.StudentBatchdetails.TABLE_NAME + " (" +
                        StudentContract.StudentBatchdetails._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_SBD_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_FIRST_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_LAST_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_MOBILE_NO + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_EMAIL_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_GENDER + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_BATCH_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENT_BATCH_CAT + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_PREVIOUS_ATTENDANCE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_COURSE_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_NOTES_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_DISCONTINUE_REASON + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_STUDENTS_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_FEES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_BASEFEES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_DUE_AMOUNT + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentBatchdetails.COLUMN_NAME_START_DATE + TYPE_TEXT + ")";


        /**
         * SQL statement to drop "users" table.
         */

        /**
         * SQL statement to create "Course" table.
         */
        private static final String SQL_CREATE_COMING_BATCH_DETAILS =
                "CREATE TABLE " + StudentContract.ComingBatchdetails.TABLE_NAME + " (" +
                        StudentContract.ComingBatchdetails._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_TYPE + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_DURATION + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_FACULTY_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_FEES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_START_DATE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_FREQUENCY + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_NOTES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_END_DATE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_COMPLETION_STATUS + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MAIL_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_TRAINER_MOBILE_NO + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_BRANCH_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_ATTENDANCE_MARKED + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_REF_BATCH + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_MEETING_LINK + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_WA_INVITE_LINK + TYPE_TEXT + COMMA_SEP +
                        StudentContract.ComingBatchdetails.COLUMN_NAME_TIMINGS + TYPE_TEXT + ")";

        /**
         * SQL statement to drop "Course" table.
         */

        /**
         * SQL statement to create "Course" table.
         */
        private static final String SQL_CREATE_STUDENT_ATTENDANCE =
                "CREATE TABLE " + StudentContract.StudentAttendance.TABLE_NAME + " (" +
                        StudentContract.StudentAttendance._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.StudentAttendance.COLUMN_NAME_ENTRY_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_USER_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_BATCH_ID + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_STUDENT_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_BATCH_DATE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentAttendance.COLUMN_NAME_ATTENDANCEDATE + TYPE_TEXT + ")";

        /**
         * SQL statement to drop "Course" table.
         */

        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_BRANCHES =
                "CREATE TABLE " + StudentContract.Branches.TABLE_NAME + " (" +
                        StudentContract.Branches._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.Branches.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_BRANCH_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_LATITUDE + TYPE_REAL + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_LONGITUDE + TYPE_REAL + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_BRANCH_SHORT + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_ADDRESS + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Branches.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */
        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_COURSES =
                "CREATE TABLE " + StudentContract.Courses.TABLE_NAME + " (" +
                        StudentContract.Courses._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.Courses.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_COURSE_TYPE_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_COURSE_CODE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_COURSE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_TIME_DURATION + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_PREREQUISITE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_RECOMMONDED + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_FEES + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_SYLLABUSPATH + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_YOU_TUBE_LINK + TYPE_TEXT + COMMA_SEP +
                        StudentContract.Courses.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */
        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_COURSETYPE =
                "CREATE TABLE " + StudentContract.CourseType.TABLE_NAME + " (" +
                        StudentContract.CourseType._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.CourseType.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.CourseType.COLUMN_NAME_TYPE_NAME + TYPE_TEXT + COMMA_SEP +
                        StudentContract.CourseType.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */

        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_DAYPREFRENCE =
                "CREATE TABLE " + StudentContract.DayPrefrence.TABLE_NAME + " (" +
                        StudentContract.DayPrefrence._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.DayPrefrence.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.DayPrefrence.COLUMN_NAME_PREFRENCE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.DayPrefrence.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */
        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_USERDAYPREFRENCE =
                "CREATE TABLE " + StudentContract.UserDayPrefrence.TABLE_NAME + " (" +
                        StudentContract.UserDayPrefrence._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_DEL_STATUS + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */
        /**
         * SQL statement to create "template" table.
         */
        private static final String SQL_CREATE_STUDENTCOMMENTS =
                "CREATE TABLE " + StudentContract.StudentComments.TABLE_NAME + " (" +
                        StudentContract.StudentComments._ID + " INTEGER PRIMARY KEY," +
                        StudentContract.StudentComments.COLUMN_NAME_ENTRY_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentComments.COLUMN_NAME_USER_ID + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentComments.COLUMN_NAME_STUDENT_COMMENT + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentComments.COLUMN_NAME_COMMENTS_DATE + TYPE_TEXT + COMMA_SEP +
                        StudentContract.StudentComments.COLUMN_NAME_M_TIMESTAMP + TYPE_INTEGER + COMMA_SEP +
                        StudentContract.StudentComments.COLUMN_NAME_SYNC_STATUS + TYPE_INTEGER + ")";

        /**
         * SQL statement to drop "template" table.
         */
        /**
         * SQL statement to drop "users" view.
         */

        private static final String SQL_CREATE_VIEW_USERS =
                "CREATE  VIEW vwSearchUsers  AS  select users.id AS id,users.gender AS gender,users.first_name AS first_name,users.last_name AS last_name,users.mobile_no AS mobile_no,users.email_id AS email_id,trim(users.first_name||' '||users.last_name||' '||users.mobile_no||' '||users.email_id||' '||users.gender) AS Details,sync_status from users where sync_status<3";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.Entry.TABLE_NAME;


        /**
         * SQL statement to drop "location" table.
         */

        private static final String SQL_DELETE_LOC_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.StudentLocation.TABLE_NAME;
        /**
         * SQL statement to drop "course" table.
         */

        private static final String SQL_DELETE_COURSE_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.StudentCourse.TABLE_NAME;

        /**
         * SQL statement to drop "template" table.
         */

        private static final String SQL_DELETE_TEMPLATE_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.TrainersTemplate.TABLE_NAME;
        /**
         * SQL statement to drop "template" table.
         */

        private static final String SQL_DELETE_STUDENT_BATCHDETAILS_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.StudentBatchdetails.TABLE_NAME;

        /**
         * SQL statement to drop "template" view.
         */

        /**
         * SQL statement to drop "coming batches" table.
         */

        private static final String SQL_DELETE_COMING_BATCHDETAILS_ENTRIES =
                "DROP TABLE IF EXISTS " + StudentContract.ComingBatchdetails.TABLE_NAME;

        /**
         * SQL statement to drop "template" view.
         */

        /**
         * SQL statement to drop "coming batches" table.
         */

        private static final String SQL_DELETE_STUDENT_ATTENDANCE =
                "DROP TABLE IF EXISTS " + StudentContract.StudentAttendance.TABLE_NAME;

        /**
         * SQL statement to drop "coming Branches" table.
         */

        private static final String SQL_DELETE_BRANCHES =
                "DROP TABLE IF EXISTS " + StudentContract.Branches.TABLE_NAME;

        /**
         * SQL statement to drop "coming Branches" table.
         */

        private static final String SQL_DELETE_COURSES =
                "DROP TABLE IF EXISTS " + StudentContract.Courses.TABLE_NAME;
        /**
         * SQL statement to drop "coming Branches" table.
         */

        private static final String SQL_DELETE_COURSETYPE =
                "DROP TABLE IF EXISTS " + StudentContract.CourseType.TABLE_NAME;
        /**
         * SQL statement to drop "coming Branches" table.
         */

        private static final String SQL_DELETE_DAYPREFRENCE =
                "DROP TABLE IF EXISTS " + StudentContract.DayPrefrence.TABLE_NAME;
        /**
         * SQL statement to drop "coming Branches" table.
         */

        private static final String SQL_DELETE_USERDAYPREFRENCE =
                "DROP TABLE IF EXISTS " + StudentContract.UserDayPrefrence.TABLE_NAME;
        /**
         * SQL statement to drop "template" view.
         */
        private static final String SQL_DELETE_USER_VIEW =
                "DROP VIEW IF EXISTS vwSearchUsers";

        /**
         * SQL statement to drop "template" view.
         */
        private static final String SQL_DELETE_STUDENTCOMMENTS =
                "DROP TABLE IF EXISTS " + StudentContract.StudentComments.TABLE_NAME;

        public AFCKSDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // db.execSQL(SQL_CREATE_USERS);
            //  db.execSQL(SQL_CREATE_USERS_LOC);
            //  db.execSQL(SQL_CREATE_USERS_COURSE);
            //  db.execSQL(SQL_CREATE_TRAINER_TEMPLATE);
            // db.execSQL(SQL_CREATE_STUDENT_BATCHDETAILS);
            // db.execSQL(SQL_CREATE_VIEW_USERS);
            db.execSQL(SQL_CREATE_COMING_BATCH_DETAILS);
            // db.execSQL(SQL_CREATE_STUDENT_ATTENDANCE);
            //  db.execSQL(SQL_CREATE_BRANCHES);
            //  db.execSQL(SQL_CREATE_COURSES);
            //  db.execSQL(SQL_CREATE_COURSETYPE);
            // db.execSQL(SQL_CREATE_DAYPREFRENCE);
            //  db.execSQL(SQL_CREATE_USERDAYPREFRENCE);
            // db.execSQL(SQL_CREATE_STUDENTCOMMENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            // db.execSQL(SQL_DELETE_ENTRIES);
            // db.execSQL(SQL_DELETE_LOC_ENTRIES);
            //  db.execSQL(SQL_DELETE_COURSE_ENTRIES);
            //  db.execSQL(SQL_DELETE_TEMPLATE_ENTRIES);
            //  db.execSQL(SQL_DELETE_STUDENT_BATCHDETAILS_ENTRIES);
            db.execSQL(SQL_DELETE_COMING_BATCHDETAILS_ENTRIES);
            //  db.execSQL(SQL_DELETE_STUDENT_ATTENDANCE);
            //db.execSQL(SQL_DELETE_USER_VIEW);
            //   db.execSQL(SQL_DELETE_BRANCHES);
            //   db.execSQL(SQL_DELETE_COURSES);
            //  db.execSQL(SQL_DELETE_COURSETYPE);
            //   db.execSQL(SQL_DELETE_DAYPREFRENCE);
            //   db.execSQL(SQL_DELETE_USERDAYPREFRENCE);
            //   db.execSQL(SQL_DELETE_STUDENTCOMMENTS);
            onCreate(db);
        }

        //fetching data
        public Cursor getUserId(String mobile_no) {
            String mno = "\"" + mobile_no + "\"";
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.Entry.TABLE_NAME + " where " + StudentContract.Entry.COLUMN_NAME_MOBILE_NO + " = " + mno + "";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getSerachUser(String search) {
            String COLUMN_DETAILS_NAME = "Details";
            SQLiteDatabase db = this.getReadableDatabase();
            //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " <> 3 ORDER BY " + COLUMN_ID + " ASC;";
            String sql = "SELECT * FROM vwSearchUsers " + " where " + StudentContract.Entry.COLUMN_NAME_MOBILE_NO + " like '%" + search + "%' or " + COLUMN_DETAILS_NAME + " like '%" + search + "%'";
            // "Select id,dept_name from vwDepartments where dept_name Like '%$Prefixtext%' or group_user_name Like '%$Prefixtext%'"
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getSerachBatches(String search, String user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql;
            //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " <> 3 ORDER BY " + COLUMN_ID + " ASC;";
            if (user_id.equals("RS")) {
                sql = "SELECT * FROM " + StudentContract.ComingBatchdetails.TABLE_NAME + " where " + StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE + " like '" + search + "%'";

            } else if (user_id.equals("AK")) {
                sql = "SELECT * FROM " + StudentContract.ComingBatchdetails.TABLE_NAME + " where " + StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE + " like '" + search + "%'";

            } else {

                sql = "SELECT * FROM " + StudentContract.ComingBatchdetails.TABLE_NAME + " where " + StudentContract.ComingBatchdetails.COLUMN_NAME_CODE + " = '" + user_id + "'" + " and " + StudentContract.ComingBatchdetails.COLUMN_NAME_BATCH_CODE + " like '" + search + "%'";
            }
            // "Select id,dept_name from vwDepartments where dept_name Like '%$Prefixtext%' or group_user_name Like '%$Prefixtext%'"
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getLocNames(String user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.StudentLocation.TABLE_NAME + " where " + StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS + "<3 " + " and " + StudentContract.StudentLocation.COLUMN_NAME_USER_ID + " = '" + user_id + "'";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public boolean updateLocDeleteStatus(String user_id, int locid, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.StudentLocation.TABLE_NAME, contentValues, StudentContract.StudentLocation.COLUMN_NAME_USER_ID + "=" + temp_id + " and " + StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID + "=" + locid, null);
            db.close();
            return true;
        }

        public void deleteLoc(String id, String user_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(StudentContract.StudentLocation.TABLE_NAME,
                    StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID + "=? AND " + StudentContract.StudentLocation.COLUMN_NAME_USER_ID + "=? ", new String[]{id, user_id});
            db.close();
        }

        public Cursor getCourseNames(String user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.StudentCourse.TABLE_NAME + " where " + StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS + "<3" + " and " + StudentContract.StudentCourse.COLUMN_NAME_USER_ID + " = '" + user_id + "'";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public boolean updateCourseDeleteStatus(String user_id, int cid, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.StudentCourse.TABLE_NAME, contentValues, StudentContract.StudentCourse.COLUMN_NAME_USER_ID + "=" + temp_id + " and " + StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID + "=" + cid, null);
            db.close();
            return true;
        }

        public void deleteCourse(String id, String user_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(StudentContract.StudentCourse.TABLE_NAME,
                    StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID + "=? AND " + StudentContract.StudentCourse.COLUMN_NAME_USER_ID + "=? ", new String[]{id, user_id});
            db.close();
        }

        public boolean updateDayPreDeleteStatus(String user_id, int did, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.UserDayPrefrence.TABLE_NAME, contentValues, StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID + "=" + temp_id + " and " + StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID + "=" + did, null);
            db.close();
            return true;
        }

        public void deleteUserDayPre(String id, String user_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(StudentContract.UserDayPrefrence.TABLE_NAME,
                    StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID + " = " + Integer.parseInt(id) + "" + " and " + StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID + " = " + Integer.parseInt(user_id) + "", null);
            db.close();
        }

        public String getConDisCountUsers(int user_id) {
            String count = "";
            int c1 = 0, c2 = 0;
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select count(Status) as Cont from " + StudentContract.StudentBatchdetails.TABLE_NAME + " where " + StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID + " = " + user_id + "" + " and " + StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS + " = 1 ";
            Cursor c = db.rawQuery(sql, null);
            assert c != null;
            if (c.moveToFirst()) {
                do {

                    c1 = c.getInt(c.getColumnIndex("Cont"));

                } while (c.moveToNext());
            }
            String sql1 = "select count(Status) as DisCont from " + StudentContract.StudentBatchdetails.TABLE_NAME + " where " + StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID + " = " + user_id + "" + " and " + StudentContract.StudentBatchdetails.COLUMN_NAME_STATUS + " = 0 ";
            Cursor cc = db.rawQuery(sql1, null);
            assert cc != null;
            if (cc.moveToFirst()) {
                do {

                    c2 = cc.getInt(cc.getColumnIndex("DisCont"));

                } while (cc.moveToNext());
            }
            count = "(" + c1 + ", " + c2 + ")";
            return count;
        }

        public String getUserNotes(String user_id) {
            String s = "";
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select Notes from " + StudentContract.Entry.TABLE_NAME + " where " + StudentContract.Entry.COLUMN_NAME_ENTRY_ID + " = '" + user_id + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    s = cursor.getString(cursor.getColumnIndex("Notes"));

                } while (cursor.moveToNext());
            }
            return s;
        }

        public Cursor getPreBatchStudentDetails(int user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.StudentBatchdetails.TABLE_NAME + " where " + StudentContract.StudentBatchdetails.COLUMN_NAME_ENTRY_ID + " = " + user_id + "";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getTemplatesDetails() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.TrainersTemplate.TABLE_NAME;
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public void updateUserDetails(String user_id, String name, String lastname, String phoneno, String emailid, String gender, int syncstatus) {
            String id1 = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.Entry.COLUMN_NAME_FIRST_NAME, name);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_LAST_NAME, lastname);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_MOBILE_NO, phoneno);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_EMAIL_ID, emailid);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_GENDER, gender);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, syncstatus);
            db.update(StudentContract.Entry.TABLE_NAME, contentValues, StudentContract.Entry.COLUMN_NAME_ENTRY_ID + "=" + id1, null);
            db.close();
        }

        public void updateUserNotes(String user_id, String cooments, int status) {
            String id1 = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.Entry.COLUMN_NAME_NOTES, cooments);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.Entry.TABLE_NAME, contentValues, StudentContract.Entry.COLUMN_NAME_ENTRY_ID + "=" + id1, null);
            db.close();
        }

        public void addNameSync(String user_id, String name, String lastname, String phoneno, String emailid, String gender, int syncstatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.Entry.COLUMN_NAME_ENTRY_ID, user_id);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_FIRST_NAME, name);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_LAST_NAME, lastname);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_MOBILE_NO, phoneno);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_EMAIL_ID, emailid);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_GENDER, gender);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_FCM_ID, "Admin");
            contentValues.put(StudentContract.Entry.COLUMN_NAME_NOTES, "");
            contentValues.put(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, syncstatus);
            db.insert(StudentContract.Entry.TABLE_NAME, null, contentValues);
            db.close();
        }

        public void deleteUser(String user_id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(StudentContract.Entry.TABLE_NAME, StudentContract.Entry.COLUMN_NAME_ENTRY_ID + "=? ", new String[]{user_id});
            db.close();
        }

        public void addUserLoc(String s_no, String id, String branch, String userid, int syncstatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_S_ID, s_no);
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID, Integer.parseInt(id));
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_BRANCH_NAME, branch);
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_USER_ID, userid);
            contentValues.put(StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS, syncstatus);
            db.insert(StudentContract.StudentLocation.TABLE_NAME, null, contentValues);
            db.close();
        }

        public void addUserCourse(String s_no, String id, String cousername, String coursecode, String coursetypeid, String userid, int syncstatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_S_ID, s_no);
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID, Integer.parseInt(id));
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_COURSE_NAME, cousername);
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_COURSE_CODE, coursecode);
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_TYPE_ID, coursetypeid);
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_USER_ID, userid);
            contentValues.put(StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS, syncstatus);
            db.insert(StudentContract.StudentCourse.TABLE_NAME, null, contentValues);
            db.close();
        }

        public void addUserDayPre(String s_no, String id, String userid, int syncstatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID, Integer.parseInt(s_no));
            contentValues.put(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID, Integer.parseInt(userid));
            contentValues.put(StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID, Integer.parseInt(id));
            contentValues.put(StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS, syncstatus);
            db.insert(StudentContract.UserDayPrefrence.TABLE_NAME, null, contentValues);
            db.close();
        }

        public Cursor getComingBatches(String course_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "select * from " + StudentContract.ComingBatchdetails.TABLE_NAME + " where " + StudentContract.ComingBatchdetails.COLUMN_NAME_COURSE_ID + " = '" + course_id + "'";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getBranches(int user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            //String sql = "select * from " + StudentContract.Branches.TABLE_NAME + " where " + StudentContract.Branches.COLUMN_NAME_ENTRY_ID + " = '" + user_id + "'";
            String sql = "Select d.id as id, b.branch_name,b.address,b.latitude,b.longitude,b.branch_short,b.m_timestamp, d.isselected from  (select id, \"selected\" as isselected from vwLocationsDemandedUserWise where  user_id=" + user_id + " union all select id, \"notselected\" from branches where id not in (select id from vwLocationsDemandedUserWise where user_id=" + user_id + ")) as d inner join branches as b on d.id=b.id";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getCourses(int user_id, int course_type) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "Select d.id as id,c.course_type_id,c.course_code, c.course_name,c.time_duration,c.prerequisite,c.recommonded,c.fees,c.syllabuspath,c.you_tube_link, d.isselected from (select id, \"selected\" as isselected from vwCourseDemandedUserWise where  user_id=" + user_id + " union all select id, \"notselected\" from courses where id not in (select id from vwCourseDemandedUserWise where user_id=" + user_id + ") and course_type_id =" + course_type + ") as d inner join courses as c on d.id=c.id ORDER BY c.id ASC";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUserDayPre(int user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "Select du.dayprefrence_id as id, dp.Prefrence,dp.m_timestamp, du.isselected from  (select dayprefrence_id, \"selected\" as isselected from user_dayprefrence where  user_id=" + user_id + " union all select id, \"notselected\" from DayPrefrence where id not in (select dayprefrence_id from user_dayprefrence where user_id=" + user_id + ")) as du inner join DayPrefrence as dp on du.dayprefrence_id=dp.id";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        //syncing data
        public Cursor getUnsyncedUsers() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.Entry.TABLE_NAME + " WHERE " + StudentContract.Entry.COLUMN_NAME_SYNC_STATUS + " = 0";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }


        public boolean updateUserIdStatus(String user_id, String id, int status) {
            String temp_id = "\"" + id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, status);
            contentValues.put(StudentContract.Entry.COLUMN_NAME_ENTRY_ID, user_id);
            db.update(StudentContract.Entry.TABLE_NAME, contentValues, StudentContract.Entry.COLUMN_NAME_ENTRY_ID + "=" + temp_id, null);
            ContentValues contentUserId = new ContentValues();
            contentUserId.put(StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID, user_id);
            db.update(StudentContract.UserDayPrefrence.TABLE_NAME, contentUserId, StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID + "=" + temp_id, null);
            db.update(StudentContract.StudentLocation.TABLE_NAME, contentUserId, StudentContract.StudentLocation.COLUMN_NAME_USER_ID + "=" + temp_id, null);
            db.update(StudentContract.StudentCourse.TABLE_NAME, contentUserId, StudentContract.StudentCourse.COLUMN_NAME_USER_ID + "=" + temp_id, null);
            db.close();
            return true;
        }

        public Cursor getUnsyncedUserDayPrefrence() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.UserDayPrefrence.TABLE_NAME + " WHERE " + StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS + " = 0";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public boolean updateUserDayPre(String user_id, String id, String pre_id, int status) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentUserId = new ContentValues();
            contentUserId.put(StudentContract.UserDayPrefrence.COLUMN_NAME_ENTRY_ID, id);
            contentUserId.put(StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.UserDayPrefrence.TABLE_NAME, contentUserId, StudentContract.UserDayPrefrence.COLUMN_NAME_USER_ID + "=" + user_id + " and " + StudentContract.UserDayPrefrence.COLUMN_NAME_DAYPREFRENCE_ID + "=" + pre_id, null);
            db.close();
            return true;
        }

        public Cursor getUnsyncedUserLoc() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.StudentLocation.TABLE_NAME + " WHERE " + StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS + " = 0";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public boolean updateUserLoc(String user_id, String bid, String id, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentUserId = new ContentValues();
            contentUserId.put(StudentContract.StudentLocation.COLUMN_NAME_S_ID, bid);
            contentUserId.put(StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.StudentLocation.TABLE_NAME, contentUserId, StudentContract.StudentLocation.COLUMN_NAME_USER_ID + "=" + temp_id + " and " + StudentContract.StudentLocation.COLUMN_NAME_ENTRY_ID + "=" + id, null);
            db.close();
            return true;
        }

        public Cursor getUnsyncedUserCourse() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.StudentCourse.TABLE_NAME + " WHERE " + StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS + " = 0";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public boolean updateUserCourse(String user_id, String cid, String id, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentUserId = new ContentValues();
            contentUserId.put(StudentContract.StudentCourse.COLUMN_NAME_S_ID, cid);
            contentUserId.put(StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.StudentCourse.TABLE_NAME, contentUserId, StudentContract.StudentCourse.COLUMN_NAME_USER_ID + "=" + temp_id + " and " + StudentContract.StudentCourse.COLUMN_NAME_ENTRY_ID + "=" + id, null);
            db.close();
            return true;
        }

        public boolean updateUserDeleteStatus(String user_id, int status) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentContract.Entry.COLUMN_NAME_SYNC_STATUS, status);
            db.update(StudentContract.Entry.TABLE_NAME, contentValues, StudentContract.Entry.COLUMN_NAME_ENTRY_ID + "=" + temp_id, null);
            db.close();
            return true;
        }

        public Cursor getUnsyncedUserDelete() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.Entry.TABLE_NAME + " WHERE " + StudentContract.Entry.COLUMN_NAME_SYNC_STATUS + " = 3";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUnsyncedUserDeleteLoc() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.StudentLocation.TABLE_NAME + " WHERE " + StudentContract.StudentLocation.COLUMN_NAME_SYNC_STATUS + " = 3";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUnsyncedUserDeleteCourse() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.StudentCourse.TABLE_NAME + " WHERE " + StudentContract.StudentCourse.COLUMN_NAME_SYNC_STATUS + " = 3";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUnsyncedUserDeleteDayPre() {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.UserDayPrefrence.TABLE_NAME + " WHERE " + StudentContract.UserDayPrefrence.COLUMN_NAME_SYNC_STATUS + " = 3";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUserDetails(String user_id) {
            String temp_id = "\"" + user_id + "\"";
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.Entry.TABLE_NAME + " WHERE " + StudentContract.Entry.COLUMN_NAME_ENTRY_ID + " =" + temp_id;
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getStudentComment(int user_id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.StudentComments.TABLE_NAME + " WHERE " + StudentContract.StudentComments.COLUMN_NAME_USER_ID + " =" + user_id;
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

        public Cursor getUserDetailsUpdate() {

            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + StudentContract.Entry.TABLE_NAME + " WHERE " + StudentContract.Entry.COLUMN_NAME_SYNC_STATUS + " =2";
            Cursor c = db.rawQuery(sql, null);
            return c;
        }

    }
}
