/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015-2016  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ooo.oxo.mr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ooo.oxo.mr.databinding.AboutActivityBinding;

public class AboutActivity extends AppCompatActivity {

    private ClipboardManager cm;

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AboutActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.about_activity);

        binding.toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        final String template = getString(R.string.about_page)
                .replace("{{fork_me_on_github}}", getString(R.string.fork_me_on_github))
                .replace("{{images_from}}", getString(R.string.images_from))
                .replace("{{support_us}}", getString(R.string.support_us))
                .replace("{{support_us_text}}", getString(R.string.support_us_text))
                .replace("{{libraries_used}}", getString(R.string.libraries_used));

        binding.chrome.setWebViewClient(new AboutClient());
        binding.chrome.loadData(template, "text/html; charset=utf-8", null);

        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    private void copy(String text) {
        cm.setPrimaryClip(ClipData.newPlainText(text, text));
        Toast.makeText(this, getString(R.string.about_copied, text), Toast.LENGTH_SHORT).show();
    }

    private void open(Uri uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    private class AboutClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            if ("copy".equals(uri.getScheme())) {
                copy(uri.getSchemeSpecificPart());
                return true;
            } else {
                open(uri);
                return true;
            }
        }

    }

}
