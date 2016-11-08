package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;

public class FriendProfileActivity extends BaseActivity {

    User user;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frient_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        L.e("user: " + user);
        if (user == null) {
            MFGT.finish(this);
            return;
        }
        initView();
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.userinfo_txt_profile));
        setUserInfo();
        isFriend();

    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), ivAvatar);
        EaseUserUtils.setAppUserNick(user.getMUserNick(), tvNickname);
        EaseUserUtils.setAppUserNameWithNo(user.getMUserName(), tvUsername);
    }

    public void isFriend() {
        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
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
                break;
        }
    }
}
