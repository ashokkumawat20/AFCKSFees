package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class StudentsAttendanceDetailsDAO {
    String batch_id="";
    String attendance="";
    String student_name="";
    String AttendanceDate="";
    String numbers="";
    public StudentsAttendanceDetailsDAO()
    {
    }

    public StudentsAttendanceDetailsDAO(String batch_id, String attendance, String student_name, String attendanceDate, String numbers) {
        this.batch_id = batch_id;
        this.attendance = attendance;
        this.student_name = student_name;
        AttendanceDate = attendanceDate;
        this.numbers = numbers;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
