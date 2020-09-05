package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 2/18/2017.
 */

public class DayPrefrenceDAO {
    String id = "";
    String Prefrence = "";
    String isselected="";
    private boolean isSelected;

    public DayPrefrenceDAO() {
    }


    public DayPrefrenceDAO(String id, String prefrence, String isselected, boolean isSelected) {
        this.id = id;
        Prefrence = prefrence;
        this.isselected = isselected;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefrence() {
        return Prefrence;
    }

    public void setPrefrence(String prefrence) {
        Prefrence = prefrence;
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
