package api.v1.repositories;

import api.v1.entities.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, String> {
}
