package in.afckstechnologies.afcksfees.model;

public class BankingdetailsDAO {
    String id = "";
    String fees = "";
    String trans_ref = "";
    String trans_date = "";
    String trans_type = "";
    String rec_amount = "";
    String adj_amount = "";
    String entered_by = "";
    String numbers = "";
    String status="";
    private boolean isSelected;

    public BankingdetailsDAO() {

    }

    public BankingdetailsDAO(String id, String fees, String trans_ref, String trans_date, String trans_type, String rec_amount, String adj_amount, String entered_by) {
        this.id = id;
        this.fees = fees;
        this.trans_ref = trans_ref;
        this.trans_date = trans_date;
        this.trans_type = trans_type;
        this.rec_amount = rec_amount;
        this.adj_amount = adj_amount;
        this.entered_by = entered_by;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getTrans_ref() {
        return trans_ref;
    }

    public void setTrans_ref(String trans_ref) {
        this.trans_ref = trans_ref;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getRec_amount() {
        return rec_amount;
    }

    public void setRec_amount(String rec_amount) {
        this.rec_amount = rec_amount;
    }

    public String getAdj_amount() {
        return adj_amount;
    }

    public void setAdj_amount(String adj_amount) {
        this.adj_amount = adj_amount;
    }

    public String getEntered_by() {
        return entered_by;
    }

    public void setEntered_by(String entered_by) {
        this.entered_by = entered_by;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return fees;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BankingdetailsDAO) {
            BankingdetailsDAO c = (BankingdetailsDAO) obj;
            if (c.getFees().equals(fees))
                return true;
        }

        return false;
    }
}
