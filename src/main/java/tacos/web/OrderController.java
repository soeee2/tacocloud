package tacos.web;

import java.security.Principal; // 추가 임포트
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;
import tacos.data.UserRepository; // 추가 임포트

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo; // 💡 안전한 유저 조회를 위한 레포지토리 추가

    @Autowired
    public OrderController(OrderRepository orderRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo; // 💡 생성자 주입
    }

    @GetMapping("/current")
    // 💡 매개변수를 @AuthenticationPrincipal 대신 자바 표준 Principal 객체로 변경합니다.
    public String orderForm(@ModelAttribute("tacoOrder") TacoOrder tacoOrder, Principal principal) {

        // 💡 주입받은 principal 정보로 몽고디비에서 현재 사용자를 직접 찾습니다. (가장 안전함)
        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        if (user != null) {
            if (tacoOrder.getDeliveryName() == null) {
                tacoOrder.setDeliveryName(user.getFullname());
            }
            if (tacoOrder.getDeliveryStreet() == null) {
                tacoOrder.setDeliveryStreet(user.getStreet());
            }
            if (tacoOrder.getDeliveryCity() == null) {
                tacoOrder.setDeliveryCity(user.getCity());
            }
            if (tacoOrder.getDeliveryState() == null) {
                tacoOrder.setDeliveryState(user.getState());
            }
            if (tacoOrder.getDeliveryZip() == null) {
                tacoOrder.setDeliveryZip(user.getZip());
            }
        }
        return "orderForm";
    }

    @PostMapping
    public String processOrder(
            @Valid @ModelAttribute("tacoOrder") TacoOrder tacoOrder,
            Errors errors,
            SessionStatus sessionStatus,
            Principal principal) { // 💡 마찬가지로 Principal로 변경

        if (errors.hasErrors()) {
            return "orderForm";
        }

        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        tacoOrder.setUser(user);
        orderRepo.save(tacoOrder);

        sessionStatus.setComplete();

        return "redirect:/";
    }
}