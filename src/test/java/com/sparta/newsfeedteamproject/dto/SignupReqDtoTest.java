package com.sparta.newsfeedteamproject.dto;

import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupReqDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("SignupReqDto Validation 테스트: 성공")
    void allValidSuccess() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn01345",
                "@Tmdtn01345",
                "김승수",
                "snol4331@gmail.com",
                "안녕하세요1"
        );

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            assertNull(violation);
        }
    }

    @Test
    @DisplayName("SignupReqDto Validation username @Size 테스트: 실패")
    void usernameSizeValidFail() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn0134", // Size: 9
                "@Tmdtn01345",
                "김승수",
                "snol4331@gmail.com",
                "안녕하세요1"
        );

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            assertEquals("[username:size] 10자 이상 20자 이하로 작성해주세요!",violation.getMessage());
        }
    }

    @Test
    @DisplayName("SignupReqDto Validation password @Pattern 테스트: 실패")
    void passwordPatternValidFail() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn01345",
                "@tmdtn01345",  // 대문자 누락
                "김승수",
                "snol4331@gmail.com",
                "안녕하세요1"
        );

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            assertEquals("[password:pattern] 숫자, 영문 대소문자, 특수기호를 최소 한개씩 포함하여 작성해주세요!",violation.getMessage());
        }
    }

    @Test
    @DisplayName("SignupReqDto Validation name @NotBlank 테스트: 실패")
    void nameNotBlankValidFail() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn01345",
                "@Tmdtn01345",
                "",   // 입력 X
                "snol4331@gmail.com",
                "안녕하세요1"
        );

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            assertEquals("[name:blank] 사용자 이름을 작성해주세요!",violation.getMessage());
        }
    }

    @Test
    @DisplayName("SignupReqDto Validation email @Email 테스트: 실패")
    void emailEmailValidFail() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn01345",
                "@Tmdtn01345",
                "김승수",
                "snol4331gmail.com",   // 이메일 형식 X
                "안녕하세요1"
        );

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            assertEquals("[email:pattern] 이메일 형식을 맞춰주세요!",violation.getMessage());
        }
    }

    @Test
    @DisplayName("SignupReqDto 여러 개의 Validation 테스트: 실패")
    void validationsFail() {

        // given
        SignupReqDto reqDto = new SignupReqDto(
                "tmdtn01345",
                "@tmdtn01345",   // 대문자 누락
                "",   // 입력 X
                "snol4331gmail.com",   // 이메일 형식 X
                "안녕하세요1"
        );
        List<String> messageList = new ArrayList<>();

        // when
        Set<ConstraintViolation<SignupReqDto>> violations = validator.validate(reqDto);

        // then
        for (ConstraintViolation<SignupReqDto> violation : violations) {
            messageList.add(violation.getMessage());
        }

        assertTrue(messageList.contains("[password:pattern] 숫자, 영문 대소문자, 특수기호를 최소 한개씩 포함하여 작성해주세요!"));
        assertTrue(messageList.contains("[name:blank] 사용자 이름을 작성해주세요!"));
        assertTrue(messageList.contains("[email:pattern] 이메일 형식을 맞춰주세요!"));
        assertFalse(messageList.contains("[username:size] 10자 이상 20자 이하로 작성해주세요!"));
    }
}