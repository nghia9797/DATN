package com.bonsai.core.dao;

import java.util.List;

public class ResultPaging <T extends BonsaiEntity> {
    public List<T> data;
    public Long count;
}
