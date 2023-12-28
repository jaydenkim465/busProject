package com.busteamproject.DTO.address;

public class Meta {
    private int total_count;
    private int pageable_count;
    private boolean is_end;

    public int getTotalCount() {
        return total_count;
    }

    public int getPageableCount() {
        return pageable_count;
    }

    public boolean isEnd() {
        return is_end;
    }
}
