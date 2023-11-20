package com.lepl.api.character;

import com.lepl.Service.character.CharacterItemService;
import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ItemService;
import com.lepl.Service.member.MemberService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.CharacterItem;
import com.lepl.domain.character.Item;
import com.lepl.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/characterItem")
@Slf4j
public class CharacterItemApiController {

    private final CharacterItemService characterItemService;
    private final CharacterService characterService;
    private final MemberService memberService;
    private final ItemService itemService;

    /*
        아이템 구매 버튼
     */
    @PostMapping("/buy/{itemId}")
    public ResponseEntity<String> buyItem(@PathVariable("itemId") Long itemId, @Login Long memberId) {
        Item item = itemService.findOne(itemId);
        Member member = memberService.findOne(memberId);
        Long characterId = member.getCharacter().getId();
        Character character = characterService.findOne(characterId);

        CharacterItem characterItem = new CharacterItem();
        characterItem.setWearingStatus(Boolean.FALSE);
        characterItem.setCharacter(character);
        log.debug("캐릭터 확인용 = {}", character.getId());

        int cnt = item.getPurchase_quantity(); //현재 재고 확인
        Long money = character.getMoney(); //보유 화폐
        int price = item.getPrice(); //아이템 가격

        Long wantItemId = item.getId(); //사용자가 구매하고자하는 아이템
        List<CharacterItem> items = characterItemService.findAll(); //사용자 소유 아이템

        for(CharacterItem i : items) {
            if(i.getItem().getId() == wantItemId && i.getCharacter().getId() == character.getId()) { //만약 이미 해당 아이템을 소유하고 있다면
                throw new IllegalStateException("이미 소유한 아이템입니다.");
            }
        }

        if(money >= price) { //현재 보유한 화폐로 아이템을 구매할 수 있다면
            if(cnt >= 1) { //재고 수량이 있다면
                characterItem.setItem(item);
                characterItemService.save(characterItem); //아이템까지 추가해서 저장
                characterService.updateCoin(money - price); //아이템 구매로 인한 화폐 차감 -> 더티체킹
                itemService.updatePurchase(cnt - 1); //아이템은 1개씩 구매 가능

                log.debug("remain money = {}", (money - price));
                log.debug("remain purchase quantity = {}", cnt);
            } else { //재고 수량이 없다면
                log.debug("remain purchase quantity = {}", cnt);
                throw new IllegalStateException("아이템을 구매할 수 없습니다 - 재고 부족");
            }
        } else { //현재 보유한 화폐로 아이템을 구매할 수 없다면
            log.debug("need more money = {}", (price - money));
            throw new IllegalStateException("아이템을 구매할 수 없습니다 - 한도초과");
        }

        return ResponseEntity.status(HttpStatus.OK).body("아이템 구매를 완료하였습니다."); //200 OK
    }

    /*
        아이템 착용 여부 변경
        아이템 착용o -> 1
        아이템 착용x -> 0
        Item Type에 따라(Room, Character) putItem, wearItem으로 구분해도 될거 같음!
     */
    @GetMapping("/put/{itemId}/{status}")
    public ResponseEntity<String> putItem(@PathVariable("itemId") Long itemId, @PathVariable("status") int status, @Login Long memberId) {
        Member member = memberService.findOne(memberId);
        Character character = member.getCharacter();
        List<CharacterItem> characterItems = character.getCharacterItems();
        CharacterItem characterItem = null;

        for(CharacterItem c : characterItems) {
            if(c.getItem().getId() == itemId) { //캐릭터가 아이템을 보유하고 있다면
                characterItem = c;
                break; //어차피 해당 아이템은 사용자 한 명 당 1개면 소유할 수 있기에 찾으면 바로 탈출
            }
        }

        if(characterItem != null) { //아이템 소유중
            if(status == 1) { //사용o
                characterItemService.updateStatus(characterItem.getId(), status);
            } else { //사용x
                characterItemService.updateStatus(characterItem.getId(), status);
            }
        } else {
            throw new IllegalStateException("소유한 아이템이 아닙니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("아이템 착용여부 변경 완료했습니다.");
    }
}
