package com.jhy.org.yueqiu.activity;

import android.app.Activity;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.TeamAdapter;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Team;
import com.jhy.org.yueqiu.utils.ImageLoader;
import com.jhy.org.yueqiu.utils.Logx;
import com.jhy.org.yueqiu.utils.RongUtils;
import com.jhy.org.yueqiu.utils.RoundTransform;
import com.jhy.org.yueqiu.view.ActionBarLayout;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
public class MyTeamActivity extends Activity implements OnClickListener{
    private ImageView iv_team_logo;//球队logo
    private EditText et_team_name;//球队名称
    private EditText et_team_slogan;//球队宣言
    private Button btn_team_addmemember;//添加成员按钮
    private TeamAdapter teamAdapter;
    private ListView lv_team_memember;
    private Team team;//我创建的球队
    private Team addteam;//我加入的球队
    private Context context = this;
    private BmobUser myTeam_bmobUser;
    private ImageLoader imageLoader;
    private BmobRelation  relation;
    private Person teamMember;//添加的球员
    private Intent addMemberIntent;
    private ActionBarLayout actionBarLayout;
    private int REQUEST_CODE_FOR_MESSAGE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        init();
        saveTeamInfo();
        showInfo();
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
        actionBarLayout.setOptionsOnClickListener(MyTeamActivity.this);
    }
    //判断登录状态
    private void saveTeamInfo(){
        myTeam_bmobUser = BmobUser.getCurrentUser(this);
        if(myTeam_bmobUser!=null){

        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
    //进入我的球队界面显示输入框中的信息
        private void showInfo(){
            Intent intent =getIntent();
            team = (Team) intent.getSerializableExtra("team");
            if(team!=null){
                et_team_name.setText(team.getName());
                et_team_slogan.setText(team.getMotto());
                String logo = team.getLogoUrl();
                Picasso.with(context)
                        .load(logo)
                        .transform(new RoundTransform())
                        .into(iv_team_logo);
            }else{

            }
        }
    //保存信息按钮的监听
    @Override
    public void onClick(View v) {
        String myteam_name = et_team_name.getText().toString();
        String myteam_slogan = et_team_slogan.getText().toString();
        if(myteam_name!=null && myteam_slogan!=null) {
            team.setCreator(BmobUser.getCurrentUser(this,Person.class));
            team.setName(myteam_name);
            team.setMotto(myteam_slogan);
            team.setMembers(relation);
            team.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MyTeamActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(MyTeamActivity.this, "保存失败" + s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //进入我的加入球队界面显示输入框中的信息
        private void showAddTeamInfo(){
            Intent intent =getIntent();
            addteam = (Team) intent.getSerializableExtra("addteam");
            if(addteam!=null){
                actionBarLayout.setTitleText("我加入球队");
                et_team_name.setText(addteam.getName());
                et_team_slogan.setText(addteam.getMotto());
                //btn_team_save.setVisibility(View.INVISIBLE);
                btn_team_addmemember.setVisibility(View.INVISIBLE);
                String logo = addteam.getLogoUrl();
                Picasso.with(context)
                        .load(logo)
                        .transform(new RoundTransform())
                        .into(iv_team_logo);
            }else{

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
                teamMember = null;

                // 得到返回的结果
                Person user = (Person) data.getSerializableExtra("result");
            }
        }
    }
    //添加成员按钮的监听
    public void teaminfoClick(View v){
                addMemberIntent = new Intent(context,ContactActivity.class);
                addMemberIntent.putExtra("message",true);
                startActivityForResult(addMemberIntent,REQUEST_CODE_FOR_MESSAGE);

                relation = new BmobRelation();
                relation.add(teamMember);
                team.setMembers(relation);
                team.update(this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        queryMember();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("onFailure","添加队员失败"+s);
                    }
            });
        }

    //上传队友加入球队的信息至Person表
    private  void addTeamBmob(){
        BmobRelation relationTeam = new BmobRelation();
        relationTeam.add(team);
        teamMember.setAddTeam(relationTeam);
        teamMember.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e("onSuccess","上传加入球队信息成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("onFailure","上传加入球队信息失败"+s);
            }
        });
    }
    //查询成员信息
    private void queryMember(){
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereRelatedTo("members", new BmobPointer(team));
        query.findObjects(this, new FindListener<Person>() {

            @Override
            public void onSuccess(List<Person> list) {
                teamAdapter = new TeamAdapter(MyTeamActivity.this,list);
                lv_team_memember.setAdapter(teamAdapter);
            }

            @Override
            public void onError(int code, String msg) {

            }
        });

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
                team = new Team();
                team.setLogoUrl(avatarUrl);
                team.update(context, team.getObjectId(), new UpdateListener() {
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
}
