package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 5/4/2017.
 */

public class BatchDAO {
    String id = "";
    String course_id = "";
    String branch_id = "";
    String batch_code = "";
    String start_date = "";
    String timings = "";
    String notes = "";
    String frequency = "";
    String fees = "";
    String duration = "";
    String course_name="";
    String branch_name="";
    String batchtype="";
    String completion_status="";
    String btach_end_date="";
    String trainer_mail_id="";
    String trainer_mobile_no="";
    String trainer_name="";
    String attendance_marked="";
    String ref_batch="";
    String 	meeting_link="";
    String wa_invite_link="";
    public BatchDAO() {

    }

    public BatchDAO(String id, String course_id, String branch_id, String batch_code, String start_date, String timings, String notes, String frequency, String fees, String duration, String course_name, String branch_name, String batchtype, String completion_status, String btach_end_date, String trainer_mail_id, String trainer_mobile_no, String trainer_name, String ref_batch) {
        this.id = id;
        this.course_id = course_id;
        this.branch_id = branch_id;
        this.batch_code = batch_code;
        this.start_date = start_date;
        this.timings = timings;
        this.notes = notes;
        this.frequency = frequency;
        this.fees = fees;
        this.duration = duration;
        this.course_name = course_name;
        this.branch_name = branch_name;
        this.batchtype = batchtype;
        this.completion_status = completion_status;
        this.btach_end_date = btach_end_date;
        this.trainer_mail_id = trainer_mail_id;
        this.trainer_mobile_no = trainer_mobile_no;
        this.trainer_name = trainer_name;
        this.ref_batch = ref_batch;
    }

    public BatchDAO(String id, String course_id, String branch_id, String batch_code, String start_date, String timings, String notes, String frequency, String fees, String duration, String course_name, String branch_name, String batchtype, String completion_status, String btach_end_date, String trainer_mail_id, String trainer_mobile_no, String trainer_name, String attendance_marked, String ref_batch) {
        this.id = id;
        this.course_id = course_id;
        this.branch_id = branch_id;
        this.batch_code = batch_code;
        this.start_date = start_date;
        this.timings = timings;
        this.notes = notes;
        this.frequency = frequency;
        this.fees = fees;
        this.duration = duration;
        this.course_name = course_name;
        this.branch_name = branch_name;
        this.batchtype = batchtype;
        this.completion_status = completion_status;
        this.btach_end_date = btach_end_date;
        this.trainer_mail_id = trainer_mail_id;
        this.trainer_mobile_no = trainer_mobile_no;
        this.trainer_name = trainer_name;
        this.attendance_marked = attendance_marked;
        this.ref_batch = ref_batch;
    }

    public BatchDAO(String id, String course_id, String branch_id, String batch_code, String start_date, String timings, String notes, String frequency, String fees, String duration, String course_name, String branch_name, String batchtype, String completion_status, String btach_end_date, String trainer_mail_id, String trainer_mobile_no, String trainer_name, String attendance_marked, String ref_batch, String meeting_link, String wa_invite_link) {
        this.id = id;
        this.course_id = course_id;
        this.branch_id = branch_id;
        this.batch_code = batch_code;
        this.start_date = start_date;
        this.timings = timings;
        this.notes = notes;
        this.frequency = frequency;
        this.fees = fees;
        this.duration = duration;
        this.course_name = course_name;
        this.branch_name = branch_name;
        this.batchtype = batchtype;
        this.completion_status = completion_status;
        this.btach_end_date = btach_end_date;
        this.trainer_mail_id = trainer_mail_id;
        this.trainer_mobile_no = trainer_mobile_no;
        this.trainer_name = trainer_name;
        this.attendance_marked = attendance_marked;
        this.ref_batch = ref_batch;
        this.meeting_link = meeting_link;
        this.wa_invite_link = wa_invite_link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBatchtype() {
        return batchtype;
    }

    public void setBatchtype(String batchtype) {
        this.batchtype = batchtype;
    }

    public String getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(String completion_status) {
        this.completion_status = completion_status;
    }

    public String getBtach_end_date() {
        return btach_end_date;
    }

    public void setBtach_end_date(String btach_end_date) {
        this.btach_end_date = btach_end_date;
    }

    public String getTrainer_mail_id() {
        return trainer_mail_id;
    }

    public void setTrainer_mail_id(String trainer_mail_id) {
        this.trainer_mail_id = trainer_mail_id;
    }

    public String getTrainer_mobile_no() {
        return trainer_mobile_no;
    }

    public void setTrainer_mobile_no(String trainer_mobile_no) {
        this.trainer_mobile_no = trainer_mobile_no;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getAttendance_marked() {
        return attendance_marked;
    }

    public void setAttendance_marked(String attendance_marked) {
        this.attendance_marked = attendance_marked;
    }

    public String getRef_batch() {
        return ref_batch;
    }

    public void setRef_batch(String ref_batch) {
        this.ref_batch = ref_batch;
    }

    public String getMeeting_link() {
        return meeting_link;
    }

    public void setMeeting_link(String meeting_link) {
        this.meeting_link = meeting_link;
    }

    public String getWa_invite_link() {
        return wa_invite_link;
    }

    public void setWa_invite_link(String wa_invite_link) {
        this.wa_invite_link = wa_invite_link;
    }

    @Override
    public String toString() {
        return batch_code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BatchDAO) {
            BatchDAO c = (BatchDAO) obj;
            if (c.getBatch_code().equals(batch_code))
                return true;
        }

        return false;
    }
}
