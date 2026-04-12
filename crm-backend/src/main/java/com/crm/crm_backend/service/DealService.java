package com.crm.crm_backend.service;

import com.crm.crm_backend.dto.ActivityRequest;
import com.crm.crm_backend.dto.DashboardStats;
import com.crm.crm_backend.dto.DealRequest;
import com.crm.crm_backend.dto.DealResponse;
import com.crm.crm_backend.model.*;
import com.crm.crm_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public List<DealResponse> getAllDeals(DealStage stage) {
        List<Deal> deals = stage != null
                ? dealRepository.findByStage(stage)
                : dealRepository.findAll();
        return deals.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public DealResponse createDeal(DealRequest request) {
        Deal deal = new Deal();
        deal.setTitle(request.getTitle());
        deal.setValue(request.getValue());
        deal.setStage(request.getStage() != null ? request.getStage() : DealStage.PROSPECT);

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new RuntimeException("Kontakt nije pronađen"));
            deal.setContact(contact);
        }

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        deal.setAssignedTo(currentUser);

        return toResponse(dealRepository.save(deal));
    }

    public DealResponse updateStage(Long id, DealStage stage) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deal nije pronađen"));
        deal.setStage(stage);
        return toResponse(dealRepository.save(deal));
    }

    public DealResponse updateDeal(Long id, DealRequest request) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deal nije pronađen"));
        deal.setTitle(request.getTitle());
        deal.setValue(request.getValue());
        if (request.getStage() != null) {
            deal.setStage(request.getStage());
        }
        return toResponse(dealRepository.save(deal));
    }

    public Activity createActivity(ActivityRequest request) {
        Activity activity = new Activity();
        activity.setType(request.getType());
        activity.setDescription(request.getDescription());

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new RuntimeException("Kontakt nije pronađen"));
            activity.setContact(contact);
        }

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        activity.setCreatedBy(currentUser);

        return activityRepository.save(activity);
    }

    public List<Activity> getActivitiesForContact(Long contactId) {
        return activityRepository.findByContactIdOrderByCreatedAtDesc(contactId);
    }

    public DashboardStats getDashboardStats() {
        long totalContacts = contactRepository.count();
        long totalDeals = dealRepository.count();
        Double totalWonValue = dealRepository.getTotalWonValue();
        long wonDeals = dealRepository.countWonDeals();
        long lostDeals = dealRepository.countLostDeals();

        double winRate = 0;
        if (wonDeals + lostDeals > 0) {
            winRate = Math.round((double) wonDeals / (wonDeals + lostDeals) * 100.0);
        }

        Map<String, Long> dealsByStage = new HashMap<>();
        dealRepository.countByStage().forEach(row -> {
            dealsByStage.put(row[0].toString(), (Long) row[1]);
        });

        return new DashboardStats(
                totalContacts,
                totalDeals,
                totalWonValue != null ? totalWonValue : 0.0,
                wonDeals,
                lostDeals,
                winRate,
                dealsByStage
        );
    }

    private DealResponse toResponse(Deal deal) {
        DealResponse response = new DealResponse();
        response.setId(deal.getId());
        response.setTitle(deal.getTitle());
        response.setValue(deal.getValue());
        response.setStage(deal.getStage());
        response.setCreatedAt(deal.getCreatedAt());
        if (deal.getContact() != null) {
            response.setContactName(deal.getContact().getFirstName()
                    + " " + deal.getContact().getLastName());
        }
        if (deal.getAssignedTo() != null) {
            response.setAssignedTo(deal.getAssignedTo().getEmail());
        }
        return response;
    }
}