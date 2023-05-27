package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.SupportRequestDTO;
import aitu.booking.bookingService.model.SupportRequest;
import aitu.booking.bookingService.repository.SupportRepository;
import aitu.booking.bookingService.util.KeycloakUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;

@Service
public class SupportService {
    private SupportRepository supportRepository;

    public Page<SupportRequest> getPageOfRequests() {
        return supportRepository.findAll(PageRequest.of(0, 100, Sort.by("dtCreate").descending()));
    }

    @Transactional
    public void resolveRequest(Long id) throws InstanceNotFoundException {
        SupportRequest request = getRequestById(id);
        request.setIsResolved(true);
        supportRepository.save(request);
    }

    @Transactional
    public void createRequest(SupportRequestDTO dto, Authentication authentication) {
        UUID userId = KeycloakUtils.getUserUuidFromAuth(authentication);
        SupportRequest request = new SupportRequest();
        request.setDtCreate(ZonedDateTime.now());
        request.setPhone(dto.getPhone());
        request.setIsResolved(false);
        request.setText(dto.getText());
        request.setUserId(userId);
        supportRepository.save(request);
    }

    public SupportRequest getRequestById(Long id) throws InstanceNotFoundException {
        return supportRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }

    @Autowired
    public void setSupportRepository(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }
}
