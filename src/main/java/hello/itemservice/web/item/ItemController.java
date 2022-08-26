package hello.itemservice.web.item;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
//final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
//생성자가 한개이면 Autowired 주입
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) throws SQLException {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "item/items";
    }
    

    //itemId를 이용해 상품 조회
    @GetMapping("/{itemId}")
    public String findById(@PathVariable Long itemId, Model model) throws SQLException {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "item/item";
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute Item item) {
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
     * @Validated를 이용하면 supports를 통해 필요한 검증기를 찾고 호출한다
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) throws SQLException {

        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "item/addForm";
        }

        //성공 로직

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setItemName(form.getItemName());
        item.setItemName(form.getItemName());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) throws SQLException {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "item/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) throws SQLException {

        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        Item itemPara = new Item();
        itemPara.setItemName(form.getItemName());
        itemPara.setPrice(form.getPrice());
        itemPara.setQuantity(form.getQuantity());

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "basic/editForm";
        }
        itemRepository.update(itemId,itemPara);
        return "redirect:/item/{itemId}";
    }
}
