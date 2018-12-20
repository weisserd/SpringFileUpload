package newbienewsletter.details;


import org.apache.commons.lang3.StringUtils;

public class Details {
    private String position;
    private String nachname;
    private String vorname;
    private String department;
    private String team;
    private String supervisor;
    private String mentor;
    private String text;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = strip(position);
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = strip(nachname);
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = strip(vorname);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = strip(department);
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = strip(team);
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = strip(supervisor);
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = strip(mentor);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = strip(text);
    }

    private String strip(String input) {
        return StringUtils.stripStart(input, null);

    }

}
