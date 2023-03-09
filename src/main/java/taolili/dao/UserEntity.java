package taolili.dao;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "interview", catalog = "")
public class UserEntity {
    private int uid;
    private long amount;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Id
    @Column(name = "uid")
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "amount")
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return uid == that.uid &&
                amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, amount);
    }
}
