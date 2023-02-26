package com.bonsai.operation;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.operation.model.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {
    private BonsaiDao<Operation> operationDao;

    @Autowired
    public OperationService(RepositoryFactory repositoryFactory){
        operationDao = repositoryFactory.createDao(Operation.class);
    }

    public List<Operation> getAll(){
        return operationDao.findAll();
    }

    public List<Operation> getByIds(List<Long> ids){
        return operationDao.find(String.format("id in (%s)", ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","))));
    }
}
