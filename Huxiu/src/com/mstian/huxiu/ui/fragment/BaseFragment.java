
package com.mstian.huxiu.ui.fragment;

import com.android.volley.Request;
import com.mstian.huxiu.data.RequestManager;

import android.support.v4.app.Fragment;

/**
 * Created by Issac on 7/18/13.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }
}
