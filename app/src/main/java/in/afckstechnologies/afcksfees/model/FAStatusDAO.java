package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 3/7/2017.
 */

public class FAStatusDAO {
    String id = "";
    String status = "";

    private boolean selected;

    public FAStatusDAO() {

    }

    public FAStatusDAO(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return status;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FAStatusDAO) {
            FAStatusDAO c = (FAStatusDAO) obj;
            if (c.getStatus().equals(status) && c.getId() == id) return true;
        }

        return false;
    }
}
