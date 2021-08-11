package com.zyf.net.response;

import com.zyf.model.Result;

public interface OnSuccessAndFaultListener {
    void onSuccess(Result result,String method);

    void onFault(Result result,String method);
}

