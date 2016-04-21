package com.jhy.org.yueqiu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.activity.MyProfileActivity;
import com.jhy.org.yueqiu.activity.OpponentActivity;
import com.jhy.org.yueqiu.activity.OpponentTeamActivity;
import com.jhy.org.yueqiu.activity.ResponseChallengeActivity;
import com.jhy.org.yueqiu.adapter.ChallengeAdapter;
import com.jhy.org.yueqiu.adapter.HomeGalleryAdapter;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Challenge;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.domain.Place;
import com.jhy.org.yueqiu.view.ChallengeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

/*
 **********************************************
 * 			所有者 X: (夏旺)
 **********************************************
 */
public class HomeFragment extends Fragment implements OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    RadioGroup group;
    Gallery gallery;
    View view;
    ListView listView;
    RadioGroup markGroup;
    ScrollView sv_nbawar;
    ScrollView sv_nbapk;
    TextView tv_war;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_gallery, null);
        buildup();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        builddown();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void buildup() {
        group = (RadioGroup) view.findViewById(R.id.rg_title);
        markGroup = (RadioGroup) view.findViewById(R.id.markGroup);
        gallery = (Gallery) view.findViewById(R.id.gallery);
        sv_nbawar = (ScrollView) view.findViewById(R.id.sv_nbawar);
        sv_nbapk = (ScrollView) view.findViewById(R.id.sv_nbapk);

        List<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.icon_home_gallery1);
        list.add(R.drawable.icon_home_gallery2);
        list.add(R.drawable.icon_home_gallery3);
        HomeGalleryAdapter adapter = new HomeGalleryAdapter(getContext(), list);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(this);
        if (markGroup != null) {
            markGroup.setOnCheckedChangeListener(this);
        }
        //group.setOnCheckedChangeListener(click);
    }


    //标题group的选择监听
    RadioGroup.OnCheckedChangeListener click = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_one:
                    sv_nbawar.setVisibility(View.VISIBLE);
                    gallery.setVisibility(View.INVISIBLE);
                    sv_nbapk.setVisibility(View.INVISIBLE);
                    break;
                case R.id.rb_two:
                    gallery.setVisibility(View.VISIBLE);
                    sv_nbawar.setVisibility(View.INVISIBLE);
                    sv_nbapk.setVisibility(View.INVISIBLE);
                    break;
                case R.id.rb_three:
                    sv_nbapk.setVisibility(View.VISIBLE);
                    gallery.setVisibility(View.INVISIBLE);
                    sv_nbawar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    //Gallery的欢动监听
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (markGroup != null) {
            RadioButton rb = (RadioButton) markGroup.getChildAt(position);
            rb.setChecked(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //markGroup的选择监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_markone:
                gallery.setSelection(0);
                break;
            case R.id.rb_marktwo:
                gallery.setSelection(1);
                break;
            case R.id.rb_markthree:
                gallery.setSelection(2);
                break;
            default:
                break;
        }
    }

    //下半部分约占列表建立
    private void builddown() {
        tv_war = (TextView) view.findViewById(R.id.tv_war);
        tv_war.setOnClickListener(clickwar);
        Bmob.initialize(getContext(), Key.bmob.application_id);
        listView = (ListView) view.findViewById(R.id.lv_war);
        List<Challenge> list = new ArrayList<Challenge>();
        BmobQuery<Challenge> query = new BmobQuery<Challenge>();
//        Date d = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//当天日期
//        BmobDate date = new BmobDate(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000));//两天前的日期
//        query.addWhereGreaterThan("createdAt", date);

        query.setLimit(3);
        query.order("-createdAt");//设置按照时间大小降序排列
        query.include("initiator");
        query.findObjects(getContext(), new FindListener<Challenge>() {
            @Override
            public void onSuccess(List<Challenge> list) {
                ChallengeAdapter adapter = new ChallengeAdapter(list, getContext());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(itemClick);
            }

            @Override
            public void onError(int i, String s) {

            }
        });



    }
    //点击约战列表 弹出详细约战长列表
    View.OnClickListener clickwar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    //点击Item项事件 弹出对手信息
    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Challenge challenge = (Challenge) parent.getItemAtPosition(position);
            String type = challenge.getType();
            Intent intent;

            if (type.equals(Challenge.TYPE_SOLO) || type.equals(Challenge.TYPE_TRAIN)) {
                intent = new Intent(getContext(), OpponentActivity.class);
                intent.putExtra("challenge", challenge);
                Log.i("rea", "challenge===========" + challenge.getInitiator());
                startActivity(intent);
            } else if (type.equals(Challenge.TYPE_TEAM)) {
                intent = new Intent(getContext(), OpponentTeamActivity.class);
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            }

        }
    };


    Challenge challenge;
    Person p1;
    Place place;

    private void addBmobDate() {
        //向服务器添加数据
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
//        String time = sdf.format(new java.util.Date());
        //上传用户选择挑战类型
        challenge = new Challenge();
        challenge.setType(Challenge.TYPE_SOLO);
        //获得用户名字并添加到Challenge表中
        p1 = new Person();
        p1.setObjectId("622cdb9543");
        challenge.setInitiator(p1);
        //上传用户选择约战开始时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse("2016-04-14 15:30:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobDate d1 = new BmobDate(date);
        challenge.setFromDate(d1);

        BmobUser.getCurrentUser(getContext()).getObjectId();
        //添加地点
        place = new Place();
        place.setObjectId("25b967cef0");
        challenge.setPlace(place);
        //上传用户编辑的标题内容
        challenge.setTitle("aaaaa来来来！约起来！大家一起玩!");
        challenge.save(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(int i, String s) {
                Log.i("result", "SaveListener=============" + s);
            }
        });
    }

    //添加Place的数据（地点）
    String placeId;

    private void addPlace() {
        //上传用户选择的篮球场地点
        place = new Place();
        place.setName("某某篮球场3");
        place.save(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                placeId = place.getObjectId();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    //上传Person的数据（用户名）
    String personId;

    private void addPerson() {
        p1 = new Person();
        p1.setUsername("用户3");
        p1.setPassword("123456");
        p1.setAddress("ccccccc");
        p1.signUp(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i("result", "addPerson保存成功=============");
                personId = p1.getObjectId();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    private void addAvader() {
        //上传用户的头像
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //File fileSD = Environment.getExternalStorageDirectory();
            String filePath = "/storage/emulated/legacy/Download/avatar1.jpg";
            BmobProFile proFile = new BmobProFile();
            proFile.upload(filePath, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    Log.i("result", bmobFile.getUrl());
                    Toast.makeText(getContext(), "保存图片成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int i, String s) {
                    Log.i("result", "onError===============" + s);
                    Toast.makeText(getContext(), "保存图片失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

