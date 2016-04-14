package com.jhy.org.yueqiu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jhy.org.yueqiu.R;
import com.jhy.org.yueqiu.adapter.ChallengeAdapter;
import com.jhy.org.yueqiu.adapter.HomeGalleryAdapter;
import com.jhy.org.yueqiu.domain.Challenge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
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
        gallery = (Gallery) view.findViewById(R.id.gallery);
        List<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.icon_home_gallery2);
        list.add(R.drawable.icon_home_gallery1);
        list.add(R.drawable.icon_home_gallery3);
        HomeGalleryAdapter adapter = new HomeGalleryAdapter(getContext(), list);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(this);
        if (group != null) {
            group.setOnCheckedChangeListener(this);
        }
        Log.i("result", "buildup=--------------------");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (group != null) {
            RadioButton rb = (RadioButton) group.getChildAt(position);
            rb.setChecked(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                gallery.setSelection(0);
                break;
            case R.id.rb_two:
                gallery.setSelection(1);
                break;
            case R.id.rb_three:
                gallery.setSelection(2);
                break;
            default:
                break;
        }
    }

    //下半部分约占列表建立
    private void builddown() {
        Bmob.initialize(getContext(), "f250b019e456d9655d4467d4959ee79e");
        listView = (ListView) view.findViewById(R.id.lv_war);
        List<Challenge> list = new ArrayList<Challenge>();
        BmobQuery<Challenge> query = new BmobQuery<Challenge>();
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//当天日期
        BmobDate date = new BmobDate(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000));//两天前的日期
        query.addWhereGreaterThan("createdAt", date);

        query.findObjects(getContext(), new FindListener<Challenge>() {
            @Override
            public void onSuccess(List<Challenge> list) {
                ChallengeAdapter adapter = new ChallengeAdapter(list, getContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }
}

//向服务器添加数据
/*               SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");                 */
/*String time = sdf.format(new java.util.Date());                                      */
/*Challenge challenge = new Challenge();                                               */
/*    challenge.setTitle("3V3 组队赛");                                                   */
/*    challenge.setPublisher("用户3" );                                                  */
/*    challenge.setTime(time);                                                         */
/*    challenge.setPlace("某333篮球场");                                                   */
/*    challenge.setText("谁敢一战！团队的力量！");                                                */
/*    challenge.save(getContext(), new SaveListener() {                                */
/*        @Override                                                                    */
/*        public void onSuccess() {                                                    */
/*            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();         */
/*        }                                                                            */
/*
*/

/*        @Override                                                                    */
/*        public void onFailure(int i, String s) {                                     */
/*            Toast.makeText(getContext(), "保存失败" + s, Toast.LENGTH_SHORT).show();     */
/*        }                                                                            */
/*    });                                                                              */