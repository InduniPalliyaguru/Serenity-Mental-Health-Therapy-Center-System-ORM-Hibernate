package lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.impl;

import lk.ijse.serenitymentalhealththerepycentersystem.config.FactoryConfiguration;
import lk.ijse.serenitymentalhealththerepycentersystem.dao.custom.UserDAO;
import lk.ijse.serenitymentalhealththerepycentersystem.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

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

    @Override
    public boolean save(User entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(User entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.find(User.class, id);
            if (user!= null) {
                session.remove(user);
                transaction.commit();
                return true;
            }
            return false;
        }catch (Exception e) {
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public User search(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<User> users = session.createQuery("FROM User", User.class).list();
        session.close();
        return users;
    }


    @Override
    public Optional<String> getLastPK() {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastPk = session.createQuery("SELECT u.user_id FROM User u ORDER BY u.user_id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public String validateUser(String username, String password) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Object[] result = null;

        try {
            result = session.createQuery(
                            "SELECT u.password, u.role FROM User u WHERE u.username = :username", Object[].class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (result == null) {
            System.out.println("Debug: No user found with username: " + username);
            return null;
        }

        String databasePassword = (String) result[0];
        String role = (String) result[1];

        if (databasePassword != null && databasePassword.equals(password)) {
            System.out.println("Debug: Authentication successful for role: " + role);
            return role;
        } else {
            System.out.println("Debug: Password does not match for user: " + username);
            return null;
        }
    }


    @Override
    public Optional<User> findByUserId(String userId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<User> users = null;

        try {
            users = session.createQuery("FROM User WHERE user_id = :userId", User.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }


    @Override
    public boolean updateUsernameAndPassword(String userId, String newUsername, String newPassword) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            User user = session.find(User.class, userId);

            if (user != null) {
                // Update only the username and password
                user.setUsername(newUsername);
                user.setPassword(newPassword);  // Assuming password is already hashed when being passed here

                session.update(user);  // Update the user with new values
                transaction.commit();
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
