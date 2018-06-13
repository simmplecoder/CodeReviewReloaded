package representations;

public class AssignmentsRequestFormat {
    public String email;
    public int id;

    public String toString() {
        return "( email: " + this.email + " id:" + id + ")";
    }
}
