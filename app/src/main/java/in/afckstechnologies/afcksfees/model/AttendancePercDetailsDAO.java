package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class AttendancePercDetailsDAO {
    String batch_id = "";
    String PresentDate = "";
    String AttendancePerc = "";
    String numbers = "";

    public AttendancePercDetailsDAO() {
    }

    public AttendancePercDetailsDAO(String batch_id, String presentDate, String attendancePerc, String numbers) {
        this.batch_id = batch_id;
        PresentDate = presentDate;
        AttendancePerc = attendancePerc;
        this.numbers = numbers;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getPresentDate() {
        return PresentDate;
    }

    public void setPresentDate(String presentDate) {
        PresentDate = presentDate;
    }

    public String getAttendancePerc() {
        return AttendancePerc;
    }

    public void setAttendancePerc(String attendancePerc) {
        AttendancePerc = attendancePerc;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
