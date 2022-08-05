package hello.itemservice.web.baisic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
//final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
//생성자가 한개이면 Autowired 주입
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

    //itemId를 이용해 상품 조회
    @GetMapping("/{itemId}")
    public String findById(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    //쿼리 파라미터를 이용하여 view에 전달
   /* @PostMapping("/add")
    public String addiItem(@RequestParam String itemName, @RequestParam int price,
                           @RequestParam Integer quantity, Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }*/

    /*
     * * ModelAttribute를 이용하여 뷰에 전달
     *객체를 생성해주고 set을 이용하여 객체에 데이터를 삽입 시켜준다
     * ModelAttribute는 생략 가능 하다
     * url에 변수를 더해서 저장하면 url 인코딩이 안되기때문에 위험하다
     *
     @PostMapping("/add")
     public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "redirect:/basic/items/" + item.getId();
    }*/

    /**
     * RedirectAttributes
     * redirect url에 값을 추가 시켜준다
     */
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        Map<String, String> errors = new HashMap<>();

        //검증 로직 - itemName에 글자가 없을 경우
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        //검증 로직 - itemPrice가 범위를 넘어설 경우
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("itemPrice", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        //검증 로직 - itemQuantity의 수량 검즘
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }
        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 값이 10000원 이상이여야 합니다. 현재 는 " + resultPrice + "입니다");
            }
        }

        if(!errors.isEmpty()){
            log.info("errors ={}",errors);
            model.addAttribute("errors",errors);
            return "basic/addForm"; //입력폼 템플릿으로 보내버리기
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
}
