package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 2/18/2017.
 */

public class CenterDAO {
    String id = "";
    String branch_name = "";
    String address = "";
    String isselected="";
    private boolean isSelected=false;

    public CenterDAO() {
    }

    public CenterDAO(String id, String branch_name, String address, String isselected, boolean isSelected) {
        this.id = id;
        this.branch_name = branch_name;
        this.address = address;
        this.isselected = isselected;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsselected() {
        return isselected;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
