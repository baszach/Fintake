package layout.companyInformation;

import file.ConfigData;
import http.Query;
import parser.JsonKeySelectionTree;

import java.util.List;

public interface CompanyInformationNavigator {
    void nextDialog(JsonKeySelectionTree jsonTree, List<ConfigData> configs, Query query);
}
