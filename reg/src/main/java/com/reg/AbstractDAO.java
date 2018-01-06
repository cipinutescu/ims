package com.reg;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ciprian on 12/29/2017.
 */
public abstract class AbstractDAO<T> {

    public abstract Class getClazz();

    public T getById(@NotNull Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> list = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List users = session.createQuery("FROM " + getClazz().getName() + " e where e.id=" + id).list();
            for (Iterator iterator =
                 users.iterator(); iterator.hasNext(); ) {
                list.add((T) iterator.next());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list.size() == 0 ? null : list.get(0);
    }

    public List<T> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<T> list = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            List result = session.createQuery("FROM " + getClazz().getName()).list();
            for(Iterator it = result.iterator(); it.hasNext();){
                list.add((T) it.next());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    public  boolean create(T acl) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(acl);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public  boolean update(T acl) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(acl);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public  boolean delete(T acl) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(acl);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public boolean eraseAllData(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = String.format("delete from %s",this.getClazz().getName());
            Query query = session.createQuery(hql);
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }
}
