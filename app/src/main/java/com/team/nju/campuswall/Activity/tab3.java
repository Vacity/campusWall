package com.team.nju.campuswall.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.nju.campuswall.Adapter.ListItemClickHelp;
import com.team.nju.campuswall.Adapter.messageListAdapter;
import com.team.nju.campuswall.Model.MessageModel;
import com.team.nju.campuswall.Network.NetworkCallbackInterface;
import com.team.nju.campuswall.Network.StatusCode;
import com.team.nju.campuswall.Network.netRequest;
import com.team.nju.campuswall.R;
import com.team.nju.campuswall.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab3 extends Fragment implements ListItemClickHelp,NetworkCallbackInterface.NetRequestIterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private netRequest requestFragment;
    private List<MessageModel> messageModel = new ArrayList<MessageModel>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView listView=null;
    private String phone = mainActivity.phone;
    int tempAcid;
    public tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab3.
     */
    // TODO: Rename and change types and number of parameters
    public static tab3 newInstance(String param1, String param2) {
        tab3 fragment = new tab3();
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
        View view = inflater.inflate(R.layout.fragment_tab3,null);//注意不要指定父视图
        listView=(ListView)view.findViewById(R.id.list3);
        return view;
    }

    private void initInfo() {
        messageModel= new ArrayList<MessageModel>();
        Map map = new HashMap();
        map.put("phone",phone);
        map.put("sortBy","time");     //tiome,like,comment
        map.put("type", StatusCode.REQUEST_MESSAGE_THING);
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
            map.put("id",messageModel.get(i).getAcid());
            map.put("title",messageModel.get(i).getActitle());
            map.put("content",messageModel.get(i).getAccontent());
            map.put("remarkNum",messageModel.get(i).getAccommentN());
            map.put("starNum",messageModel.get(i).getAclikeN());
            map.put("author",messageModel.get(i).getAcsponsorid());
            map.put("time",messageModel.get(i).getAcsponsT());
            //     map.put("image",R.drawable.XXXX);  可以加头像
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
        if (requestUrl.equals(CommonUrl.getMessage)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_MESSAGE_THING_SUCCESS) {
                Gson gson = new Gson();
                messageModel= gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), new TypeToken<List<MessageModel>>(){}.getType());
                message.what = StatusCode.REQUEST_MESSAGE_THING_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
                message.what = StatusCode.STATUS_ERROR;
                handler.sendMessage(message);
                return;
            }
        }
        //点赞请求的回复
        if (requestUrl.equals(CommonUrl.star)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_ISSTAR) {                   //查询：赞过
                message.what = StatusCode.REQUEST_ISSTAR;
                handler.sendMessage(message);
                return;
            } else if (code == StatusCode.REQUEST_NOTSTAR) {             //查询：没赞过
                message.what = StatusCode.REQUEST_NOTSTAR;
                handler.sendMessage(message);
                return;
            } else if (code == StatusCode.REQUEST_STAR_SUCCESS) {       //进行点赞
                this.onResume();
                return;
            } else if (code == StatusCode.REQUEST_UNSTAR_SUCCESS) {    //取消点赞
                this.onResume();
                return;
            } else {
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

    @Override
    public void onClick(View item, View widget, int position, int which,int id) {
        switch (which) {
            case R.id.star:
                if(!(phone==null)) {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_ASK_ISSTAR);
                    map.put("acid", id);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }else
                    Toast.makeText(this.getActivity(), "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                break;
            case R.id.remark:
                if(!(phone==null)) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    Bundle data = new Bundle();
                    data.putString("phone", phone);
                    data.putInt("id",id);
                    startActivity(intent);
                }else
                    Toast.makeText(this.getActivity(), "登陆后进行更多操作", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_MESSAGE_THING_SUCCESS://成功返回所有动态
                {
                    List<Map<String,Object>> list = getData();
                    listView.setAdapter(new messageListAdapter(tab3.this.getActivity(),list,tab3.this));
                    break;
                }
                case StatusCode.REQUEST_ISSTAR:    //已经点赞，进行消赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_UNSTAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                case StatusCode.REQUEST_NOTSTAR:  //没有赞过，进行点赞的操作
                {
                    Map map = new HashMap();
                    map.put("type", StatusCode.REQUEST_STAR);
                    map.put("acid", tempAcid);
                    map.put("phone", mainActivity.phone);
                    requestFragment.httpRequest(map, CommonUrl.star);
                }
                default: //用户身份认证失败
                {
                    Toast.makeText(tab3.this.getActivity(), "网络错误", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };
}
