package api.v1.types;

public enum IpAddressStatusType {
    ACQUIRED("acquired"), AVAILABLE("available");

    private String label;

    IpAddressStatusType(String label) {
        this.label = label;
    }

    public String label () {
        return this.label;
    }
}
