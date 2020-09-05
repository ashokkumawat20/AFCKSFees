package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class StudentCertificateDetailsDAO {


    String Student_Name = "";
    String Student_certificate_name = "";


    public StudentCertificateDetailsDAO() {
    }

    public StudentCertificateDetailsDAO(String student_Name, String student_certificate_name) {
        Student_Name = student_Name;
        Student_certificate_name = student_certificate_name;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getStudent_certificate_name() {
        return Student_certificate_name;
    }

    public void setStudent_certificate_name(String student_certificate_name) {
        Student_certificate_name = student_certificate_name;
    }
}
