package sumcoda.boardbuddy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sumcoda.boardbuddy.dto.FileDTO;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileStorageUtil {

    private static final String ROOT_PATH = System.getProperty("user.dir");

    public static final String LOCAL_STORE_FULL_DIR = ROOT_PATH + "/src/main/resources/static/uploadFiles/badgeImages/";
    public static final String LOCAL_STORE_DIR = "/uploadFiles/badgeImages/";

    public static String getFullLocalPath (String filename) {
        return LOCAL_STORE_FULL_DIR + filename;
    }

    public static String getLocalStoreDir(String filename) {
        return LOCAL_STORE_DIR + filename;
    }

    public static FileDTO saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        // 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        assert originalFilename != null;
        String savedFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        // 디렉토리가 없으면 생성
        log.info(LOCAL_STORE_FULL_DIR);
        new File(LOCAL_STORE_FULL_DIR).mkdirs();

        File image = new File(LOCAL_STORE_FULL_DIR, savedFilename);

        // 파일을 디렉토리에 저장
        file.transferTo(image);

        // 저장된 파일의 경로 반환
        return FileDTO.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .file(image)
                .build();
    }

    // 확장자 추출
    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return (pos != -1) ? originalFilename.substring(pos + 1) : "";
    }
}
