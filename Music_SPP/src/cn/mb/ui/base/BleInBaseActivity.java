package cn.mb.ui.base;

import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public abstract class BleInBaseActivity extends InBaseActivity {

    protected SppManager bleManager;
    protected SppConnectHelper helper;

    @Override
    protected void onResume() {
        super.onResume();
        if (helper == null) {
            helper = new SppConnectHelper("", null);
        }
        if (bleManager == null) {
            bleManager = helper.getInstance();
        }
    }
}
