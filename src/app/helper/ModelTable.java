package app.helper;

public class ModelTable {


    private static ModelTable Instance;

    String id, firstname, lastname, email, gender, role;
    public ModelTable(String id, String firstname, String lastname, String email, String gender, String role)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.role = role;
    }

    public static ModelTable getInstance()
    {
        return Instance;
    }

    public String Pid()
    {
        return getId();
    }

    public String firstname()
    {
        return getFname();
    }

    public String lastname()
    {
        return getLname();
    }


    String fname,lname,age,ph,pco,hco,datem,comments,interpreter,results;

    public ModelTable(String id, String fname, String lname, String age, String ph, String pco, String hco, String datem,String comments, String interpreter,String results) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.ph = ph;
        this.pco = pco;
        this.hco = hco;
        this.datem = datem;
        this.comments = comments;
        this.interpreter = interpreter;
        this.results = results;


    }



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String firstname) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getPco() {
        return pco;
    }

    public void setPco(String pco) {
        this.pco = pco;
    }

    public String getHco() {
        return hco;
    }

    public void setHco(String hco) {
        this.hco = hco;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDatem() {
        return datem;
    }

    public void setDate(String date) {
        this.datem = datem;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    public String getResults() { return results;}

    public void setResults(String results) {this.results = results;}
}
