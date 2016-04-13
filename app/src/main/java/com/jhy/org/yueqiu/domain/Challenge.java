package com.jhy.org.yueqiu.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class Challenge extends BmobObject {
    public static final int TYPE_SOLO = -1;      // solo赛
    public static final int TYPE_TRAIN = -2;    // 陪练赛
    public static final int TYPE_TEAM = -3;     // 团队赛
    public static final int STATE_INIT = 1;     // 发起挑战
    public static final int STATE_PREPARED = 2; // 等待开始
    public static final int STATE_PLAYING = 3;  // 挑战开始
    public static final int STATE_OVER = 4;     // 挑战结束
    public static final int STATE_DELETED = 5;  // 挑战删除

    private String title;           // 标题
    private String content;         // 内容
    private Person initiator;       // 发起人
    private Place place;            // 场地
    private BmobDate fromDate;      // 入场时间
    private BmobDate toDate;        // 散场时间
    private Integer type;           // 对战类型
    private Integer state;          // 挑战状态
    private BmobRelation responders;// 响应者
    private BmobRelation targets;   // 对战者

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
