package in.afckstechnologies.afcksfees.utils;

import java.util.ArrayList;

/**
 * Created by admin on 12/9/2016.
 */

public class Config {
    // public static final String BASE_URL = "http://192.168.1.10:80/afcks/api/";
    // public static final String BASE_URL = "http://testprojects.in/afcks/api/";

    public static final String BASE_URL = "https://afckstechnologies.in/afcks/api/";
    public static final String URL_STUDENT_REGISTRATION = BASE_URL + "user/adduserByAdmin";
    public static final String GETAVAILABLETMOBILEDEVICEID = BASE_URL + "user/getAvailableTMobileDeviceID";
    public static final String URL_CENTER_DETAILS = BASE_URL + "user/branches";
    public static final String URL_COURSE_DETAILS = BASE_URL + "user/cources";
    public static final String URL_SEND_DETAILS = BASE_URL + "user/savecourse";
    public static final String URL_SEND_LOCATION_DETAILS = BASE_URL + "user/savebranch";
    public static final String URL_AVAILABLEAUTHORIZETRAINERREPANDRETURNID = BASE_URL + "user/availableAuthorizeTrainerRepAndReturnID";
    public static final String URL_UPDATEFEESAPPDETAILS = BASE_URL + "user/updatefeesappdetails";
    public static final String URL_GET_USER_DETAILS = BASE_URL + "user/getuserdetails";
    public static final String URL_UPDATE_USER_DETAILS = BASE_URL + "user/updateuserdetails";
    public static final String URL_DELETE_COURSE = BASE_URL + "user/deleteusercource";
    public static final String URL_DELETE_CENTER = BASE_URL + "user/deleteuserbranch";
    public static final String URL_DISPLAY_CENTER = BASE_URL + "user/getallbranches";
    public static final String URL_DISPLAY_STUDENTS = BASE_URL + "user/getallStudentsByBatchID";
    public static final String URL_ADDTEMPLATE = BASE_URL + "user/addTemplates";
    public static final String URL_UPDATETEMPLATEDETAILS = BASE_URL + "user/updatetemplatedetails";
    public static final String URL_VIEWTEMPLATE = BASE_URL + "user/getallTemplates";
    public static final String URL_GETALLBATCHTRAINERBYPREFIX = BASE_URL + "user/getallBatchTrainerByPreFix";
    public static final String URL_ADD_STUDENT_FEESDETAILS = BASE_URL + "user/addStudentFeesDetails";
    public static final String URL_GETALLSTUDENTSBYID1 = BASE_URL + "user/getallStudentsByPreFix";
    public static final String URL_ADD_STUDENT_INBATCH = BASE_URL + "user/addStudentInBatch";
    public static final String URL_ADDSTUDENTFROMDEMOINBATCH = BASE_URL + "user/addStudentFromDemoInBatch";
    public static final String URL_GETALL_FEES_DETAILSINBATCH = BASE_URL + "user/getallFeesDetailsInBatch";
    public static final String URL_DELETE_TEMPLATE = BASE_URL + "user/deleteTemplate";
    public static final String URL_ADDTEMPLATESCONTACTS = BASE_URL + "user/addTemplatesContacts";
    public static final String URL_GETALLCONTACTTEMPLATES = BASE_URL + "user/getallContactTemplates";
    public static final String URL_DELETECONTACTTEMPLATE = BASE_URL + "user/deleteContactTemplate";
    public static final String URL_UPDATECONTACTTEMPLATEDETAILS = BASE_URL + "user/updatecontacttemplatedetails";
    public static final String URL_TAKEATTENDENCEBYBTACH = BASE_URL + "user/takeAttendenceByBtach";
    public static final String URL_DISCONTINUEBATCHSTUDENT = BASE_URL + "user/discontinueBatchStudent";
    public static final String URL_DELETEPRESENTDETAILS = BASE_URL + "user/deletePresentDetails";
    public static final String URL_AVAILABLESTUDENT = BASE_URL + "user/availableStudent";
    public static final String URL_DISPLAY_FEEDBACK_STUDENTS = BASE_URL + "user/getStudentFeedbackDetails";
    public static final String URL_UPDATEMOSSTUDENT = BASE_URL + "user/updateMOSStudent";
    public static final String URL_GETSTUDENTSCASHBACK = BASE_URL + "user/getStudentsCashBack";
    public static final String URL_GETFULLFEESSTATUS = BASE_URL + "user/getFullFeesStatus";
    public static final String URL_DISCONTINUENOTESSTUDENT = BASE_URL + "user/discontinueNotesStudent";
    public static final String URL_GETALLATTENDANCEDETAILSINBATCH = BASE_URL + "user/getallAttendanceDetailsInBatch";
    public static final String URL_GETALLCERTIFICATEDISPLAYVIEW= BASE_URL + "user/getallCertificateDisplayView";
    public static final String URL_UPDATEBATCHEND = BASE_URL + "user/updateBatchEnd";
    public static final String URL_GETSTUDENTDISCONTINUEDETAILSINBATCH = BASE_URL + "user/getStudentDiscontinueDetailsInBatch";
    public static final String URL_UPDATE_USER_COMMENT_DETAILS = BASE_URL + "user/updateUserCommentDetails";
    public static final String URL_GETUSERCOMMENTDETAILS = BASE_URL + "user/getUserCommentDetails";
    public static final String URL_DAYPREFRENCE_DETAILS = BASE_URL + "user/dayprefrence";
    public static final String URL_SEND_DAYPREFRENCE_DETAILS = BASE_URL + "user/savedayprefrence";
    public static final String URL_DELETE_DAYPREFRENCE = BASE_URL + "user/deletedayprefrence";
    public static final String URL_UPDATEBATCHDETAILS = BASE_URL + "user/updatebatchdetails";
    public static final String URL_GETALLBATCHMODIFYBYPREFIX = BASE_URL + "user/getallBatchModifyByPreFix";
    public static final String URL_GETUSERNAMEPASSSMS = BASE_URL + "user/getUserNamePassSMS";
    public static final String URL_CHECKCERTIFICATEENTRY= BASE_URL + "user/checkCertificateEntry";
    public static final String URL_ADDACTUALBATCHTIMINGS = BASE_URL + "user/addActualBatchTimings";
    public static final String URL_GETVERIFYCODEFORWEB = BASE_URL + "user/getVerifyCodeForWeb";
    public static final String URL_GETVERIFYSTUDENTINBATCH = BASE_URL + "user/getVerifyStudentInBatch";
    public static final String URL_UPDATEBTACHCODE = BASE_URL + "user/updateBtachCode";
    public static final String URL_GETALLSTUDENTTRANSFERBATCHTRAINERBYPREFIX = BASE_URL + "user/getallStudentTransferBatchTrainerByPreFix";
    public static final String URL_ADDSTUDENTINBATCHTRANSFERFROMOLDBATCH = BASE_URL + "user/addStudentInBatchTransferFromOldBatch";
    public static final String URL_GETAVAILABLEISTIMEINRANGE = BASE_URL + "user/getAvailableIsTimeInRange";
    public static final String URL_GETALLONGOINGTRAINER = BASE_URL + "user/getallOngoingTrainer";
    public static final String URL_GETALLTRAINERONGOINGBRANCH = BASE_URL + "user/getallTrainerOngoingBranch";
    public static final String URL_GETALLTRAINERONGOINGBATCHES = BASE_URL + "user/getallTrainerOngoingBatches";
    public static final String URL_GETALLONGOINGBATCHES = BASE_URL + "user/getallOngoingBatches";
    public static final String URL_GETALLTOTALBATCHTIME = BASE_URL + "user/getallTotalBatchTime";
    public static final String URL_GETALLATTENDANCEPERC = BASE_URL + "user/getallAttendancePerc";
    public static final String URL_GETALLACTUALBATCHDETAILS = BASE_URL + "user/getallActualBatchDetails";
    public static final String URL_GETALLFEESDETAILSINBATCHBYADMIN = BASE_URL + "user/getallFeesDetailsInBatchByAdmin";
    public static final String URL_GETTEMPLATETEXTLOCATIONID = BASE_URL + "user/getTemplateTextLocationID";
    public static final String URL_GETTEMPLATETEXTCOURSEID = BASE_URL + "user/getTemplateTextCourseID";
    public static final String URL_UPDATEDUEDATEREMINDERUSINGSTOREPROC = BASE_URL + "user/UpdateDueDateReminderUsingStoreProc";
    public static final String URL_GETJUSTDIALFEEDBACKID = BASE_URL + "user/getJustDialFeedbackID";
    public static final String URL_GETAVAILABLEUSERROLES = BASE_URL + "user/getAvailableUserRoles";
    public static final String URL_ADDUSERCOMMENTDETAILS = BASE_URL + "user/addUserCommentDetails";
    public static final String URL_GETALLCOMMENTSDETAILSINBATCH = BASE_URL + "user/getallCommentsDetailsInBatch";
    public static final String URL_DELETEUSERCOMMENT = BASE_URL + "user/deleteusercomment";
    public static final String URL_GETALLBANKRDETAILS = BASE_URL + "user/getallBankRDetails";
    public static final String URL_GETALLFEESRDETAILSINBATCH = BASE_URL + "user/getallFeesRDetailsInBatch";
    public static final String URL_GETALLBANKDETAILS = BASE_URL + "user/getallBankDetails";
    public static final String URL_USERDUEDATESYNCDATA = BASE_URL + "user/userDueDateSyncData";
    public static final String URL_ADDBANKRECEIPT = BASE_URL + "user/addBankReceipt";
    public static final String URL_DELETEBANKDETAILS = BASE_URL + "user/deleteBankDetails";
    public static final String URL_GETALLBATCHCLASSDETAILS = BASE_URL + "user/getallBatchClassDetails";
    public static final String URL_GETZIPFILENAMES = BASE_URL + "user/getZipFileNames";
    public static final String URL_UPDATEREFBATCHNO = BASE_URL + "user/updateRefBatchNo";
    public static final String URL_ADDCERTIFICATEDETAILSSYNCDATA = BASE_URL + "user/addCertificateDetailsSyncData";
    public static final String URL_GETALLVWBATCHSTARTINGDETAILSF = BASE_URL + "user/getAllvwBatchStartingDetailsF";
    public static final String URL_GETAVAILABLEFEESOPTIONS = BASE_URL + "user/getAvailableFeesOptions";
    public static final String URL_GETAVAILABLESUPERADMIN = BASE_URL + "user/getAvailableSuperAdmin";
    public static final String URL_GETEMAILSCOUNT = BASE_URL + "user/getEmailsCount";
    public static final String URL_AVAILABLEENQUIRYUSER = BASE_URL + "user/availableEnquiryUser";
    public static final String URL_DELETESTUDENTBYADMIN = BASE_URL + "user/deleteStudentByAdmin";
    public static final String URL_UPDATEBANKINGSTATUS = BASE_URL + "user/updateBankingStatus";
    public static final String URL_DELETESTUDENTFEESBYADMIN = BASE_URL + "user/deleteStudentFeesByAdmin";
    public static final String URL_GETALLCOURSES = BASE_URL + "user/getallCourses";
    public static final String URL_GETALLCOURSESDEMO = BASE_URL + "user/getallCoursesDemo";
    public static final String URL_ADDSTUDENTINBATCHDEMOBYTRAINER = BASE_URL + "user/addStudentInBatchDemoByTrainer";
    public static final String URL_ADDSTUDENTINNOBATCHDEMOBYTRAINER = BASE_URL + "user/addStudentInNOBatchDemoByTrainer";
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "AFCKS Images";
    public static String DATA_ENTERLEVEL_COURSES = "";
    public static String DATA_SPLIZATION_COURSES = "";

    public static String DATA_MOVE_FROM_LOCATION = "";
    public static ArrayList<String> VALUE = new ArrayList<String>();
    // public static final String SMS_ORIGIN = "WAVARM";
    public static final String SMS_ORIGIN = "AFCKST";
}
