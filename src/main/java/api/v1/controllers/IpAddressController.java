package api.v1.controllers;

import api.v1.entities.IpAddress;
import api.v1.repositories.IpAddressRepository;
import api.v1.types.IpAddressStatusType;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ipaddresses")
public class IpAddressController {

    @Autowired
    private IpAddressRepository ipAddressRepository;

    @GetMapping
    public List<IpAddress> getIpAddresses() {
        return ipAddressRepository.findAll();
    }

    @PostMapping
    public String createIpAddresses(@RequestParam(name = "cidr") String ipv4Cidr) {

        try {
            SubnetUtils subnetUtils = new SubnetUtils(ipv4Cidr);
            subnetUtils.setInclusiveHostCount(true);

            List<IpAddress> ipAddresses = new ArrayList<>();

            Arrays.stream(subnetUtils.getInfo().getAllAddresses()).forEach(ipString -> {
                IpAddress ipAddress = new IpAddress();
                ipAddress.setIpAddress(ipString);
                ipAddress.setStatus(IpAddressStatusType.AVAILABLE.label());
                ipAddresses.add(ipAddress);
            });

            ipAddressRepository.saveAll(ipAddresses);

            return HttpStatus.OK.toString() + " - Successfully added IP addresses for " + ipv4Cidr;
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed CIDR Block", e);
        }
    }

    @PatchMapping("/acquisition/{ipAddress}")
    public String acquireIpAddress(@PathVariable String ipAddress) {
        IpAddress toAcquire = ipAddressRepository.findById(ipAddress).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "IP Address not found"));

        toAcquire.setStatus(IpAddressStatusType.ACQUIRED.label());
        ipAddressRepository.save(toAcquire);
        return HttpStatus.OK.toString() + " - " + toAcquire.toString();
    }

    @PatchMapping("/release/{ipAddress}")
    public String releaseIpAddress(@PathVariable String ipAddress) {
        IpAddress toRelease = ipAddressRepository.findById(ipAddress).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "IP Address not found"));

        toRelease.setStatus(IpAddressStatusType.AVAILABLE.label());
        ipAddressRepository.save(toRelease);
        return  HttpStatus.OK.toString() + " - " + toRelease.toString();
    }
}
