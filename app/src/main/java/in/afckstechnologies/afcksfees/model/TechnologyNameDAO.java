package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 3/7/2017.
 */

public class TechnologyNameDAO {
    String id = "";
    String user_name = "";
    String work_from_status = "";


    public TechnologyNameDAO() {

    }

    public TechnologyNameDAO(String id, String user_name, String work_from_status) {
        this.id = id;
        this.user_name = user_name;
        this.work_from_status = work_from_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getWork_from_status() {
        return work_from_status;
    }

    public void setWork_from_status(String work_from_status) {
        this.work_from_status = work_from_status;
    }

    @Override
    public String toString() {
        return user_name;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TechnologyNameDAO) {
            TechnologyNameDAO c = (TechnologyNameDAO) obj;
            if (c.getUser_name().equals(user_name) && c.getId() == id) return true;
        }

        return false;
    }
}
