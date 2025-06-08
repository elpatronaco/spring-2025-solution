package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    @Value("${productService.getProductDetails.url}")
    private String productServiceUrl;

    @Override
    public void notifyProductAvailable(ProductMessage productMessage) {

        GetProductResponse product = new RestTemplate().getForEntity(productServiceUrl, GetProductResponse.class, productMessage.getProductId()).getBody();

        GetUserResponse[] usersToAlert = new RestTemplate().getForEntity(userServiceUrl, GetUserResponse[].class, productMessage.getProductId(), LocalDate.now()).getBody();

        for (GetUserResponse user : usersToAlert) {
            log.info("Sending an email to user " + user.getFullName() + " at \"" + user.getEmail() + "\" to notify new units available on product \"" + product.getName() + "\".");
        }
    }
}
