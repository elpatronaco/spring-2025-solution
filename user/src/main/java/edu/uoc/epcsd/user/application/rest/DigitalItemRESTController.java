package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalItemRequest;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/digitalItem")
public class DigitalItemRESTController {

    private final DigitalItemService digitalItemService;

    @GetMapping("/allItems")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalItem> getAllDigitalItem() {
        log.trace("getAllDigitalItem");

        return digitalItemService.findAllDigitalItem();
    }
    
    @GetMapping("/{digitalItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DigitalItem> getDigitalItemById(@PathVariable @NotNull Long digitalItemId) {
        log.trace("getDigitalItemById");

        return digitalItemService.getDigitalItemById(digitalItemId).map(item -> ResponseEntity.ok().body(item))
                .orElse(ResponseEntity.notFound().build());
    }

    
    @GetMapping("/digitalItemBySession")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalItem> findDigitalItemBySession(@RequestParam @NotNull Long digitalSessionId) {
        log.trace("getDigitalItemsBySession");
        return digitalItemService.findDigitalItemBySession(digitalSessionId);
    }  
    
    @PostMapping("/addItem")
    public ResponseEntity<Long> addDigitalItem(@RequestBody @Valid CreateDigitalItemRequest createDigitalItemRequest) {
        log.trace("addDigitalItem");

        try {
            log.trace("Adding DigitalItem " + createDigitalItemRequest);
            Long digitalItemId = digitalItemService.addDigitalItem(DigitalItem.builder()
                    .digitalSessionId(createDigitalItemRequest.getDigitalSessionId())
                    .description(createDigitalItemRequest.getDescription())
                    .lon(createDigitalItemRequest.getLon())
                    .lat(createDigitalItemRequest.getLat())
                    .link(createDigitalItemRequest.getLink())
                    .build());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(digitalItemId)
                    .toUri();

            return ResponseEntity.created(uri).body(digitalItemId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digital session ID " + createDigitalItemRequest.getDigitalSessionId() + " does not exist.", e);
        }
    }

    @PutMapping("/updateItem/{digitalItemId}")
    public ResponseEntity<Boolean> updateDigitalItem(@PathVariable @NotNull Long digitalItemId, @RequestBody @Valid CreateDigitalItemRequest updateDigitalItemRequest) {
        log.trace("updateDigitalItem");
        log.info(digitalItemId);
        try {
			log.trace("Updating DigitalItem " + updateDigitalItemRequest);
            digitalItemService.updateDigitalItem(digitalItemId, updateDigitalItemRequest.getDescription(), updateDigitalItemRequest.getLink(), updateDigitalItemRequest.getLat(), updateDigitalItemRequest.getLon());

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified ID " + digitalItemId + " does not exist.", e);
        }
    }

    @PatchMapping("/reviewDigitalItem/{digitalItemId}")
    public ResponseEntity<Boolean> setDigitalItemForReview(@PathVariable @NotNull Long digitalItemId) {
        log.trace("setDigitalItemForReview");
        log.info(digitalItemId);
        try {
            digitalItemService.setDigitalItemForReview(digitalItemId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified DigitalItem id " + digitalItemId + " does not exist.", e);
        }
    }
    
    @PatchMapping("/approveDigitalItem/{digitalItemId}")
    public ResponseEntity<Boolean> approvePendingDigitalItem(@PathVariable @NotNull Long digitalItemId) {
        log.trace("approvePendingDigitalItem");
        log.info(digitalItemId);
        try {
            digitalItemService.approvePendingDigitalItem(digitalItemId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified DigitalItem id " + digitalItemId + " does not exist.", e);
        }
    }
    @PatchMapping("/rejectDigitalItem/{digitalItemId}")
    public ResponseEntity<Boolean> rejectPendingDigitalItem(@PathVariable @NotNull Long digitalItemId) {
        log.trace("rejectPendingDigitalItem");
        log.info(digitalItemId);
        try {
            digitalItemService.rejectPendingDigitalItem(digitalItemId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified DigitalItem id " + digitalItemId + " does not exist.", e);
        }
    }
    
    @DeleteMapping("/dropItem/{digitalItemId}")
    public ResponseEntity<Boolean> dropDigitalItem(@PathVariable @NotNull Long digitalItemId) {
        log.trace("dropDigitalItem");
        log.info(digitalItemId);
        try {
            digitalItemService.dropDigitalItem(digitalItemId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified DigitalItem id " + digitalItemId + " does not exist.", e);
        }
    }  
}
