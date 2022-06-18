package tm;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @since : 5/16/21
 **/

public class StudentDetailsTM {
    private String id;
    private String name;
    private String contact;
    private String nic;
    private String institute;
    private String batch;
    private int payment;
    private String label;

    public StudentDetailsTM(String id, String name, String contact, String nic, String institute, String batch, int payment, String label) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.nic = nic;
        this.institute = institute;
        this.batch = batch;
        this.payment = payment;
        this.label = label;
    }

    public StudentDetailsTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return name;
    }
}
