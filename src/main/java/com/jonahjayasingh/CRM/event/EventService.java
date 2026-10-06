package com.jonahjayasingh.CRM.event;

import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        if (event.getStartTime() != null && event.getEndTime() != null && event.getEndTime().isBefore(event.getStartTime())) {
            throw new IllegalArgumentException("Event end time cannot be before start time.");
        }

        String username = "System";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            username = auth.getName();
            Optional<User> userOpt = userRepository.findByName(username);
            if (userOpt.isPresent()) {
                event.setAssignedToUserId(userOpt.get().getId());
                event.setAssignedToUserName(userOpt.get().getName());
            }
        }

        Event saved = eventRepository.save(event);
        auditLogService.log("CREATE", "Event", saved.getId(), username, "Created event: " + saved.getTitle());
        return saved;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        if (updatedEvent.getStartTime() != null && updatedEvent.getEndTime() != null && updatedEvent.getEndTime().isBefore(updatedEvent.getStartTime())) {
            throw new IllegalArgumentException("Event end time cannot be before start time.");
        }

        String username = "System";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            username = auth.getName();
        }

        final String finalUsername = username;
        return eventRepository.findById(id).map(existing -> {
            existing.setTitle(updatedEvent.getTitle());
            existing.setDescription(updatedEvent.getDescription());
            existing.setStartTime(updatedEvent.getStartTime());
            existing.setEndTime(updatedEvent.getEndTime());
            existing.setLocation(updatedEvent.getLocation());
            existing.setEventType(updatedEvent.getEventType());
            existing.setStatus(updatedEvent.getStatus());
            existing.setCustomerId(updatedEvent.getCustomerId());
            existing.setCustomerName(updatedEvent.getCustomerName());
            Event saved = eventRepository.save(existing);
            auditLogService.log("UPDATE", "Event", saved.getId(), finalUsername, "Updated event: " + saved.getTitle());
            return saved;
        }).orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public void deleteEvent(Long id) {
        String username = "System";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            username = auth.getName();
        }

        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.deleteById(id);
        auditLogService.log("DELETE", "Event", id, username, "Deleted event: " + event.getTitle());
    }
}
