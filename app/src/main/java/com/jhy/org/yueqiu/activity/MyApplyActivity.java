package com.jhy.org.yueqiu.activity;
/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 * 		修改：（夏旺）
 **********************************************
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.ApplyAdapter;
import com.jhy.org.yueqiu.adapter.ChallengeAdapter;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.MyChallege;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyApplyActivity extends Activity {
    private ListView lv_apply_info;
    private ListView lv_mychallenge;
    TextView tv_challenge; //提示信息（发起挑战）
    TextView tv_apply_test;     //提示信息（响应的报名）
    private BmobUser my_apply_bmobuser;
    private Context context = this;
    private Challenge challenge;
    private Person person;//当前用户
    Logx logx = new Logx(MyApplyActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
    }

    @Override
    protected void onStart() {
        super.onStart();
        build();
        myChallenge();
        applyChallenge();
    }

    private void build() {
        lv_apply_info = (ListView) findViewById(R.id.lv_apply_info);
        lv_mychallenge = (ListView) findViewById(R.id.lv_mychallenge);
        tv_challenge = (TextView) findViewById(R.id.tv_challenge);
        tv_apply_test = (TextView) findViewById(R.id.tv_apply_test);
        person = BmobUser.getCurrentUser(MyApplyActivity.this, Person.class);
        judgeLogin();
        registerForContextMenu(lv_mychallenge);
    }

    private List<Challenge> challengeList = new ArrayList<>();
    private ChallengeAdapter challengeAdapter = null;

    //查询我发起的挑战
    private void myChallenge() {
        challengeAdapter = new ChallengeAdapter(challengeList, this, false);
        Person person = BmobUser.getCurrentUser(this, Person.class);
        if (person != null) {
            BmobQuery<Challenge> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("initiator", person);
            bmobQuery.include("initiator");
            bmobQuery.findObjects(context, new FindListener<Challenge>() {
                @Override
                public void onSuccess(List<Challenge> list) {
                    challengeList.clear();
                    challengeList.addAll(list);
                    if (Utils.isEmpty(list)) {
                        tv_challenge.setVisibility(View.VISIBLE);
                        lv_mychallenge.setAdapter(challengeAdapter);
                    } else {
                        lv_mychallenge.setAdapter(challengeAdapter);
                    }
//                    challengeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int i, String s) {
                    Log.i("Mychallenge", "查询List失败，" + s + i);
                }
            });
        }
    }

    private List<Challenge> challengeList1 = new ArrayList<>();
    private ChallengeAdapter challengeAdapter1 = null;

    //查询我报名的挑战
    private void applyChallenge() {
        challengeAdapter1 = new ChallengeAdapter(challengeList1, this, false);
        person = BmobUser.getCurrentUser(this, Person.class);
        if (person != null) {
            MyChallege.query(person, new FindListener<MyChallege>() {
                @Override
                public void onSuccess(List<MyChallege> list) {
                    challengeList1.clear();
                    if (Utils.isEmpty(list)) {
                        tv_apply_test.setVisibility(View.VISIBLE);
                        lv_apply_info.setAdapter(challengeAdapter1);
                    } else {
                        challengeList1.clear();
                        for (MyChallege i : list) {
                            challengeList1.add(i.getChallenge());

                            if (i.getChallenge().getInitiator() == null) {
                                Log.e("ilkdfldfd", "发起人为空");
                            }
                        }
                        lv_apply_info.setAdapter(challengeAdapter1);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    //长按弹出菜单删除
    private static final int ITEM_DELETE = 113;
    AdapterView.AdapterContextMenuInfo info;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.addSubMenu(menu.NONE, ITEM_DELETE, menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case ITEM_DELETE:
                Challenge challenge = challengeList.get(info.position);
                //MyChallenge表中删除challenge
                MyChallege.delete(challenge);
                //challenge自身删除
                challenge.delete(context, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("challenge", "challenge===+++++++删除成功");
                        challengeList.remove(info.position);
                        challengeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });


                break;
            default:
                break;
        }
        return true;
    }

    //判断登录状态并对报名列表进行监听
    private void judgeLogin() {
        my_apply_bmobuser = BmobUser.getCurrentUser(context);
        if (my_apply_bmobuser != null) {
            lv_apply_info.setOnItemClickListener(click);
        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }

    //点击Item项事件 弹出对手信息
    AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            Challenge challenge = (Challenge) parent.getItemAtPosition(position);
            String type = challenge.getType();
            Intent intent;

            if (type.equals(Challenge.TYPE_SOLO) || type.equals(Challenge.TYPE_TRAIN)) {
                intent = new Intent(MyApplyActivity.this, OpponentActivity.class);
                intent.putExtra("person", challenge.getInitiator());
                Log.i("rea", "challenge===========" + challenge.getInitiator());
                startActivity(intent);
            } else if (type.equals(Challenge.TYPE_TEAM)) {
                intent = new Intent(MyApplyActivity.this, OpponentTeamActivity.class);
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            }

        }
    };

    //返回按钮的监听
    public void myApplyBackClick(View v) {
        finish();
    }
}
