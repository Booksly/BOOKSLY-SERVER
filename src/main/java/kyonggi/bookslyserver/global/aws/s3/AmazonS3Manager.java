package kyonggi.bookslyserver.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kyonggi.bookslyserver.global.common.Uuid;
import kyonggi.bookslyserver.global.common.UuidRepository;
import kyonggi.bookslyserver.global.config.AmazonConfig;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.InternalServerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

}
