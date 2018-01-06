package com.auth;

import org.springframework.stereotype.Repository;

/**
 * Created by Ciprian on 1/6/2018.
 */
@Repository
public class UserDAO extends AbstractDAO<User>{
    @Override
    public Class getClazz() {
        return User.class;
    }
}
