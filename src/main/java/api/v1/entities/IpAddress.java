package api.v1.entities;

import javax.persistence.*;

@Entity
public class IpAddress {

    @Id
    @Column
    private String ipAddress;

    @Column
    private String status;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IpAddress{" +
                "ipAddress='" + ipAddress + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
