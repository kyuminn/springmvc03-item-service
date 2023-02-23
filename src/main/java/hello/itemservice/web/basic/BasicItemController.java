package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터 주입
     * @PostConstruct : 의존성 주입이 이루어진 후 초기화를 수행하는 메서드
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",1000000,10));
        itemRepository.save(new Item("itemB",2000000,20));
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    // @ModelAttribute를 쓰려면 input type의 name 값과 Item class의 필드이름 값을 일치시켜줘야함.
    // @ModelAttribute 대신 @RequestParam(name="itemName")String itemName 이런식으로 하나하나 받아도됨.. 여기서도 name은 form의 input name값
    // @ModelAttrubute로 한번에 객체를 받아서 setter 함수를 호출하는게 나아보임.
    // @ModelAttribute에 name 속성을 지정하면 Model 객체를 생성 후 addAttribute를 호출해 해당 name으로 객체를 넣어 view로 전달해주는 역할도 해줌.
    // 만약 name속성이 따로 없다면 class명의 첫글자만 소문자로 바꾼 item 이라는 이름으로 modelAttribute에 담기게 됨.
    // 이 경우는 String이나 int 타입이 아니기 때문에 @ModelAttribute를 생략한 채 Item item만 작성해도 자동으루 @ModelAttribute가 동작한다.
    // 만약 String,int와 같은 타입이면 @RequestParam이 자동으로 동작함
    @PostMapping("/add")
    public String save(@ModelAttribute(name="findItem")Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
//        model.addAttribute("findItem",item); // @ModelAttribute에 의해 자동으로 추가가되므로 생략 가능.
        // 숫자 하나인경우에는 문제가 되지 않지만 한글이나 공백이 있는 경우 +아래와 같이 return하는 것은 위험.
        // redirectAttribute 쓰기 권장. (encoding 다 해줌)
        //return "redirect:/basic/items/"+item.getId();
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true); // return url 에 {}로 따로 명시해주지 않으면 자동으로 쿼리파라미터로 넘어감
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("{id}")
    public String item(@PathVariable(name = "id")Long itemId,Model model){
        log.info("pathVariable ItemId =>{}", itemId);
        Item findItem = itemRepository.findbyId(itemId);
        model.addAttribute("findItem",findItem);
        return "/basic/item";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable(name="id")Long itemId,Model model){
        log.info("edit itemId =>{}", itemId);
        Item editItem = itemRepository.findbyId(itemId);
        model.addAttribute("editItem",editItem);
        return "basic/editForm";
    }

    @PostMapping("/edit/{itemId}")
    public String update(@PathVariable(name="itemId")Long itemId,@ModelAttribute(name="editItem")Item item){
        log.info("editItem =>{}",item.toString());
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }
}
