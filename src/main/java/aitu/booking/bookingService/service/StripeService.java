package aitu.booking.bookingService.service;

import aitu.booking.bookingService.dto.CartItemDTO;
import aitu.booking.bookingService.model.MenuItem;
import aitu.booking.bookingService.repository.MenuItemRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.InstanceNotFoundException;
import java.util.*;

@Slf4j
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;
    @Value("${front.url}")
    private String frontUrl;
    private MenuItemRepository repository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public Session createCheckoutSession(Long bookingId, List<CartItemDTO> cartItems, UUID userId) throws StripeException {
        List<Map<String, Object>> cartList = getMapFromCart(cartItems);
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (Map<String, Object> item : cartList) {
            lineItems.add(SessionCreateParams.LineItem.builder()
                    .setQuantity((Long) item.get("quantity"))
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("kzt")
                            .setUnitAmount((Long) item.get("price") * 100)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName((String) item.get("name"))
                                    .build())
                            .build())
                    .build());
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addAllLineItem(lineItems)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(frontUrl + "/profile/cart/success?bookingId=" + bookingId + "&userId=" + userId)
                        .setCancelUrl(frontUrl + "/profile/cart/cancel?bookingId=" + bookingId)
                        .build();

        Session session = Session.create(params);
        log.info("Session url: {}", session.getUrl());
        return session;
    }

    List<Map<String, Object>> getMapFromCart(List<CartItemDTO> cartItems) {
        List<Map<String, Object>> list = new ArrayList<>();
        cartItems.forEach(item -> {
            MenuItem menuItem;
            try {
                menuItem = repository.findById(item.getItemId()).orElseThrow(InstanceNotFoundException::new);
                Map<String, Object> map = new HashMap<>();
                map.put("id", menuItem.getId());
                map.put("quantity", (long) item.getQuantity());
                map.put("price", (long) menuItem.getPrice());
                map.put("name", menuItem.getName());
                list.add(map);
            } catch (InstanceNotFoundException e) {
                log.error("No menuItem with id: {}", item.getItemId());
            }
        });
        log.info("CartList: {}", list);
        return list;
    }

    @Autowired
    public void setRepository(MenuItemRepository repository) {
        this.repository = repository;
    }
}
