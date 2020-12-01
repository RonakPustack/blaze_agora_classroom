package com.ronak.www.mylibrary.service;

import com.ronak.www.mylibrary.classroom.bean.board.BoardBean;
import com.ronak.www.mylibrary.service.bean.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BoardService {

    @GET("/board/apps/{appId}/v1/rooms/{roomUuid}")
    Call<ResponseBody<BoardBean>> getBoardInfo(
            @Header("token") String userToken,
            @Path("appId") String appId,
            @Path("roomUuid") String roomUuid
    );

}
