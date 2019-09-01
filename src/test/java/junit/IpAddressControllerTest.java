package junit;

import api.v1.controllers.IpAddressController;
import api.v1.entities.IpAddress;
import api.v1.repositories.IpAddressRepository;
import api.v1.types.IpAddressStatusType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IpAddressControllerTest {

    @InjectMocks
    private IpAddressController ipAddressController;

    @Mock
    private IpAddressRepository ipAddressRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private IpAddress createTestIpAddress() {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpAddress("10.0.0.1");
        ipAddress.setStatus(IpAddressStatusType.AVAILABLE.label());
        return ipAddress;
    }

    @Test
    public void testGetAllIpAddresses() {

        IpAddress ipAddress = createTestIpAddress();

        List<IpAddress> ipList = Collections.singletonList(ipAddress);
        when(ipAddressRepository.findAll()).thenReturn(ipList);

        List<IpAddress> list = ipAddressController.getIpAddresses();

        verify(ipAddressRepository).findAll();

        assertEquals("10.0.0.1", list.get(0).getIpAddress());
        assertEquals("available", list.get(0).getStatus());
    }

    @Test
    public void testCreateIpAddressesSuccess() {
        String result = ipAddressController.createIpAddresses("10.0.0.0/24");
        assertEquals(HttpStatus.OK.toString() + " - Successfully added IP addresses for 10.0.0.0/24", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void testCreateIpAddressesFailure() throws Exception {
        ipAddressController.createIpAddresses("123123213123123");
    }

    @Test
    public void testAcquireIpAddressSuccess() {

        IpAddress ipAddress = createTestIpAddress();
        Optional<IpAddress> optionalIpAddress = Optional.of(ipAddress);

        when(ipAddressRepository.findById(ArgumentMatchers.anyString())).thenReturn(optionalIpAddress);

        String result = ipAddressController.acquireIpAddress("10.0.0.1");

        IpAddress compare = createTestIpAddress();
        compare.setStatus(IpAddressStatusType.ACQUIRED.label());

        assertEquals(HttpStatus.OK.toString() + " - " + compare.toString(), result);
    }

    @Test(expected = ResponseStatusException.class)
    public void testAcquireIpAddressFailure() {
        ipAddressController.acquireIpAddress("123123123123");
    }

    @Test
    public void testReleaseIpAddress() {

        IpAddress ipAddress = createTestIpAddress();
        ipAddress.setStatus(IpAddressStatusType.ACQUIRED.label());
        Optional<IpAddress> optionalIpAddress = Optional.of(ipAddress);

        when(ipAddressRepository.findById(ArgumentMatchers.anyString())).thenReturn(optionalIpAddress);

        String result = ipAddressController.releaseIpAddress("10.0.0.1");

        IpAddress compare = createTestIpAddress();
        assertEquals(HttpStatus.OK.toString() + " - " + compare.toString(), result);
    }

    @Test(expected = ResponseStatusException.class)
    public void testReleaseIpAddressFailure() {
        ipAddressController.releaseIpAddress("123123123123");
    }
}
