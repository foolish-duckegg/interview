package taolili;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import taolili.dao.RecordEntity;
import taolili.dao.UserEntity;
import taolili.exception.NoneUserException;
import taolili.exception.NotEnoughAmountException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WalletOperator implements WalletOperate {

    private final SessionFactory factory;
    private static final WalletOperator operate = new WalletOperator();

    private WalletOperator() {
        Configuration configuration = new Configuration();
        configuration.configure();
        factory = configuration.buildSessionFactory();
    }

    //单例模式
    public static WalletOperator getOperate() {
        return operate;
    }

    @Override
    public long checkAmount(int uid) throws NoneUserException {
        Session session = factory.openSession();
        UserEntity user = session.get(UserEntity.class, uid);
        session.close(); //释放资源
        if (user == null) {
            throw new NoneUserException();
        }
        return user.getAmount();
    }

    @Override
    public long spend100(int uid) throws NotEnoughAmountException, NoneUserException {
        return resume(uid, -100, "spend100");
    }

    @Override
    public long refund20(int uid) throws NotEnoughAmountException, NoneUserException {
        return resume(uid, 20, "refund20");
    }

    @Override
    public List<RecordEntity> checkRecord(int uid) {
        final String str = "select * from record where uid=";
        Session session = factory.openSession();

        return (List<RecordEntity>) session.createSQLQuery(str + uid).addEntity(RecordEntity.class).list();

    }

    //需求中不要求任意金额消费接口，不对外暴露
    private long resume(int uid, int record, String str) throws NotEnoughAmountException, NoneUserException {
        Session session = factory.openSession();

        //获取用户钱包对象
        UserEntity user = session.get(UserEntity.class, uid);
        if (user == null) {
            throw new NoneUserException();
        }

        //重新打开一个session来强制刷新session的状态，使乐观锁生效
        session.close();
        session = factory.openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();

        //操作钱包
        long amount = user.getAmount();
        long returner = amount + record;
        if (returner < 0) {
            throw new NotEnoughAmountException();
        }

        user.setAmount(amount + record);
        session.update(user);

        //记录数据
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setRecordid(GetRecordId.newRecordId());
        recordEntity.setRecord(record);
        recordEntity.setReason(str);
        recordEntity.setUid(uid);
        session.save(recordEntity);

        //提交事务
        transaction.commit();
        session.close();

        return returner;
    }
}

//获取不重复订单号的方法
class GetRecordId {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    private static int i = 1;
    private static long time;

    //保证线程安全
    //bigint 只能用19位，时间占14位，订单号不能超过5位
    public static synchronized long newRecordId() {
        if (i == 100000) {
            i = 1;
            if (new Date().getTime() == time) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time = new Date().getTime();
        }
        long returner = Long.parseLong(format.format(new Date())) * 100000 + i;
        i++;
        return returner;
    }

}
