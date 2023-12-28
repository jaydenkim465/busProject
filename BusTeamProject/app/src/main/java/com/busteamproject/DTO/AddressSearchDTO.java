package com.busteamproject.DTO;

import com.busteamproject.DTO.address.Documents;
import com.busteamproject.DTO.address.Meta;

import java.util.List;

// 참고 Link : https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-sample

public class AddressSearchDTO {
    private Meta meta;
    private List<Documents> documents;

    public Meta getMeta() {
        return meta;
    }

    public List<Documents> getDocuments() {
        return documents;
    }
}
