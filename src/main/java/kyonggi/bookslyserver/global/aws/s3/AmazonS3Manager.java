package kyonggi.bookslyserver.global.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kyonggi.bookslyserver.global.common.uuid.Uuid;
import kyonggi.bookslyserver.global.config.AmazonConfig;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;


    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            //S3 버킷에 저장
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        //저장된 파일의 url 겟챠!
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    //keyName: 버킷에 올라갈 객체의 이름
    public String generateReviewKeyName(Uuid uuid, String originalFilename) {
        return amazonConfig.getReviewPath() + '/' + uuid.getUuid() + '/' + originalFilename;
    }

    public String generateMenuKeyName(Uuid uuid, String originalFilename) {
        return amazonConfig.getMenuPath() + '/' + uuid.getUuid() + '/' + originalFilename;
    }

    public String generateEmployeeKeyName(Uuid uuid) {
        return amazonConfig.getEmployeePath() + '/' + uuid.getUuid();
    }

    public String generateShopKeyName(Uuid uuid, String originalFilename) {
        return amazonConfig.getShopPath() + '/' + uuid.getUuid() + '/' + originalFilename;
    }

    public void deleteFile(String keyName) {
        try {
            // deleteObject(버킷명, 키값)으로 객체 삭제
            amazonS3.deleteObject(amazonConfig.getBucket(), keyName);
        } catch (AmazonServiceException e) {
            log.error(e.toString());
        }
    }

    public String extractKeyNameFromUrl(String url) {
        try {
            // URL에서 keyName 부분만 추출하기 위한 정규 표현식
            Pattern pattern = Pattern.compile("https://[^/]+/(.+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                throw new IllegalArgumentException("Invalid S3 URL");
            }
        } catch (Exception e) {
            log.error("Error extracting keyName from URL: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid S3 URL", e);
        }
    }

    public void deleteFileFromUrl(String url) {
        String keyNameFromUrl = extractKeyNameFromUrl(url);
        deleteFile(keyNameFromUrl);
    }

}
