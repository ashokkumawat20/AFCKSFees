package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class StudentsDiscontinueDetailsDAO {
    String batch_id="";
    String discontinue_reason="";
    String discontinue_date="";

    public StudentsDiscontinueDetailsDAO()
    {
    }

    public StudentsDiscontinueDetailsDAO(String batch_id, String discontinue_reason, String discontinue_date) {
        this.batch_id = batch_id;
        this.discontinue_reason = discontinue_reason;
        this.discontinue_date = discontinue_date;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getDiscontinue_reason() {
        return discontinue_reason;
    }

    public void setDiscontinue_reason(String discontinue_reason) {
        this.discontinue_reason = discontinue_reason;
    }

    public String getDiscontinue_date() {
        return discontinue_date;
    }

    public void setDiscontinue_date(String discontinue_date) {
        this.discontinue_date = discontinue_date;
    }
}
