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
 * {@link tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab2 extends Fragment implements ListItemClickHelp,NetworkCallbackInterface.NetRequestIterface {
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
    public tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static tab2 newInstance(String param1, String param2) {
        tab2 fragment = new tab2();
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
        View view = inflater.inflate(R.layout.fragment_tab2,null);//注意不要指定父视图
        listView=(ListView)view.findViewById(R.id.list2);
        return view;
    }

    private void initInfo() {
        messageModel= new ArrayList<MessageModel>();
        Map map = new HashMap();
        map.put("sortBy","time");     //tiome,like,comment
        map.put("type", StatusCode.REQUEST_MESSAGE_EMOTION);
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

            if (code == StatusCode.REQUEST_MESSAGE_EMOTION_SUCCESS) {
                Gson gson = new Gson();
                messageModel= gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), new TypeToken<List<MessageModel>>(){}.getType());
                message.what = StatusCode.REQUEST_MESSAGE_EMOTION_SUCCESS;
                handler.sendMessage(message);
                return;
            } else {
//                Toast.makeText(this.getActivity(), "未知错误", Toast.LENGTH_LONG).show();
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
                Map map = new HashMap();
                map.put("type",StatusCode.REQUEST_STAR);
                map.put("acid",id);
                map.put("phone",mainActivity.phone);
                requestFragment.httpRequest(map,CommonUrl.star);
                break;
            case R.id.remark:
                Intent intent = new Intent(getActivity(),CommentActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_MESSAGE_EMOTION_SUCCESS://成功返回所有动态
                {
                    List<Map<String,Object>> list = getData();
                    listView.setAdapter(new messageListAdapter(tab2.this.getActivity(),list,tab2.this));
                    break;
                }
                default: //用户身份认证失败
                {

                    break;
                }
            }
        }
    };
}
