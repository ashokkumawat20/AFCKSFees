package in.afckstechnologies.afcksfees.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class StudentContract {
    private StudentContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "in.afckstechnologies.afcksfees";
    /**
     * Base URI. (content://in.afckstechnologies.afcksenquirymanagement)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "entries";
    private static final String PATH_LOC_ENTRIES = "locentries";
    private static final String PATH_COURSE_ENTRIES = "courses";
    private static final String PATH_TEMPLATE_ENTRIES = "templates";
    private static final String PATH_STUDENT_BATCHDETAILS_ENTRIES = "studentbatches";
    private static final String PATH_COMING_BATCHDETAILS_ENTRIES = "comingbatches";
    private static final String PATH_STUDENT_ATTENDANCE_ENTRIES = "studentattendance";
    private static final String PATH_BRANCHES_ENTRIES = "branchesdetails";
    private static final String PATH_COURSES_ENTRIES = "coursesdetails";
    private static final String PATH_COURSETYPE_ENTRIES = "coursetype";
    private static final String PATH_DAYPREFRENCEDETAILS_ENTRIES = "dayprefrencedetails";
    private static final String PATH_USERDAYPREFRENCEDETAILS_ENTRIES = "userdayprefrencedetails";
    private static final String PATH_STUDENTSCOMMENTDETAILS_ENTRIES = "studentscommentdetails";


    /**
     * Columns supported by "entries" records.
     */

    public static class Entry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.entries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.users";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_MOBILE_NO = "mobile_no";
        public static final String COLUMN_NAME_EMAIL_ID = "email_id";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_FCM_ID = "fcm_id";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_NOTES = "Notes";
        public static final String COLUMN_NAME_PREFERENCE = "preference";
        public static final String COLUMN_NAME_CALLBACK = "CallBack";
        public static final String COLUMN_NAME_ENQNOTES = "EnqNotes";
        public static final String COLUMN_NAME_NICK_NAME = "Nick_name";
        public static final String COLUMN_NAME_NATIONALITY = "Nationality";
        public static final String COLUMN_NAME_DOB = "DOB";
        public static final String COLUMN_NAME_MARITAL_STATUS = "Marital_status";
        public static final String COLUMN_NAME_PROFILE_PIC = "profile_pic";
        public static final String COLUMN_NAME_JOB_SEARCH_STATUS = "job_search_status";
        public static final String COLUMN_NAME_JOB_PROGRAM_STATUS = "job_program_status";
        public static final String COLUMN_NAME_CURRENT_CTC = "current_ctc";
        public static final String COLUMN_NAME_EXPECTED_FROM_CTC = "expected_from_ctc";
        public static final String COLUMN_NAME_EXPECTED_TO_CTC = "expected_to_ctc";
        public static final String COLUMN_NAME_U_TIMESTAMP = "m_timestamp";
        public static final String COLUMN_NAME_SYNC_STATUS = "sync_status";


    }

    public static class StudentLocation implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.locentries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.vwLocationsDemandedUserWise";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOC_ENTRIES).build();

        public static final String TABLE_NAME = "vwLocationsDemandedUserWise";
        public static final String COLUMN_NAME_S_ID = "s_no";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_BRANCH_NAME = "branch_name";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_U_TIMESTAMP = "m_timestamp";
        public static final String COLUMN_NAME_SYNC_STATUS = "sync_status";


    }

    public static class StudentCourse implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.courses";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.vwCourseDemandedUserWise";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE_ENTRIES).build();

        public static final String TABLE_NAME = "vwCourseDemandedUserWise";
        public static final String COLUMN_NAME_S_ID = "s_no";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TYPE_ID = "type_name_id";
        public static final String COLUMN_NAME_TYPE_NAME = "type_name";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_COURSE_CODE = "course_code";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_U_TIMESTAMP = "m_timestamp";
        public static final String COLUMN_NAME_SYNC_STATUS = "sync_status";


    }

    public static class TrainersTemplate implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.templates";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.TrainersTemplate";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEMPLATE_ENTRIES).build();

        public static final String TABLE_NAME = "TrainersTemplate";
        public static final String COLUMN_NAME_ENTRY_ID = "ID";
        public static final String COLUMN_NAME_SUBJECT = "Subject";
        public static final String COLUMN_NAME_TEMPLATE_TEXT = "Template_Text";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_COURSE_MAP_ID = "course_map_id";
        public static final String COLUMN_NAME_LOCATION_MAP_ID = "location_map_id";

    }

    public static class StudentBatchdetails implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.studentbatches";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.vwDetailsStudentsInBatch";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT_BATCHDETAILS_ENTRIES).build();

        public static final String TABLE_NAME = "vwDetailsStudentsInBatch";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_BATCH_ID = "batchid";
        public static final String COLUMN_NAME_BATCH_CODE = "batch_code";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_COURSE_CODE = "course_code";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_MOBILE_NO = "mobile_no";
        public static final String COLUMN_NAME_EMAIL_ID = "email_id";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_FEES = "fees";
        public static final String COLUMN_NAME_DUE_AMOUNT = "due_amount";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_BASEFEES = "BaseFees";
        public static final String COLUMN_NAME_STUDENTS_NAME = "Students_Name";
        public static final String COLUMN_NAME_SBD_ID = "sbd_id";
        public static final String COLUMN_NAME_STUDENT_BATCH_CAT = "student_batch_cat";
        public static final String COLUMN_NAME_NOTES_ID = "notes_id";
        public static final String COLUMN_NAME_PREVIOUS_ATTENDANCE = "previous_attendance";
        public static final String COLUMN_NAME_DISCONTINUE_REASON = "discontinue_reason";

    }

    public static class ComingBatchdetails implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.comingbatches";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.vwBatchStartingDetails";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMING_BATCHDETAILS_ENTRIES).build();

        public static final String TABLE_NAME = "vwBatchStartingDetails";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_BRANCH_ID = "branch_id";
        public static final String COLUMN_NAME_BATCH_CODE = "batch_code";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_TIMINGS = "timings";
        public static final String COLUMN_NAME_NOTES = "Notes";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";
        public static final String COLUMN_NAME_FEES = "fees";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_BRANCH_NAME = "branch_name";
        public static final String COLUMN_NAME_BATCH_TYPE = "batchtype";
        public static final String COLUMN_NAME_COMPLETION_STATUS = "completion_status";
        public static final String COLUMN_NAME_BATCH_END_DATE = "batch_end_date";
        public static final String COLUMN_NAME_TRAINER_MAIL_ID = "trainer_mail_id";
        public static final String COLUMN_NAME_TRAINER_MOBILE_NO = "trainer_mobile_no";
        public static final String COLUMN_NAME_FACULTY_NAME = "faculty_Name";
        public static final String COLUMN_NAME_ATTENDANCE_MARKED = "attendance_marked";
        public static final String COLUMN_NAME_REF_BATCH = "ref_batch";
        public static final String COLUMN_NAME_MEETING_LINK = "meeting_link";
        public static final String COLUMN_NAME_WA_INVITE_LINK = "wa_invite_link";
        public static final String COLUMN_NAME_CODE = "Code";
    }

    public static class StudentAttendance implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.studentattendance";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.vwViewStudentAttendance";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT_ATTENDANCE_ENTRIES).build();

        public static final String TABLE_NAME = "vwViewStudentAttendance";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_BATCH_ID = "batch_id";
        public static final String COLUMN_NAME_ATTENDANCE = "attendance";
        public static final String COLUMN_NAME_STUDENT_NAME = "student_name";
        public static final String COLUMN_NAME_BATCH_DATE = "batch_date";
        public static final String COLUMN_NAME_ATTENDANCEDATE = "AttendanceDate";
    }

    public static class Branches implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.branchesdetails";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.branches";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANCHES_ENTRIES).build();

        public static final String TABLE_NAME = "branches";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_BRANCH_NAME = "branch_name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_BRANCH_SHORT = "branch_short";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";

    }

    public static class Courses implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.coursesdetails";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.courses";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSES_ENTRIES).build();

        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_COURSE_TYPE_ID = "course_type_id";
        public static final String COLUMN_NAME_COURSE_CODE = "course_code";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_TIME_DURATION = "time_duration";
        public static final String COLUMN_NAME_PREREQUISITE = "prerequisite";
        public static final String COLUMN_NAME_RECOMMONDED = "recommonded";
        public static final String COLUMN_NAME_FEES = "fees";
        public static final String COLUMN_NAME_SYLLABUSPATH = "syllabuspath";
        public static final String COLUMN_NAME_YOU_TUBE_LINK = "you_tube_link";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";

    }

    public static class CourseType implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.coursetype";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.course_type";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSETYPE_ENTRIES).build();

        public static final String TABLE_NAME = "course_type";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TYPE_NAME = "type_name";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";

    }

    public static class DayPrefrence implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.dayprefrencedetails";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.DayPrefrence";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAYPREFRENCEDETAILS_ENTRIES).build();

        public static final String TABLE_NAME = "DayPrefrence";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_PREFRENCE = "Prefrence";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";

    }

    public static class UserDayPrefrence implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.userdayprefrencedetails";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.user_dayprefrence";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERDAYPREFRENCEDETAILS_ENTRIES).build();

        public static final String TABLE_NAME = "user_dayprefrence";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_DAYPREFRENCE_ID = "dayprefrence_id";
        public static final String COLUMN_NAME_DEL_STATUS = "del_status";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";
        public static final String COLUMN_NAME_SYNC_STATUS = "sync_status";

    }

    public static class StudentComments implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.afcksfees.studentscommentdetails";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.afcksfees.students_comment_details";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENTSCOMMENTDETAILS_ENTRIES).build();

        public static final String TABLE_NAME = "students_comment_details";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_STUDENT_COMMENT = "student_comment";
        public static final String COLUMN_NAME_COMMENTS_DATE = "comments_date";
        public static final String COLUMN_NAME_M_TIMESTAMP = "m_timestamp";
        public static final String COLUMN_NAME_SYNC_STATUS = "sync_status";

    }
}
