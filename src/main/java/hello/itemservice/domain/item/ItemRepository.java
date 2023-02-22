package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long,Item> store = new HashMap<>(); //static (멀티스레드 환경에는 ConcurrentHashMap 사용해야함)
    private static long sequence =0L; //static (멀티스레드 환경에서는 automicLong? 사용해야함)

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(),item);
        return item;
    }

    public Item findbyId(Long id){
        Item item = store.get(id);
        return item;
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId,Item updateParam){
        Item findItem = findbyId(itemId);
        // item class에서 id를 제외한 필드로 dto를 만드는 방식이 정석임 (중복이지만, 명확하기때문) (중복성 < 명확성 우선순위..)
        // e.g) updateItemParamDTO 처럼!
        // 이 경우는 테스트하니까 그냥 이 방식으로 set
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
