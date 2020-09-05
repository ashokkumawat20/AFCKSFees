package in.afckstechnologies.afcksfees.model;

public class StudentListDAO {
    String center_Name;
    String id;
    String first_Name;
    String last_Name;
    String mobile_No;
    String student_mail;

    public StudentListDAO() {
    }

    public StudentListDAO(String center_Name, String id, String first_Name, String last_Name, String mobile_No, String student_mail) {
        this.center_Name = center_Name;
        this.id = id;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.mobile_No = mobile_No;
        this.student_mail = student_mail;
    }

    public String getCenter_Name() {
        return center_Name;
    }

    public void setCenter_Name(String center_Name) {
        this.center_Name = center_Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    public String getMobile_No() {
        return mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        this.mobile_No = mobile_No;
    }

    public String getStudent_mail() {
        return student_mail;
    }

    public void setStudent_mail(String student_mail) {
        this.student_mail = student_mail;
    }

    @Override
    public String toString() {
        return center_Name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentListDAO) {
            StudentListDAO c = (StudentListDAO) obj;
            if (c.getCenter_Name().equals(center_Name))
                return true;
        }

        return false;
    }
}
