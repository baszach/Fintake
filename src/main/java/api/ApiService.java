package api;

import java.util.Objects;

public class ApiService {

    private final String serviceName;

    public ApiService(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ApiService that = (ApiService) other;
        return Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }
}
