package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 3/7/2017.
 */

public class TemplatesDAO {
    String ID = "";
    String Template_Text = "";



    public TemplatesDAO() {

    }

    public TemplatesDAO(String ID, String template_Text) {
        this.ID = ID;
        Template_Text = template_Text;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTemplate_Text() {
        return Template_Text;
    }

    public void setTemplate_Text(String template_Text) {
        Template_Text = template_Text;
    }

    @Override
    public String toString() {
        return ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TemplatesDAO) {
            TemplatesDAO c = (TemplatesDAO) obj;
            if (c.getID().equals(ID)) return true;
        }

        return false;
    }
}
