package in.afckstechnologies.afcksfees.model;

/**
 * Created by Ashok on 6/2/2017.
 */

public class TemplatesContactDAO {
    String ID = "";
    String Subject = "";
    String tag = "";
    String Template_Text = "";
    boolean isSelected = false;

    public TemplatesContactDAO() {
    }

    public TemplatesContactDAO(String ID, String subject, String tag, String template_Text, boolean isSelected) {
        this.ID = ID;
        Subject = subject;
        this.tag = tag;
        Template_Text = template_Text;
        this.isSelected = isSelected;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTemplate_Text() {
        return Template_Text;
    }

    public void setTemplate_Text(String template_Text) {
        Template_Text = template_Text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
