package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok Kumawat on 5/9/2018.
 */

public class OnGoingBatchDAO {
    String batch_code = "";
    String course_name = "";
    String trainer_id = "";
    String st_date = "";
    String timings = "";
    String frequency = "";
    String branch_name = "";
    String TotalClasses="";
    String TotalTime="";
    String StudentsInBatch="";
    String ActiveStudents="";
    String DiscontinuedStudents="";
    String DemoStudents="";
    String ActivePerc="";
    String PresentPerc="";
    String TotalFees="";
    String mobile_no="";

public OnGoingBatchDAO()
{}

    public OnGoingBatchDAO(String batch_code, String course_name, String trainer_id, String st_date, String timings, String frequency, String branch_name, String totalClasses, String totalTime, String studentsInBatch, String activeStudents, String discontinuedStudents, String demoStudents, String activePerc, String presentPerc, String totalFees, String mobile_no) {
        this.batch_code = batch_code;
        this.course_name = course_name;
        this.trainer_id = trainer_id;
        this.st_date = st_date;
        this.timings = timings;
        this.frequency = frequency;
        this.branch_name = branch_name;
        TotalClasses = totalClasses;
        TotalTime = totalTime;
        StudentsInBatch = studentsInBatch;
        ActiveStudents = activeStudents;
        DiscontinuedStudents = discontinuedStudents;
        DemoStudents = demoStudents;
        ActivePerc = activePerc;
        PresentPerc = presentPerc;
        TotalFees = totalFees;
        this.mobile_no = mobile_no;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getTotalClasses() {
        return TotalClasses;
    }

    public void setTotalClasses(String totalClasses) {
        TotalClasses = totalClasses;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getStudentsInBatch() {
        return StudentsInBatch;
    }

    public void setStudentsInBatch(String studentsInBatch) {
        StudentsInBatch = studentsInBatch;
    }

    public String getActiveStudents() {
        return ActiveStudents;
    }

    public void setActiveStudents(String activeStudents) {
        ActiveStudents = activeStudents;
    }

    public String getDiscontinuedStudents() {
        return DiscontinuedStudents;
    }

    public void setDiscontinuedStudents(String discontinuedStudents) {
        DiscontinuedStudents = discontinuedStudents;
    }

    public String getDemoStudents() {
        return DemoStudents;
    }

    public void setDemoStudents(String demoStudents) {
        DemoStudents = demoStudents;
    }

    public String getActivePerc() {
        return ActivePerc;
    }

    public void setActivePerc(String activePerc) {
        ActivePerc = activePerc;
    }

    public String getPresentPerc() {
        return PresentPerc;
    }

    public void setPresentPerc(String presentPerc) {
        PresentPerc = presentPerc;
    }

    public String getTotalFees() {
        return TotalFees;
    }

    public void setTotalFees(String totalFees) {
        TotalFees = totalFees;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
