package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.converter.ReservationConverter;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveCommandService {
    private final ReservationSettingRepository reservationSettingRepository;
    private final ShopRepository shopRepository;
    
    public ReserveResponseDTO.reservationSettingResultDTO setReservationSetting(ReserveRequestDTO.reserveSettingRequestDTO request, Long shopId){
        Shop shop=shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if ((request.getRegisterMin() == null && request.getRegisterHr() == null)
                || (request.isAuto() && request.getMaxCapacity() == null)){
            throw new InvalidValueException();
        }

        ReservationSetting reservationSetting= ReservationConverter.toReservationSetting(request);
        reservationSetting.setShop(shop);
        return ReservationConverter.toReservationSettingResultDTO(reservationSettingRepository.save(reservationSetting));
    }
}
