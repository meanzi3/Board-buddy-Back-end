package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.MemberService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 아이디 중복 확인 요청
     *
     * @param verifyUsernameDuplicationDTO 사용자가 입력한 아이디
     * @return 아이디가 중복되지 않았다면 약속된 SuccessResponse 반환
     **/
    @PostMapping(value = "/api/auth/username/check")
    public ResponseEntity<ApiResponse<Void>> verifyUsernameDuplication(
            @RequestBody MemberRequest.VerifyUsernameDuplicationDTO verifyUsernameDuplicationDTO) {
        log.info("verify username duplication is working");

        memberService.verifyUsernameDuplication(verifyUsernameDuplicationDTO);

        return buildSuccessResponseWithoutData("사용가능한 아이디 입니다.", HttpStatus.OK);
    }

    /**
     * 닉네임 중복 확인 요청
     *
     * @param verifyNicknameDuplicationDTO 사용자가 입력한 닉네임
     * @return 닉네임이 중복되지 않았다면 약속된 SuccessResponse 반환
     **/
    @PostMapping(value = "/api/auth/nickname/check")
    public ResponseEntity<ApiResponse<Void>> verifyNicknameDuplication(
            @RequestBody MemberRequest.VerifyNicknameDuplicationDTO verifyNicknameDuplicationDTO) {
        log.info("verify nickname duplication is working");

        memberService.verifyNicknameDuplication(verifyNicknameDuplicationDTO);

        return buildSuccessResponseWithoutData("사용가능한 닉네임 입니다.", HttpStatus.OK);
    }

    /**
     * 회원가입 요청 캐치
     *
     * @param registerDTO 프론트로부터 전달받은 회원가입 정보
     * @return 회원가입에 성공했다면약속된 SuccessResponse 반환
     **/
    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody MemberRequest.RegisterDTO registerDTO) {
        log.info("register is working");

        memberService.registerMember(registerDTO);

        return buildSuccessResponseWithoutData("회원가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    /**
     * 신규 소셜 로그인 사용자의 회원가입 요청 캐치
     *
     * @param oAuth2RegisterDTO 프론트로부터 전달받은 소셜 로그인 유저 회원가입 정보
     * @param username 소셜 로그인 사용자 아이디
     * @return 첫 소셜 로그인 사용자가 회원가입에 성공했다면 약속된 SuccessResponse 반환
     **/
    @PostMapping(value = "/api/auth/oauth2/register")
    public ResponseEntity<ApiResponse<Void>> oAuth2Register(@RequestBody MemberRequest.OAuth2RegisterDTO oAuth2RegisterDTO,
                                                            @RequestAttribute String username) {
        log.info("social register is working");

        memberService.registerOAuth2Member(oAuth2RegisterDTO, username);

        return buildSuccessResponseWithoutData("소셜 회원가입 및 로그인이 완료되었습니다.", HttpStatus.CREATED);
    }

    /**
     * 회원 탈퇴 요청 캐치
     *
     * @param username 로그인 사용자 아이디
     * @return 회원 탈퇴가 완료되었다면 약속된 SuccessResponse 반환
     **/
    @PostMapping(value = "/api/auth/withdrawal")
    public ResponseEntity<ApiResponse<Void>> withdrawalMember(@RequestAttribute String username) {
        log.info("withdrawal member is working");

        memberService.withdrawalMember(username);

        return buildSuccessResponseWithoutData("회원탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

    /**
     * 내 동네 조회 요청 캐치
     *
     * @param username 로그인 사용자 아이디
     * @return 내 동네 조회를 성공했다면 약속된 SuccessResponse 반환
     */
    @GetMapping("/api/my/neighborhoods")
    public ResponseEntity<ApiResponse<MemberResponse.MyLocationsDTO>> getMemberNeighborhoods(@RequestAttribute String username) {
        log.info("getMemberNeighborhoods is working");

        MemberResponse.MyLocationsDTO myLocationsDTO = memberService.getMemberNeighborhoods(username);

        return buildSuccessResponseWithMultiplePairKeyData(myLocationsDTO, "내 동네 조회를 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 내 동네 설정 요청 캐치
     *
     * @param locationDTO 사용자가 입력한 위치 정보
     * @return 내 동네 설정을 성공했다면 약속된 SuccessResponse 반환
     **/
    @PutMapping("/api/my/neighborhoods")
    public ResponseEntity<ApiResponse<Map<String, Map<Integer, List<MemberResponse.LocationDTO>>>>> updateMemberNeighborhood(
            @RequestBody MemberRequest.LocationDTO locationDTO,
            @RequestAttribute String username) {
        log.info("updateMemberNeighborhood is working");

        Map<Integer, List<MemberResponse.LocationDTO>> locations = memberService.updateMemberNeighborhood(locationDTO, username);

        return buildSuccessResponseWithPairKeyData("locations", locations, "내 동네 설정을 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 내 반경 설정 요청 캐치
     *
     * @param radiusDTO 사용자가 입력한 반경 정보
     * @param username 로그인 사용자 아이디
     * @return 내 반경 설정을 성공했다면 약속된 SuccessResponse 반환
     **/
    @PutMapping("/api/my/radius")
    public ResponseEntity<ApiResponse<Void>> updateMemberRadius(
            @RequestBody MemberRequest.RadiusDTO radiusDTO,
            @RequestAttribute String username) {
        log.info("updateMemberRadius is working");

        memberService.updateMemberRadius(radiusDTO, username);

        return buildSuccessResponseWithoutData("내 반경 설정을 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 프로필 조회 요청 캐치
     *
     * @param nickname 유저 닉네임
     * @return 프로필 조회가 성공했다면 약속된 SuccessResponse 반환
     **/
    @GetMapping("/api/profiles/{nickname}")
    public ResponseEntity<ApiResponse<Map<String, MemberResponse.ProfileInfosDTO>>> getMemberProfileByNickname (@PathVariable String nickname) {
        log.info("get member profile is working");

        MemberResponse.ProfileInfosDTO profileInfosDTO = memberService.getMemberProfileByNickname(nickname);

        return buildSuccessResponseWithPairKeyData("profile", profileInfosDTO, "프로필이 조회되었습니다.", HttpStatus.OK);
    }

    /**
     * 프로필 수정 요청 캐치
     *
     * @param username         유저 아이디
     * @param updateProfileDTO 수정할 정보가 담겨있는 DTO
     * @param profileImageFile 수정할 프로필 이미지 파일
     * @return 프로필 조회가 성공했다면 약속된 SuccessResponse 반환
     **/
    @PutMapping("/api/profiles")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @RequestAttribute String username,
            @RequestPart(value = "UpdateProfileDTO") MemberRequest.UpdateProfileDTO updateProfileDTO,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        log.info("update Profile is working");

        memberService.updateProfile(username, updateProfileDTO, profileImageFile);

        return buildSuccessResponseWithoutData("프로필이 수정되었습니다.", HttpStatus.OK);
    }
}
