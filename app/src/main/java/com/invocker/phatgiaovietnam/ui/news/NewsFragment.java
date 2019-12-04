package com.invocker.phatgiaovietnam.ui.news;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.invocker.phatgiaovietnam.R;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private WebView myWebView;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        myWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_news, container, false);
        myWebView = root.findViewById(R.id.new_web);
        myWebView.loadUrl(newsViewModel.getUrlWeb());
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.getSettings().setBuiltInZoomControls(false);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                newsViewModel.setUrlWeb(url);
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
            }
        });
        myWebView.loadUrl("https://phatgiao.org.vn");

        //final TextView textView = root.findViewById(R.id.text_dashboard);
        newsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //        textView.setText(s);
            }
        });
        return root;
    }


}