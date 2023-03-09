package taolili;

import taolili.dao.RecordEntity;

import java.util.List;

public interface WalletOperate {
    public long checkAmount(int uid) throws Throwable;

    public long spend100(int uid);

    public long refund20(int uid);

    public List<RecordEntity> checkRecord(int uid);
}
