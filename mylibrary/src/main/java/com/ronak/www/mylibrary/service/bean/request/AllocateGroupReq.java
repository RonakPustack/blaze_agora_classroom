package com.ronak.www.mylibrary.service.bean.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ronak.www.mylibrary.service.bean.base.RoleConfig;

public class AllocateGroupReq {
    @NonNull
    private int memberLimit = 4;
    @Nullable
    private RoleConfig roleConfig;

    public AllocateGroupReq() {
    }
}
