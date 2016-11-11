package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.net.NetDao;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.OkHttpUtils;
import cn.ucai.superwechat.utils.ResultUtils;

public class FriendProfileActivity extends BaseActivity {

    String username = null;
    User user = null;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_Avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_userName)
    TextView tvUsername;
    @Bind(R.id.btn_AddContact)
    Button btnAddContact;
    @Bind(R.id.btn_Message)
    Button btnMessage;
    @Bind(R.id.btn_Video_Message)
    Button btnVideoMessage;
    boolean isFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frient_profile);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra(I.User.USER_NAME);
        L.e("user: " + username);
        if (username == null) {
            MFGT.finish(this);
            return;
        }
        initView();
        user = SuperWeChatHelper.getInstance().getAppContactList().get(username);

        if (user == null){
            isFriend = false;
        }else{
            L.e(".....................user="+user);
//            setUserInfo();
            isFriend = true;

        }
        isFriend(isFriend);
        syncUserInfo();
    }

    private void syncFail(){
        MFGT.finish(this);
        return;
    }
    private void syncUserInfo() {
        NetDao.syncUserInfo(this, username, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null){
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null && result.isRetMsg()){
                        user = (User) result.getRetData();
                        if (user != null){
                            setUserInfo();
                            if (isFriend){
                                SuperWeChatHelper.getInstance().saveAppContact(user);
                            }
                        }else{
                            syncFail();
                        }
                    }else{
                        syncFail();
                    }
                }else{
                    syncFail();
                }
            }

            @Override
            public void onError(String error) {
                syncFail();
            }
        });
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.userinfo_txt_profile));
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), ivAvatar);
        EaseUserUtils.setAppUserNick(user.getMUserNick(), tvNickname);
        EaseUserUtils.setAppUserNameWithNo(user.getMUserName(), tvUsername);
    }

    public void isFriend(boolean isFriend) {
        if (isFriend) {
            btnMessage.setVisibility(View.VISIBLE);
            btnVideoMessage.setVisibility(View.VISIBLE);
        } else {
            btnAddContact.setVisibility(View.VISIBLE);
        }
    }

        @OnClick({R.id.ivBack,R.id.btn_AddContact, R.id.btn_Message, R.id.btn_Video_Message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btn_AddContact:
                MFGT.gotoAddFriendMsg(this,user.getMUserName());
                break;
            case R.id.btn_Message:
                MFGT.gotoChat(this,user.getMUserName());
                break;
            case R.id.btn_Video_Message:
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, VoiceCallActivity.class).putExtra("username", user.getMUserName())
                            .putExtra("isComingCall", false));
                    // voiceCallBtn.setEnabled(false);
                }
                break;
        }
    }
}
