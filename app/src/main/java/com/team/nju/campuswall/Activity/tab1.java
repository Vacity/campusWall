package com.team.nju.campuswall.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.nju.campuswall.Adapter.ListItemClickHelp;
import com.team.nju.campuswall.Adapter.messageListAdapter;
import com.team.nju.campuswall.Model.MessageModel;
import com.team.nju.campuswall.Model.UserModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab1 extends Fragment implements ListItemClickHelp,NetworkCallbackInterface.NetRequestIterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private netRequest requestFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<MessageModel> messageModel= new ArrayList<MessageModel>();
    private OnFragmentInteractionListener mListener;
    private ListView listView=null;
    private String phone = mainActivity.phone;              //正在登录的用户，可能未登录为null
    private Spinner spinner;
    int tempAcid;                                           //暂时记录进行操作的动态id
    public tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static tab1 newInstance(String param1, String param2) {
        tab1 fragment = new tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //服务器请求
        requestFragment=new netRequest(this,this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, null);//注意不要指定父视图
        listView = (ListView) view.findViewById(R.id.list1);
        spinner = (Spinner) view.findViewById(R.id.sort1);

        spinner.setSelected(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {               //pos 0,1,2 分别代表时间，评论，点赞
                switch(pos) {
                    case 0: {
                        Map map = new HashMap();
                        map.put("phone", phone);
                        map.put("sortby", "time");     //tiome,like,comment
                        map.put("type", StatusCode.REQUEST_MESSAGE_SCHOOL);
                        requestFragment.httpRequest(map, CommonUrl.getMessage);
                    }
                    break;
                    case 1: {
                        Map map = new HashMap();
                        map.put("phone", phone);
                        map.put("sortby", "comment");     //tiome,like,comment
                        map.put("type", StatusCode.REQUEST_MESSAGE_SCHOOL);
                        requestFragment.httpRequest(map, CommonUrl.getMessage);
                    }
                    break;
                    case 2: {
                        Map map = new HashMap();
                        map.put("phone", phone);
                        map.put("sortby", "like");     //tiome,like,comment
                        map.put("type", StatusCode.REQUEST_MESSAGE_SCHOOL);
                        requestFragment.httpRequest(map, CommonUrl.getMessage);
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        return view;
    }
    private void initInfo() {
        messageModel= new ArrayList<MessageModel>();
        Map map = new HashMap();
        map.put("phone",phone);
        map.put("sortby","time");     //tiome,like,comment 默认时间
        map.put("type", StatusCode.REQUEST_MESSAGE_SCHOOL);
        requestFragment.httpRequest(map, CommonUrl.getMessage);
    }

    @Override
    public void onResume(){
        super.onResume();
        initInfo();
    }

    private List<Map<String,Object>> getData() {
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<messageModel.size();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("isLike",messageModel.get(i).isliked());
            map.put("id",messageModel.get(i).getAcid());
            map.put("title",messageModel.get(i).getActitle());
            map.put("content",messageModel.get(i).getAccontent());
            map.put("remarkNum",messageModel.get(i).getAccommentN());
            map.put("starNum",messageModel.get(i).getAclikeN());
            map.put("author",messageModel.get(i).getAcsponsorname());
            map.put("time",messageModel.get(i).getAcsponsT());
            map.put("userimg",messageModel.get(i).getAcsponsorimg());
            map.put("image",messageModel.get(i).getAcimgurl());
            map.put("niming",messageModel.get(i).getNiming());
            list.add(map);
        }
        return list;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        //查询消息的返回
        if (requestUrl.equals(CommonUrl.getMessage)) {
            messageModel= new ArrayList<MessageModel>();
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_MESSAGE_SCHOOL_SUCCESS) {
                JSONArray jsonArray = object.getJSONArray("contents");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dongTai = jsonArray.getJSONObject(i);
                    MessageModel itemModel = new MessageModel();
                    itemModel.setAccommentN((int)dongTai.get("AccommentN"));
                    itemModel.setAccontent((String) dongTai.get("Accontent"));
                    itemModel.setAcid((int) dongTai.get("Acid"));
                    itemModel.setAclikeN((int) dongTai.get("AclikeN"));
                    itemModel.setAcsponsorid((int)dongTai.get("Acsponsorid"));
                    itemModel.setAcsponsorname((String)dongTai.get("Acsponsorname"));
                    itemModel.setAcsponsT((String)dongTai.get("AcsponsT"));
                    itemModel.setActitle((String)dongTai.get("Actitle"));
                    itemModel.setIsliked((int) dongTai.get("Acisliked"));
                    itemModel.setAcsponsorimg((String)dongTai.get("Acsponsorimg"));
                    itemModel.setAcimgurl((String)dongTai.get("Acimgurl"));
                    itemModel.setNiming((int)dongTai.get("niming"));
                    messageModel.add(itemModel);
                }
                message.what = StatusCode.REQUEST_MESSAGE_SCHOOL_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
                message.what = StatusCode.STATUS_ERROR;
                handler.sendMessage(message);
                return;
            }
        }
        //点赞请求的回复
        if(requestUrl.equals(CommonUrl.star)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_ISSTAR) {                   //查询：赞过
                message.what = StatusCode.REQUEST_ISSTAR;
                handler.sendMessage(message);
                return;
            }else if(code == StatusCode.REQUEST_NOTSTAR){             //查询：没赞过
                message.what = StatusCode.REQUEST_NOTSTAR;
                handler.sendMessage(message);
                return;
            }else if(code == StatusCode.REQUEST_STAR_SUCCESS){       //进行点赞
                this.onResume();
                return;
            }else if(code == StatusCode.REQUEST_UNSTAR_SUCCESS){    //取消点赞
                this.onResume();
                return;
            }
            else{
                message.what = StatusCode.STATUS_ERROR;
                handler.sendMessage(message);
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //listview中点赞和评论按钮点击的相应
    @Override
    public void onClick(View item, View widget, int position, int which,int id) {
        tempAcid=id;
        switch (which) {
            case R.id.star:                 //点赞或者取消
                if(!(phone==null)) {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_ASK_ISSTAR);
                    map.put("acid", id);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }else
                    Toast.makeText(this.getActivity(), "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                break;
            case R.id.remark:              //跳转评论界面
                if(!(phone==null)) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    Bundle data = new Bundle();
                    data.putString("phone", phone);
                    data.putInt("id",id);
                    intent.putExtras(data);
                    startActivity(intent);
                }else{
                    Toast.makeText(this.getActivity(), "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.authorpic:

                break;
            default:
                break;
        }
    }

    //服务器返回响应之后要对界面的操作由于不能干扰主UI线程，交给handler传递消息到主线程
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_MESSAGE_SCHOOL_SUCCESS: //成功返回所有动态
                    List<Map<String,Object>> list = getData();
                    listView.setAdapter(new messageListAdapter(tab1.this.getActivity(),list,tab1.this));
                    break;
                case StatusCode.REQUEST_ISSTAR:    //已经点赞，进行消赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_UNSTAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                    break;
                case StatusCode.REQUEST_NOTSTAR:  //没有赞过，进行点赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_STAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                   break;
                default: //用户身份认证失败
                    Toast.makeText(tab1.this.getActivity(), "网络错误", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
