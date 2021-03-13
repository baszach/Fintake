package api;

public abstract class ApiFactory {

    public static SupportedApi buildApi(String apiName) {
        for (SupportedApi api: SupportedApi.values()) {
            if (api.isApi(apiName)) {
                return SupportedApi.valueOf(apiName);
            }
        }
        return null;
    }
}
