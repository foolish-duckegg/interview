package taolili.dao;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "record", schema = "interview", catalog = "")
public class RecordEntity {
    private long recordid;
    private int uid;
    private int record;
    private String reason;

    @Id
    @Column(name = "recordid")
    public long getRecordid() {
        return recordid;
    }

    public void setRecordid(long recordid) {
        this.recordid = recordid;
    }

    @Basic
    @Column(name = "uid")
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "record")
    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    @Basic
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordEntity that = (RecordEntity) o;
        return recordid == that.recordid &&
                uid == that.uid &&
                record == that.record &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordid, uid, record, reason);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("recordid=")
                .append(recordid)
                .append(" uid=")
                .append(uid)
                .append(" record=")
                .append(record)
                .append(" reason:")
                .append(reason);
        return builder.toString();
    }
}
