package in.afckstechnologies.afcksfees.model;

/**
 * Created by admin on 12/16/2016.
 */

public class CommentModeDAO {
    private String id;
    private String student_comments;
    private String date_comments;
    public CommentModeDAO() {

    }
    public CommentModeDAO(String id, String student_comments, String date_comments) {
        this.id = id;
        this.student_comments = student_comments;
        this.date_comments = date_comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_comments() {
        return student_comments;
    }

    public void setStudent_comments(String student_comments) {
        this.student_comments = student_comments;
    }

    public String getDate_comments() {
        return date_comments;
    }

    public void setDate_comments(String date_comments) {
        this.date_comments = date_comments;
    }
}