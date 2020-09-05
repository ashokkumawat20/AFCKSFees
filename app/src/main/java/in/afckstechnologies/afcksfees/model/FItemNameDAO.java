package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 3/7/2017.
 */

public class FItemNameDAO {
    String id = "";
    String ItemName = "";

    private boolean selected;

    public FItemNameDAO() {

    }

    public FItemNameDAO(String id, String itemName) {
        this.id = id;
        ItemName = itemName;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return ItemName;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FItemNameDAO) {
            FItemNameDAO c = (FItemNameDAO) obj;
            if (c.getItemName().equals(ItemName) && c.getId() == id) return true;
        }

        return false;
    }
}
