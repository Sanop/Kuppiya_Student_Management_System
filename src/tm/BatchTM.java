package tm;

import java.util.Date;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @since : 5/15/21
 **/

public class BatchTM {
    private String batchTitle;
    private Date starting_date;
    private Date ending_date;

    public BatchTM(String batchTitle, Date starting_date, Date ending_date) {
        this.batchTitle = batchTitle;
        this.starting_date = starting_date;
        this.ending_date = ending_date;
    }

    public BatchTM() {
    }

    public BatchTM(String batchTitle) {
        this.batchTitle = batchTitle;
    }

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
    }

    public Date getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(Date starting_date) {
        this.starting_date = starting_date;
    }

    public Date getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(Date ending_date) {
        this.ending_date = ending_date;
    }

    @Override
    public String toString() {
        return batchTitle;
    }
}
