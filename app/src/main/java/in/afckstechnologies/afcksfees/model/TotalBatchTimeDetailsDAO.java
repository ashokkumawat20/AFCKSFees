package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 4/18/2017.
 */

public class TotalBatchTimeDetailsDAO {
    String batch_id = "";
    String batch_date = "";
    String TotalTime = "";
    String numbers = "";

    public TotalBatchTimeDetailsDAO() {
    }

    public TotalBatchTimeDetailsDAO(String batch_id, String batch_date, String totalTime, String numbers) {
        this.batch_id = batch_id;
        this.batch_date = batch_date;
        TotalTime = totalTime;
        this.numbers = numbers;
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

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
