package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class ActualBatchDetailsDAO {
    String id = "";
    String batch_id = "";
    String batch_date = "";
    String todays_topics = "";
    String next_class_topics = "";
    String next_class_date = "";
    String date_time = "";
    String notes = "";
    String course_name = "";
    String date_time_notes = "";
    String numbers = "";

    public ActualBatchDetailsDAO() {
    }

    public ActualBatchDetailsDAO(String id, String batch_id, String batch_date, String todays_topics, String next_class_topics, String next_class_date, String date_time, String notes, String course_name, String date_time_notes, String numbers) {
        this.id = id;
        this.batch_id = batch_id;
        this.batch_date = batch_date;
        this.todays_topics = todays_topics;
        this.next_class_topics = next_class_topics;
        this.next_class_date = next_class_date;
        this.date_time = date_time;
        this.notes = notes;
        this.course_name = course_name;
        this.date_time_notes = date_time_notes;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getBatch_date() {
        return batch_date;
    }

    public void setBatch_date(String batch_date) {
        this.batch_date = batch_date;
    }

    public String getTodays_topics() {
        return todays_topics;
    }

    public void setTodays_topics(String todays_topics) {
        this.todays_topics = todays_topics;
    }

    public String getNext_class_topics() {
        return next_class_topics;
    }

    public void setNext_class_topics(String next_class_topics) {
        this.next_class_topics = next_class_topics;
    }

    public String getNext_class_date() {
        return next_class_date;
    }

    public void setNext_class_date(String next_class_date) {
        this.next_class_date = next_class_date;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getDate_time_notes() {
        return date_time_notes;
    }

    public void setDate_time_notes(String date_time_notes) {
        this.date_time_notes = date_time_notes;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
