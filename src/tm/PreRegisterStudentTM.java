package tm;

import javafx.scene.control.Button;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @Since : 5/30/21
 **/
public class PreRegisterStudentTM {
    private String id;
    private String name;
    private String contact;
    private String institute;
    private String batch;
    private Button checkIn;
    private Button checkOut;

    public PreRegisterStudentTM(String id, String name, String contact, String institute, String batch, Button checkIn, Button checkOut) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.institute = institute;
        this.batch = batch;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public PreRegisterStudentTM() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public Button getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Button checkIn) {
        this.checkIn = checkIn;
    }

    public Button getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Button checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "PreRegisterStudentTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", institute='" + institute + '\'' +
                ", batch='" + batch + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}
