package hello.web.item;

import hello.domain.item.Item;
import hello.domain.item.ItemSaveDto;
import hello.domain.item.ItemSearchDto;
import hello.domain.item.ItemUpdateDto;
import hello.repository.ItemRepository;
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
    public String items(@ModelAttribute("item") ItemSearchDto itemSearchDto,
                        Model model) throws SQLException {
        List<Item> items = itemRepository.findAll(itemSearchDto);
        model.addAttribute("items", items);
        return "item/items";
    }

    @PostMapping
    public String items_search(@ModelAttribute("item") ItemSearchDto itemSearchDto,
                    Model model){
        List<Item> items = itemRepository.findAll(itemSearchDto);
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
        return "item/addForm";
    }


    /**
     * RedirectAttributes
     * redirect url에 값을 추가 시켜준다
     * @Validated를 이용하면 supports를 통해 필요한 검증기를 찾고 호출한다
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveDto form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

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
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) throws SQLException {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "item/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateDto form, BindingResult bindingResult) throws SQLException {

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
            return "item/editForm";
        }
        itemRepository.update(itemId,itemPara);
        return "redirect:/items/{itemId}";
    }
}
