package com.bonsai.operation;

import com.bonsai.core.RepositoryFactory;
import com.bonsai.core.dao.BonsaiDao;
import com.bonsai.core.dao.Sort;
import com.bonsai.operation.model.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        Sort sort = new Sort(Arrays.asList(new String[]{"alias"}),
                Arrays.asList(new Sort.Direction[]{Sort.Direction.ASC}));
        return operationDao.find("1=1", sort, null).data;
    }

    public List<Operation> getByIds(List<Long> ids){
        return operationDao.find(String.format("id in (%s)", ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","))));
    }
}
