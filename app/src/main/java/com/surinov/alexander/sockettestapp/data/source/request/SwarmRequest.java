package com.surinov.alexander.sockettestapp.data.source.request;

import com.surinov.alexander.sockettestapp.data.source.JsonSerializableRequest;

public interface SwarmRequest extends JsonSerializableRequest {
    long gerRequestId();
}
