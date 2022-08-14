package infitry.rest.api.repository.domain.embedded;

import infitry.rest.api.dto.user.AddressDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(columnDefinition = "varchar(20)")
    String zipCode;
    @Column(columnDefinition = "varchar(255)")
    String address;
    @Column(columnDefinition = "varchar(255)")
    String addressDetail;

    private Address(AddressDto addressDto) {
        this.zipCode = addressDto.getZipCode();
        this.address = addressDto.getAddress();
        this.addressDetail = addressDto.getAddressDetail();
    }

    public static Address createAddress(AddressDto addressDto) {
        return new Address(addressDto);
    }
}
