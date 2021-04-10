package layout.companyInformation;

import file.ConfigData;
import parser.JsonKeySelectionTree;

import java.util.List;

public interface CompanyInformationNavigator {
    void next(List<ConfigData> configs, JsonKeySelectionTree jsonTree);
}
