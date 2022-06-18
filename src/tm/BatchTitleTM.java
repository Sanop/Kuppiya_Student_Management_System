package tm;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @Since : 6/22/21
 **/
public class BatchTitleTM {
    private String title;

    public BatchTitleTM(String title) {
        this.title = title;
    }

    public BatchTitleTM() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
