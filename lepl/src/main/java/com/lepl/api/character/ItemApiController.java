package com.lepl.api.character;

import com.lepl.Service.character.ItemService;
import com.lepl.domain.character.Item;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/item")
@Slf4j
public class ItemApiController {

    private final ItemService itemService;

    /**
     * 아이템 등록
     */
    @GetMapping("/add")
    public ResponseEntity<ItemDto> saveItem(@RequestBody @Valid ItemDto request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setType(request.getType());
        item.setPrice(request.getPrice());
        item.setPurchase_quantity(request.getPurchase_quantity());
        item.setStart_time(request.getStart_time());
        item.setEnd_time(request.getEnd_time());

        //아이템 등록
        itemService.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ItemDto(item));
    }

    /*
        등록된 특정 아이템 조회
        Item 형태로 받기
     */
    @GetMapping("/find/id/{itemId}")
    public Item findItem(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findOne(itemId);
        log.debug("item = {}", item);

        if(item == null) {
            throw new IllegalStateException("해당 아이템이 존재하지 않습니다.");
        }
        log.debug("item id = {}", itemId);

        return item;
    }

    /*
        등록된 아이템 이름으로 조회
     */
    @GetMapping("/find/name/{itemName}")
    public Item findItemByName(@PathVariable("itemName") String itemName) {
        Item item = itemService.findByName(itemName);
        log.debug("item = {}", item);

        if(item == null) {
            throw new IllegalStateException("해당 아이템이 존재하지 않습니다.");
        }
        log.debug("item name = {}", item.getName());

        return item;
    }

    /*
        등록된 전체 아이템 조회
        List 형태로 받기
     */
    @GetMapping("/all")
    public List<ItemDto> findItems() {
        List<Item> items = itemService.findAll();
        List<ItemDto> result = items.stream()
                .map(o -> new ItemDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Getter
    @NoArgsConstructor //no Creators 에러 해결, 내부적으로 Json을 Java로 변환할 때 생기는 오류
    static class ItemDto {
        private String type;
        private String name;
        private int price;
        private int purchase_quantity;
        private LocalDateTime start_time;
        private LocalDateTime end_time;

        public ItemDto(Item item) {
            this.type = item.getType();
            this.name = item.getName();
            this.price = item.getPrice();
            this.purchase_quantity = item.getPurchase_quantity();
            this.start_time = item.getStart_time();
            this.end_time = item.getEnd_time();
        }
    }


}
