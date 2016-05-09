package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.TeamAdapter;
import com.jhy.org.yueqiu.domain.AddTeam;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.utils.ImageLoader;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.utils.Utils;
import com.jhy.org.yueqiu.view.ActionBarLayout;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/*
 **********************************************
 * 			所有者 C: (曹昌盛)
 **********************************************
 */

public class MyTeamActivity extends Activity{

    private ImageView iv_team_logo;//球队logo
    private EditText et_team_name;//球队名称
    private EditText et_team_slogan;//球队宣言
    private Button btn_team_addmemember;//添加成员按钮
    private TeamAdapter teamAdapter;
    private List<Person> memberList = new ArrayList<>();
    private ListView lv_team_memember;
    private Team team;//我创建的球队
    private Context context = this;
    private BmobUser myTeam_bmobUser;
    private ImageLoader imageLoader;
    private BmobRelation  relation;
    private Person teamMember;//添加的球员
    private Intent memberIntent;//我的球队成员的跳转
    private Intent addMemberIntent;//添加成员按钮的跳转
    private AddTeam addTeam;
    private ActionBarLayout actionBarLayout;
    private int REQUEST_CODE_FOR_MESSAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        init();
        saveTeamInfo();
        resolveIntent(getIntent());
    }

    //初始化控件
    private void init(){
        iv_team_logo = (ImageView) findViewById(R.id.iv_team_logo);
        et_team_name = (EditText) findViewById(R.id.et_team_name);
        et_team_slogan = (EditText) findViewById(R.id.et_team_slogan);
        btn_team_addmemember = (Button) findViewById(R.id.btn_team_addmemember);
        lv_team_memember = (ListView) findViewById(R.id.lv_team_memember);
        imageLoader = new ImageLoader(MyTeamActivity.this,iv_team_logo);
        actionBarLayout = (ActionBarLayout) findViewById(R.id.actionbar_team_title);
        actionBarLayout.setOptionsOnClickListener(click);

        lv_team_memember.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                memberIntent = new Intent(MyTeamActivity.this, OpponentActivity.class);
                Person person = memberList.get(position);
                memberIntent.putExtra("person", person);
                startActivity(memberIntent);
            }
        });
    }

    //保存按钮的监听
        View.OnClickListener click = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String myteam_name = et_team_name.getText().toString();
                String myteam_slogan = et_team_slogan.getText().toString();
                if(Utils.isEmpty(myteam_name)){
                    toast("请创建你喜欢的球队名称");
                }else if(Utils.isEmpty(myteam_slogan)){
                    toast("请创建你喜欢的球队口号");
                }else {
                    Team myTeam = new Team();
                    myTeam.setCreator(BmobUser.getCurrentUser(MyTeamActivity.this, Person.class));
                    myTeam.setName(myteam_name);
                    myTeam.setMotto(myteam_slogan);
                    myTeam.setMembers(relation);
                    MyTeamActivity.this.team = myTeam;
                    myTeam.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("保存成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("保存失败");
                        }
                    });
                }
            }
        };
    //判断登录状态
    private void saveTeamInfo(){
        myTeam_bmobUser = BmobUser.getCurrentUser(this);
        if(myTeam_bmobUser!=null){

        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
    //从MyAllTeamActivity传来的team，并进行判断
    private void resolveIntent (Intent intent) {
        Team team = (Team) intent.getSerializableExtra("team");
        boolean myOwn = intent.getBooleanExtra("myOwn", false);
        showTeamInfo(team, myOwn);
        this.team = team;
    }
    //判断传来的team是我创建的还是我加入的
    private void showTeamInfo (Team team, boolean myOwn) {
        if (team != null) {
            if (!myOwn) {
                actionBarLayout.setTitleText("我加入球队");
                iv_team_logo.setClickable(false);

                et_team_name.setFocusable(false);
                et_team_name.setFocusableInTouchMode(false);
                et_team_name.requestFocus();
                et_team_slogan.setFocusable(false);
                et_team_slogan.setFocusableInTouchMode(false);
                et_team_slogan.requestFocus();

                actionBarLayout.getOptionsView().setVisibility(View.INVISIBLE);
                btn_team_addmemember.setVisibility(View.GONE);
            }
            et_team_name.setText(team.getName());
            et_team_slogan.setText(team.getMotto());
            Picasso.with(context)
                    .load(team.getLogoUrl())
                    .transform(new RoundTransform())
                    .into(iv_team_logo);
            queryMember(team);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageLoader != null) {
            uploadFile(imageLoader.setResult(requestCode, data));
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_MESSAGE) {
                // 得到返回的结果
                Team team = new Team();
                teamMember = (Person) data.getSerializableExtra("result");
                relation = new BmobRelation();
                relation.add(teamMember);
                team.setMembers(relation);
                team.update(this, this.team.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MyTeamActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                       queryMember(MyTeamActivity.this.team);
                        addTeamInfo(teamMember, MyTeamActivity.this.team);
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("onFailure","添加队员失败"+s);
                    }
                });
            }
        }
    }
    //添加成员按钮的监听
    public void teaminfoClick(View v){
        addMemberIntent = new Intent(MyTeamActivity.this,ContactActivity.class);
        addMemberIntent.putExtra("message", true);
        addMemberIntent.putExtra("noTeamMembers", true);
       // addMemberIntent.putExtra("teamMembers", (Object)memberList);

        startActivityForResult(addMemberIntent, REQUEST_CODE_FOR_MESSAGE);
    }
    //查询成员信息
    private void queryMember(Team team){
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereRelatedTo("members", new BmobPointer(team));
        query.findObjects(this, new FindListener<Person>() {

            @Override
            public void onSuccess(final List<Person> list) {
                Log.e("onSuccess", "我的队员信息" + list);
                memberList.clear();
                memberList.addAll(list);
                teamAdapter = new TeamAdapter(MyTeamActivity.this, memberList);
                lv_team_memember.setAdapter(teamAdapter);
            }

            @Override
            public void onError(int code, String msg) {

            }
        });
    }
    //添加信息到AddTeam表
    private void addTeamInfo(Person teamMember,Team team){
        addTeam = new AddTeam();
        addTeam.setMember(teamMember);
        addTeam.setAddTeam(team);
        addTeam.save(MyTeamActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e("addTeamInfo","添加信息成功");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    protected void onPause () {
        super.onPause();
        RongUtils.saveUserInfo();
    }

    //上传头像
    static private Logx logx = new Logx(MyTeamActivity.class);
    private BmobFile bmobFile;
    private String avatarUrl = "";

    private void uploadFile (File file) {
        if (file == null) {
            return;
        }

        bmobFile = new BmobFile(file);
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                logx.e("上传文件 成功: url = " + bmobFile.getFileUrl(context));

                avatarUrl = bmobFile.getFileUrl(context);
                Team _team = new Team();
                _team.setLogoUrl(avatarUrl);
                Log.e("onSuccess","上传头像"+team);
                _team.update(context, team.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        logx.e("更新用户头像 成功!");
                        String userId = team.getObjectId();
                        String username = team.getName();
                        RongUtils.refreshUserInfo(userId, username, avatarUrl);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        logx.e("更新用户头像 失败!");
                        //更新失败, 应该删除网络上已上传的头像
                        //这里暂时不操作
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                logx.e("上传文件 失败: url = " + bmobFile.getFileUrl(context));
                logx.e("\t\t\t" + s);
            }
        });
    }
    private void toast(String str){
        Toast.makeText(MyTeamActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
