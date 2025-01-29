package umc7th.bulk.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.group.dto.GroupMapResponseDto;
import umc7th.bulk.group.dto.TodayMemberDto;
import umc7th.bulk.group.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;


    @GetMapping("/{groupId}/map")
    public CustomResponse<GroupMapResponseDto> getGroupMap(@PathVariable Long groupId) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, groupService.getGroupMap(groupId));
    }

    @GetMapping("/{groupId}/today")
    public ResponseEntity<CustomResponse<List<TodayMemberDto>>> getTodayMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, groupService.getTodayMembers(groupId)));
    }
}
