package persistence.database;
import model.OficiuPersoana;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import model.OficiuPersoana;

public class OficiuPersoanaDBRepo implements IOficiuPersoana {

    static SessionFactory sessionFactory;

    //private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public OficiuPersoanaDBRepo(Properties properties) {
        logger.info("Initializing OficiuPersoanaDBRepo with hibernate:{}", properties);
        //dbUtils=new JdbcUtils(properties);
        initialize();
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public OficiuPersoana findOne(Long aLong) {
      //  logger.traceEntry("findone task{}, elem");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                System.out.println("findone task{}, elem");
                tx = session.beginTransaction();
                String hql = "from OficiuPersoana O where O.id = :id";
                Query query = session.createQuery(hql);
                query.setParameter("id", aLong);
                OficiuPersoana oficiuPersoana = (OficiuPersoana) query.getSingleResult();
                System.out.println("findone task{}, elem");
                tx.commit();
                return oficiuPersoana;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public Iterable<OficiuPersoana> findAll() {
        return null;
    }

    @Override
    public OficiuPersoana save(OficiuPersoana entity) {
        return null;
    }

    @Override
    public OficiuPersoana delete(Long aLong) {
        return null;
    }

    @Override
    public OficiuPersoana update(OficiuPersoana entity) {
        return null;
    }

    @Override
    public OficiuPersoana Login(String user, String password) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                System.out.println("login task{}, elem");
                tx = session.beginTransaction();
                String hql = "from OficiuPersoana O where O.user = :user and O.password=:password";
                Query query = session.createQuery(hql);
                query.setParameter("user", user);
                query.setParameter("password", password);
                OficiuPersoana oficiuPersoana = (OficiuPersoana) query.getResultList().get(0);
                System.out.println("findone task{}, elem");
                tx.commit();
                return oficiuPersoana;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }
}
