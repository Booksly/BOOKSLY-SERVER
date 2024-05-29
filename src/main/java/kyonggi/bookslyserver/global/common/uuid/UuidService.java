package kyonggi.bookslyserver.global.common.uuid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UuidService {

    private final UuidRepository uuidRepository;

    public Uuid createUuid() {
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid).build());
        return savedUuid;
    }
}
