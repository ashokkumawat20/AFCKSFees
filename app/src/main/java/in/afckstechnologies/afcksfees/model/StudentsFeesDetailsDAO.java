package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class StudentsFeesDetailsDAO {
    String id="";
    String Fees="";
    String FeeMode="";
    String Note="";
    String DateTimeOfEntry="";
    String ReceivedBy="";
    String UserName="";
    String first_name="";
    String last_name="";
    String email_id="";
    String MobileNo="";


    public StudentsFeesDetailsDAO()
    {
    }


    public StudentsFeesDetailsDAO(String fees, String feeMode, String note, String dateTimeOfEntry, String receivedBy, String userName, String first_name, String last_name, String email_id, String mobileNo) {
        Fees = fees;
        FeeMode = feeMode;
        Note = note;
        DateTimeOfEntry = dateTimeOfEntry;
        ReceivedBy = receivedBy;
        UserName = userName;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        MobileNo = mobileNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFees() {
        return Fees;
    }

    public void setFees(String fees) {
        Fees = fees;
    }

    public String getFeeMode() {
        return FeeMode;
    }

    public void setFeeMode(String feeMode) {
        FeeMode = feeMode;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDateTimeOfEntry() {
        return DateTimeOfEntry;
    }

    public void setDateTimeOfEntry(String dateTimeOfEntry) {
        DateTimeOfEntry = dateTimeOfEntry;
    }

    public String getReceivedBy() {
        return ReceivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        ReceivedBy = receivedBy;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
