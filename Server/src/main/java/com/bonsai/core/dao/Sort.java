package com.bonsai.core.dao;

import java.util.List;

public class Sort {
    public enum Direction{
        ACS,
        DESC
    }
    private List<String> columnNames;
    private List<Direction> directions;

    public Sort(List<String> columnNames, List<Direction> directions) {
        this.columnNames = columnNames;
        this.directions = directions;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
