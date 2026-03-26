package com.utp.ioscalculator.history;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel {
    // Ahora la lista es de tipo HistoryModel
    private static final List<HistoryModel> historyList = new ArrayList<>();

    public void addEntry(String op, String res) {
        historyList.add(new HistoryModel(op, res));
    }

    public List<HistoryModel> getHistoryList() {
        return historyList;
    }
}
