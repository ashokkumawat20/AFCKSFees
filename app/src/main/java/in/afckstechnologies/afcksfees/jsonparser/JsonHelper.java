package in.afckstechnologies.afcksfees.jsonparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.afckstechnologies.afcksfees.model.ActualBatchDetailsDAO;
import in.afckstechnologies.afcksfees.model.AttendancePercDetailsDAO;
import in.afckstechnologies.afcksfees.model.BankingdetailsDAO;
import in.afckstechnologies.afcksfees.model.BatchesForStudentsDAO;
import in.afckstechnologies.afcksfees.model.CenterDAO;
import in.afckstechnologies.afcksfees.model.CertificateDisplayDetailsDAO;
import in.afckstechnologies.afcksfees.model.CommentModeDAO;
import in.afckstechnologies.afcksfees.model.DayPrefrenceDAO;
import in.afckstechnologies.afcksfees.model.FileNamesDAO;
import in.afckstechnologies.afcksfees.model.NewCoursesDAO;
import in.afckstechnologies.afcksfees.model.OnGoingBatchDAO;
import in.afckstechnologies.afcksfees.model.StudentsAttendanceDetailsDAO;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.model.StudentsDiscontinueDetailsDAO;
import in.afckstechnologies.afcksfees.model.StudentsFeesDetailsDAO;
import in.afckstechnologies.afcksfees.model.TemplatesContactDAO;
import in.afckstechnologies.afcksfees.model.TotalBatchTimeDetailsDAO;
import in.afckstechnologies.afcksfees.model.UserClassesDAO;


/**
 * Created by admin on 2/18/2017.
 */

public class JsonHelper {

    private ArrayList<StudentsDAO> studentsDAOArrayList = new ArrayList<StudentsDAO>();
    private StudentsDAO studentsDAO;
    long serialNo = 001;
    int id = 1;
    int temp;

    private ArrayList<StudentsFeesDetailsDAO> studentsFeesDetailsDAOArrayList = new ArrayList<StudentsFeesDetailsDAO>();
    private StudentsFeesDetailsDAO studentsFeesDetailsDAO;
    private ArrayList<TemplatesContactDAO> templatesContactDAOArrayList = new ArrayList<TemplatesContactDAO>();
    private TemplatesContactDAO templatesContactDAO;

    private ArrayList<CenterDAO> centerDAOArrayList = new ArrayList<CenterDAO>();
    private CenterDAO centerDAO;

    private ArrayList<OnGoingBatchDAO> onGoingBatchDAOArrayList = new ArrayList<OnGoingBatchDAO>();
    private OnGoingBatchDAO onGoingBatchDAO;
    private ArrayList<DayPrefrenceDAO> DayPrefrenceDAOArrayList = new ArrayList<DayPrefrenceDAO>();
    private DayPrefrenceDAO dayPrefrenceDAO;

    private ArrayList<NewCoursesDAO> newCoursesDAOArrayList = new ArrayList<NewCoursesDAO>();
    private NewCoursesDAO newCoursesDAO;

    private ArrayList<StudentsDiscontinueDetailsDAO> studentsDiscontinueDetailsDAOs = new ArrayList<StudentsDiscontinueDetailsDAO>();
    private StudentsDiscontinueDetailsDAO studentsDiscontinueDetailsDAO;

    private ArrayList<StudentsAttendanceDetailsDAO> studentsAttendanceDetailsDAOArrayList = new ArrayList<StudentsAttendanceDetailsDAO>();
    private StudentsAttendanceDetailsDAO studentsAttendanceDetailsDAO;

    private ArrayList<TotalBatchTimeDetailsDAO> totalBatchTimeDetailsDAOS = new ArrayList<TotalBatchTimeDetailsDAO>();
    private TotalBatchTimeDetailsDAO totalBatchTimeDetailsDAO;

    private ArrayList<AttendancePercDetailsDAO> attendancePercDetailsDAOS = new ArrayList<AttendancePercDetailsDAO>();
    private AttendancePercDetailsDAO attendancePercDetailsDAO;

    private ArrayList<ActualBatchDetailsDAO> actualBatchDetailsDAOS = new ArrayList<ActualBatchDetailsDAO>();
    private ActualBatchDetailsDAO actualBatchDetailsDAO;

    private ArrayList<CommentModeDAO> commentModeDAOArrayList = new ArrayList<CommentModeDAO>();
    private CommentModeDAO commentModeDAO;

    private ArrayList<BankingdetailsDAO> bankingdetailsDAOArrayList = new ArrayList<BankingdetailsDAO>();
    private BankingdetailsDAO bankingdetailsDAO;
    private ArrayList<UserClassesDAO> userClassesDAOArrayList = new ArrayList<UserClassesDAO>();
    private UserClassesDAO userClassesDAO;
    private ArrayList<FileNamesDAO> fileNamesDAOArrayList = new ArrayList<FileNamesDAO>();
    private FileNamesDAO fileNamesDAO;

    private ArrayList<BatchesForStudentsDAO> batchDAOArrayList = new ArrayList<BatchesForStudentsDAO>();
    private BatchesForStudentsDAO batchDAO;

    private ArrayList<CertificateDisplayDetailsDAO> certificateDisplayDetailsDAOArrayList = new ArrayList<CertificateDisplayDetailsDAO>();
    private CertificateDisplayDetailsDAO certificateDisplayDetailsDAO;

    //studentPaser
    public ArrayList<StudentsDAO> parseStudentList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                studentsDAO = new StudentsDAO();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsDAO.setBatchid(json_data.getString("batchid"));
                studentsDAO.setSbd_id(json_data.getString("sbd_id"));
                studentsDAO.setEmail_id(json_data.getString("email_id"));
                studentsDAO.setTrainer_email_id(json_data.getString("trainer_email_id"));
                studentsDAO.setStudents_Name(json_data.getString("Students_Name"));
                studentsDAO.setStudent_certificate_name(json_data.getString("StudentCertificateName"));
                studentsDAO.setFirst_name(json_data.getString("first_name"));
                studentsDAO.setUser_id(json_data.getString("id"));
                studentsDAO.setMobile_no(json_data.getString("mobile_no"));
                studentsDAO.setTotalFees(json_data.getString("TotalFees"));
                studentsDAO.setGender(json_data.getString("gender"));
                studentsDAO.setCorporate(json_data.getString("student_batch_cat"));
                studentsDAO.setNotes_id(json_data.getString("notes_id"));
                studentsDAO.setDiscontinued(json_data.getString("Discontinued"));
                studentsDAO.setStatus(json_data.getString("Status"));
                studentsDAO.setNotes(json_data.getString("Notes"));
                studentsDAO.setPrevious_attendance(json_data.getString("previous_attendance"));
                studentsDAO.setDue_amount(json_data.getString("due_amount"));
                studentsDAO.setNext_due_date(json_data.getString("next_due_date"));
                studentsDAO.setReminder_count(json_data.getString("reminder_count"));
                studentsDAO.setDiscount(json_data.getString("discount"));
                studentsDAO.setStudent_batch_cat(json_data.getString("student_batch_cat"));
                studentsDAO.setNumbers("" + sequence);
                studentsDAOArrayList.add(studentsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsDAOArrayList;
    }

    //studentPaser
    public ArrayList<StudentsDAO> parseShowStudentList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                studentsDAO = new StudentsDAO();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsDAO.setBranch_id(json_data.getString("branch_id"));
                studentsDAO.setCourse_id(json_data.getString("course_id"));
                studentsDAO.setEmail_id(json_data.getString("email_id"));
                studentsDAO.setGender(json_data.getString("gender"));
                studentsDAO.setStudents_Name(json_data.getString("Student_Name"));
                studentsDAO.setUser_id(json_data.getString("user_id"));
                studentsDAO.setMobile_no(json_data.getString("mobile_no"));
                studentsDAO.setFirst_name(json_data.getString("first_name"));
                studentsDAO.setSourse(json_data.getString("Source"));
                studentsDAO.setNotes(json_data.getString("Notes"));
                studentsDAO.setNumbers("" + sequence);
                studentsDAOArrayList.add(studentsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsDAOArrayList;
    }

    //templatePaser
    public ArrayList<TemplatesContactDAO> parseTemplateList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                templatesContactDAO = new TemplatesContactDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                templatesContactDAO.setID(json_data.getString("ID"));
                templatesContactDAO.setSubject(json_data.getString("Subject"));
                templatesContactDAO.setTag(json_data.getString("tag"));
                templatesContactDAO.setTemplate_Text(json_data.getString("Template_Text"));
                templatesContactDAOArrayList.add(templatesContactDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return templatesContactDAOArrayList;
    }

    //studentlistfeesdetailsrPaser
    public ArrayList<StudentsFeesDetailsDAO> parseStudentFessDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                studentsFeesDetailsDAO = new StudentsFeesDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsFeesDetailsDAO.setId(json_data.getString("id"));
                studentsFeesDetailsDAO.setDateTimeOfEntry(json_data.getString("DateTimeOfEntry"));
                studentsFeesDetailsDAO.setFeeMode(json_data.getString("FeeMode"));
                studentsFeesDetailsDAO.setNote(json_data.getString("Note"));
                studentsFeesDetailsDAO.setReceivedBy(json_data.getString("ReceivedBy"));
                studentsFeesDetailsDAO.setUserName(json_data.getString("UserName"));
                studentsFeesDetailsDAO.setFees(json_data.getString("Fees"));
                studentsFeesDetailsDAO.setFirst_name(json_data.getString("first_name"));
                studentsFeesDetailsDAO.setLast_name(json_data.getString("last_name"));
                studentsFeesDetailsDAO.setEmail_id(json_data.getString("email_id"));
                studentsFeesDetailsDAO.setMobileNo(json_data.getString("MobileNo"));
                studentsFeesDetailsDAOArrayList.add(studentsFeesDetailsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsFeesDetailsDAOArrayList;
    }


    //centerPaser
    public ArrayList<CenterDAO> parseCenterList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                centerDAO = new CenterDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                centerDAO.setId(json_data.getString("id"));
                centerDAO.setBranch_name(json_data.getString("branch_name"));
                centerDAO.setAddress(json_data.getString("address"));
                centerDAO.setIsselected(json_data.getString("isselected"));
                centerDAOArrayList.add(centerDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return centerDAOArrayList;
    }


    //centerPaser
    public ArrayList<DayPrefrenceDAO> parseDayPrefrenceList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                dayPrefrenceDAO = new DayPrefrenceDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                dayPrefrenceDAO.setId(json_data.getString("id"));
                dayPrefrenceDAO.setPrefrence(json_data.getString("Prefrence"));
                dayPrefrenceDAO.setIsselected(json_data.getString("isselected"));
                DayPrefrenceDAOArrayList.add(dayPrefrenceDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return DayPrefrenceDAOArrayList;
    }

    //newcoursesPaser
    public ArrayList<NewCoursesDAO> parseNewCoursesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                newCoursesDAO = new NewCoursesDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                newCoursesDAO.setId(json_data.getString("id"));
                newCoursesDAO.setCourse_type_id(json_data.getString("course_type_id"));
                newCoursesDAO.setCourse_code(json_data.getString("course_code"));
                newCoursesDAO.setCourse_name(json_data.getString("course_name"));
                newCoursesDAO.setTime_duration(json_data.getString("time_duration"));
                newCoursesDAO.setPrerequisite(json_data.getString("prerequisite"));
                newCoursesDAO.setRecommonded(json_data.getString("recommonded"));
                newCoursesDAO.setFees(json_data.getString("fees"));
                newCoursesDAO.setIsselected(json_data.getString("isselected"));
                newCoursesDAOArrayList.add(newCoursesDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newCoursesDAOArrayList;
    }

    //fixedAssestsPaser
    public ArrayList<OnGoingBatchDAO> parseOnGoingBatchesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                onGoingBatchDAO = new OnGoingBatchDAO();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                onGoingBatchDAO.setBatch_code(json_data.getString("batch_code"));
                onGoingBatchDAO.setCourse_name(json_data.getString("course_name"));
                onGoingBatchDAO.setTrainer_id(json_data.getString("trainer_id"));
                onGoingBatchDAO.setSt_date(json_data.getString("st_date"));
                onGoingBatchDAO.setTimings(json_data.getString("timings"));
                onGoingBatchDAO.setFrequency(json_data.getString("frequency"));
                onGoingBatchDAO.setBranch_name(json_data.getString("branch_name"));
                onGoingBatchDAO.setTotalClasses(json_data.getString("TotalClasses"));
                onGoingBatchDAO.setTotalTime(json_data.getString("TotalTime"));
                onGoingBatchDAO.setStudentsInBatch(json_data.getString("StudentsInBatch"));
                onGoingBatchDAO.setActiveStudents(json_data.getString("ActiveStudents"));
                onGoingBatchDAO.setDiscontinuedStudents(json_data.getString("DiscontinuedStudents"));
                onGoingBatchDAO.setActivePerc(json_data.getString("ActivePerc"));
                onGoingBatchDAO.setPresentPerc(json_data.getString("PresentPerc"));
                onGoingBatchDAO.setTotalFees(json_data.getString("TotalFees"));
                onGoingBatchDAO.setMobile_no(json_data.getString("mobile_no"));
                onGoingBatchDAO.setDemoStudents(json_data.getString("DemoStudents"));
                onGoingBatchDAOArrayList.add(onGoingBatchDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return onGoingBatchDAOArrayList;
    }

    //studentlistdiscontinuedetailsrPaser
    public ArrayList<StudentsDiscontinueDetailsDAO> parseStudentDiscontinueDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                studentsDiscontinueDetailsDAO = new StudentsDiscontinueDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsDiscontinueDetailsDAO.setBatch_id(json_data.getString("BatchID"));
                if (!json_data.getString("discontinue_date").equals("null")) {
                    studentsDiscontinueDetailsDAO.setDiscontinue_date(json_data.getString("discontinue_date"));
                }
                if (!json_data.getString("discontinue_reason").equals("null")) {
                    studentsDiscontinueDetailsDAO.setDiscontinue_reason(json_data.getString("discontinue_reason"));
                }
                studentsDiscontinueDetailsDAOs.add(studentsDiscontinueDetailsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsDiscontinueDetailsDAOs;
    }

    //studentlistattendancedetailsrPaser
    public ArrayList<StudentsAttendanceDetailsDAO> parseStudentAttendanceDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                studentsAttendanceDetailsDAO = new StudentsAttendanceDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsAttendanceDetailsDAO.setBatch_id(json_data.getString("batch_id"));
                studentsAttendanceDetailsDAO.setStudent_name(json_data.getString("student_name"));
                studentsAttendanceDetailsDAO.setAttendance(json_data.getString("attendance"));
                studentsAttendanceDetailsDAO.setAttendanceDate(json_data.getString("AttendanceDate"));
                studentsAttendanceDetailsDAO.setNumbers("" + sequence);
                studentsAttendanceDetailsDAOArrayList.add(studentsAttendanceDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsAttendanceDetailsDAOArrayList;
    }

    //studentlistcertificatedetailsrPaser
    public ArrayList<CertificateDisplayDetailsDAO> parseStudentCertificateDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", i + 1);
                certificateDisplayDetailsDAO = new CertificateDisplayDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                certificateDisplayDetailsDAO.setId(json_data.getString("id"));
                certificateDisplayDetailsDAO.setStudent_id(json_data.getString("student_id"));
                certificateDisplayDetailsDAO.setStudent_Name(json_data.getString("Student_Name"));
                certificateDisplayDetailsDAO.setDispatch(json_data.getString("dispatch"));
                certificateDisplayDetailsDAO.setNumbers("" + sequence);
                certificateDisplayDetailsDAOArrayList.add(certificateDisplayDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return certificateDisplayDetailsDAOArrayList;
    }

    //TotalBatchTimedetailsrPaser
    public ArrayList<TotalBatchTimeDetailsDAO> parseTotalBatchTimeDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                totalBatchTimeDetailsDAO = new TotalBatchTimeDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                totalBatchTimeDetailsDAO.setBatch_id(json_data.getString("batch_id"));
                totalBatchTimeDetailsDAO.setBatch_date(json_data.getString("batch_date"));
                totalBatchTimeDetailsDAO.setTotalTime(json_data.getString("TotalTime"));
                totalBatchTimeDetailsDAO.setNumbers("" + sequence);
                totalBatchTimeDetailsDAOS.add(totalBatchTimeDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return totalBatchTimeDetailsDAOS;
    }

    //TotalBatchTimedetailsrPaser
    public ArrayList<AttendancePercDetailsDAO> parseAttendancePercDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                attendancePercDetailsDAO = new AttendancePercDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                attendancePercDetailsDAO.setBatch_id(json_data.getString("batch_id"));
                attendancePercDetailsDAO.setPresentDate(json_data.getString("PresentDate"));
                attendancePercDetailsDAO.setAttendancePerc(json_data.getString("AttendancePerc"));
                attendancePercDetailsDAO.setNumbers("" + sequence);
                attendancePercDetailsDAOS.add(attendancePercDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return attendancePercDetailsDAOS;
    }

    //TotalBatchTimedetailsrPaser
    public ArrayList<ActualBatchDetailsDAO> parseActualBatchDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                actualBatchDetailsDAO = new ActualBatchDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                actualBatchDetailsDAO.setId(json_data.getString("id"));
                actualBatchDetailsDAO.setBatch_id(json_data.getString("batch_id"));
                actualBatchDetailsDAO.setBatch_date(json_data.getString("batch_date"));
                actualBatchDetailsDAO.setDate_time(json_data.getString("date_time"));
                actualBatchDetailsDAO.setTodays_topics(json_data.getString("todays_topics"));
                actualBatchDetailsDAO.setNext_class_topics(json_data.getString("next_class_topics"));
                actualBatchDetailsDAO.setNext_class_date(json_data.getString("next_class_date"));
                actualBatchDetailsDAO.setNotes(json_data.getString("notes"));
                actualBatchDetailsDAO.setCourse_name(json_data.getString("course_name"));
                actualBatchDetailsDAO.setNumbers("" + sequence);
                actualBatchDetailsDAOS.add(actualBatchDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return actualBatchDetailsDAOS;
    }

    //studentlistfeesdetailsrPaser
    public ArrayList<CommentModeDAO> parseStudentCommentDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                commentModeDAO = new CommentModeDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                commentModeDAO.setId(json_data.getString("id"));
                commentModeDAO.setStudent_comments(json_data.getString("student_comment"));
                commentModeDAO.setDate_comments(json_data.getString("display_date"));
                commentModeDAOArrayList.add(commentModeDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commentModeDAOArrayList;
    }

    //studentlistfeesdetailsrPaser
    public ArrayList<CommentModeDAO> parseStudentReminderDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                commentModeDAO = new CommentModeDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                commentModeDAO.setId(json_data.getString("id"));
                commentModeDAO.setStudent_comments(json_data.getString("sms"));
                commentModeDAO.setDate_comments(json_data.getString("f_reminder_date"));
                commentModeDAOArrayList.add(commentModeDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commentModeDAOArrayList;
    }

    //bankingdetailsPaser
    public ArrayList<BankingdetailsDAO> parseBankingDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                bankingdetailsDAO = new BankingdetailsDAO();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                bankingdetailsDAO.setId(json_data.getString("id"));
                bankingdetailsDAO.setRec_amount(json_data.getString("rec_amount"));
                bankingdetailsDAO.setAdj_amount(json_data.getString("adj_amount"));
                bankingdetailsDAO.setEntered_by(json_data.getString("entered_by"));
                bankingdetailsDAO.setTrans_date(json_data.getString("trans_date"));
                bankingdetailsDAO.setTrans_ref(json_data.getString("trans_ref"));
                bankingdetailsDAO.setTrans_type(json_data.getString("trans_type"));
                bankingdetailsDAO.setStatus(json_data.getString("status"));
                bankingdetailsDAO.setNumbers("" + sequence);
                bankingdetailsDAOArrayList.add(bankingdetailsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bankingdetailsDAOArrayList;
    }

    //batches
    public ArrayList<UserClassesDAO> userClassesDAOArrayList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            temp = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                userClassesDAO = new UserClassesDAO();
                String sequence = String.format("%02d", temp - i);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                userClassesDAO.setBranch_name(json_data.getString("branch_name"));
                userClassesDAO.setBatch_id(json_data.getString("batch_id"));
                userClassesDAO.setNext_class_date(json_data.getString("next_class_date"));
                userClassesDAO.setNext_class_topics(json_data.getString("next_class_topics"));
                userClassesDAO.setTodays_topics(json_data.getString("todays_topics"));
                userClassesDAO.setBatch_date(json_data.getString("batch_date"));
                userClassesDAO.setTimings(json_data.getString("timings"));
                userClassesDAO.setFrequency(json_data.getString("frequency"));
                userClassesDAO.setTrainer_name(json_data.getString("trainer_name"));
                userClassesDAO.setLecture_count(json_data.getString("lecture_count"));
                userClassesDAO.setLatitude(json_data.getString("latitude"));
                userClassesDAO.setLongitude(json_data.getString("longitude"));
                userClassesDAO.setPath(json_data.getString("path"));
                userClassesDAO.setFile_names(json_data.getString("file_names").replace(", ", "\n\n").trim());
                userClassesDAO.setNumbers(sequence);
                userClassesDAOArrayList.add(userClassesDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userClassesDAOArrayList;
    }

    public ArrayList<FileNamesDAO> parseImagePathList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("filesname")) {
                JSONArray jsonArray = jsonObject.getJSONArray("filesname");
                for (int i = 0; i < jsonArray.length(); i++) {
                    fileNamesDAO = new FileNamesDAO();
                    fileNamesDAO.setName(jsonArray.getString(i));
                    fileNamesDAOArrayList.add(fileNamesDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileNamesDAOArrayList;
    }

    //comingbatchPaser
    public ArrayList<BatchesForStudentsDAO> comingBatchList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    batchDAO = new BatchesForStudentsDAO();
                    batchDAO.setId(object.getString("id"));
                    batchDAO.setCourse_id(object.getString("course_id"));
                    batchDAO.setBranch_id(object.getString("branch_id"));
                    batchDAO.setBatch_code(object.getString("batch_code"));
                    batchDAO.setStart_date(object.getString("start_date"));
                    batchDAO.setTimings(object.getString("timings"));
                    batchDAO.setNotes(object.getString("Notes"));
                    batchDAO.setFrequency(object.getString("frequency"));
                    batchDAO.setFees(object.getString("fees"));
                    batchDAO.setDuration(object.getString("duration"));
                    batchDAO.setCourse_name(object.getString("course_name"));
                    batchDAO.setBranch_name(object.getString("branch_name"));
                    batchDAO.setBatchtype(object.getString("batchtype"));
                    batchDAO.setCompletion_status(object.getString("completion_status"));
                    batchDAO.setBatch_end_date(object.getString("batch_end_date"));
                    batchDAO.setTrainer_mail_id(object.getString("email_id"));
                    batchDAO.setTrainer_mobile_no(object.getString("mobile_no"));
                    batchDAO.setFaculty_Name(object.getString("faculty_Name"));
                    batchDAO.setAttendance_marked(object.getString("attendance_marked"));
                    batchDAO.setRef_batch(object.getString("ref_batch"));
                    batchDAO.setMeeting_link(object.getString("meeting_link"));
                    batchDAO.setWa_invite_link(object.getString("wa_invite_link"));
                    batchDAO.setCode(object.getString("Code"));
                    batchDAOArrayList.add(batchDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return batchDAOArrayList;
    }
}
