package com.reg;

import org.springframework.stereotype.Repository;

/**
 * Created by Ciprian on 1/6/2018.
 */
@Repository
public class UserDAO2 extends AbstractDAO<User2>{
    @Override
    public Class getClazz() {
        return User2.class;
    }
}
