package com.bonsai.core;

import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.BonsaiEntity;

import javax.sql.DataSource;

public class RepositoryFactory {
    private DataSource dataSource;

    public RepositoryFactory(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public BonsaiDao createDao(Class<? extends  BonsaiEntity> entityClass){
        return new BonsaiDao(dataSource, entityClass);
    }
}
