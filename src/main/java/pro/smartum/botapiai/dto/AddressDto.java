package pro.smartum.botapiai.dto;

import lombok.Data;

@Data
public class AddressDto {
    
    private final AddressItemDto user;
    private final AddressItemDto conversation;

    @Data
    public static class AddressItemDto {
        private final String id;
        private final String name;
    }
}
