package app.helper;

public class ModelTable {


    private static ModelTable Instance;

    public ModelTable()
    {
        Instance = this;
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


    String id,fname,lname,age,ph,pco,hco,date,interpreter;

    public ModelTable(String id, String fname, String lname, String age, String ph, String pco, String hco, String date, String interpreter) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.ph = ph;
        this.pco = pco;
        this.hco = hco;
        this.date = date;
        this.interpreter = interpreter;


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }
}
