package org.api.manager;

import org.api.bo.Prospect;
import org.api.bo.SelectOption;
import org.api.utils.TechniqueException;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListeManager extends Manager {

    public List<SelectOption> getListForTable(String pTable) throws TechniqueException {
        String lQuery = "SELECT * FROM " + pTable;
        System.out.println(lQuery);
        return select( lQuery , null, lResultSet -> {
                    List<SelectOption> lSelectOption = new ArrayList<>();
                    while (lResultSet.next()) {
                        lSelectOption.add(SelectOption.mapIn(lResultSet));
                    }
                    return lSelectOption;
                });
    }
}
