package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAOImpl implements UserDAO {


    @Override
    public User findByUsername(String username) {
        Session session = FactoryConfiguration.getInstance().getSession();

        Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        User user = query.uniqueResult();
        session.close();
        return user;
    }
}
