package com.jhy.org.yueqiu.domain;

import com.baidu.mapapi.model.LatLng;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/*
 **********************************************
 * 			所有者 H: (黄振梓)
 **********************************************
 */
public class Challenge extends BmobObject {
    public static final String TYPE_SOLO = "1V1 SOLO赛";       // solo赛
    public static final String TYPE_TRAIN = "娱乐陪练";         // 陪练赛
    public static final String TYPE_TEAM = "3V3 组队对抗赛";    // 团队赛
    public static final int STATE_INIT = 1;     // 发起挑战
    public static final int STATE_PREPARED = 2; // 等待开始
    public static final int STATE_PLAYING = 3;  // 挑战开始
    public static final int STATE_OVER = 4;     // 挑战结束
    public static final int STATE_DELETED = 5;  // 挑战删除

    private String title;           // 标题
    private String content;         // 内容
    private Person initiator;       // 发起人
    private String placeUid;        // 场地标识
    private String placeName;       // 场地名字
    private String placeAddress;    // 场地地址
    private Double placeLatitude;    // 场地的纬度
    private Double placeLongitude;   // 场地的经度
    private BmobDate fromDate;      // 入场时间
    private BmobDate toDate;        // 散场时间
    private String type;           // 对战类型
    private Integer state;          // 挑战状态
    private BmobRelation responders;// 响应者
    private BmobRelation targets;   // 对战者

    private Place place;            // 场地, 已遗弃

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Person getInitiator() {
        return initiator;
    }
    public void setInitiator(Person initiator) {
        this.initiator = initiator;
    }

    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public String getPlaceAddress() { return placeAddress; }
    public void setPlaceAddress(String placeAddress) { this.placeAddress = placeAddress; }


    public Double getPlaceLatitude() { return placeLatitude; }
    public void setPlaceLatitude(Double placeLatitude) { this.placeLatitude = placeLatitude; }

    public Double getPlaceLongitude() { return placeLongitude; }
    public void setPlaceLongitude(Double placeLongitude) { this.placeLongitude = placeLongitude; }

    public LatLng getPlaceLatLng () {
        if (placeLatitude == null || placeLongitude == null) {
            return null;
        }
        return new LatLng(placeLatitude, placeLongitude);
    }
    public void setPlaceLatLng (LatLng latLng) {
        if (latLng != null) {
            placeLatitude = latLng.latitude;
            placeLongitude = latLng.longitude;
        }
    }


    public String getPlaceUid() { return placeUid; }
    public void setPlaceUid(String placeUid) { this.placeUid = placeUid; }

    public BmobDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(BmobDate fromDate) {
        this.fromDate = fromDate;
    }

    public BmobDate getToDate() {
        return toDate;
    }
    public void setToDate(BmobDate toDate) {
        this.toDate = toDate;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }

    public BmobRelation getResponders() {
        return responders;
    }
    public void setResponders(BmobRelation responders) {
        this.responders = responders;
    }

    public BmobRelation getTargets() {
        return targets;
    }
    public void setTargets(BmobRelation targets) {
        this.targets = targets;
    }
}
