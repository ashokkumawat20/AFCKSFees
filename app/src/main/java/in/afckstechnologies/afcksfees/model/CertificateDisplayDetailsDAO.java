package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class CertificateDisplayDetailsDAO {
    String id = "";
    String student_id = "";
    String Student_Name = "";
    String dispatch = "";
    String numbers = "";

    public CertificateDisplayDetailsDAO() {
    }

    public CertificateDisplayDetailsDAO(String id, String student_id, String student_Name, String dispatch, String numbers) {
        this.id = id;
        this.student_id = student_id;
        Student_Name = student_Name;
        this.dispatch = dispatch;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
