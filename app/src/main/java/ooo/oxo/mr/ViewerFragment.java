/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import ooo.oxo.mr.databinding.ViewerFragmentBinding;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.util.PostponedTransitionTrigger;
import ooo.oxo.mr.widget.RxBindingFragment;

public class ViewerFragment extends RxBindingFragment<ViewerFragmentBinding> {

    private static final String TAG = "ViewerFragment";

    private PostponedTransitionTrigger transitionTrigger;

    public ViewerFragment() {
    }

    @Nullable
    @Override
    public ViewerFragmentBinding onCreateBinding(LayoutInflater inflater,
                                                 @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        return ViewerFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Image image = intent.getParcelableExtra("image");

        ViewCompat.setTransitionName(binding.image,
                String.format("%s.image", image.id));

        binding.image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        binding.image.setSingleTapListener(((ViewerActivity) getActivity())::toggleFade);
        binding.image.setDoubleTapListener(((ViewerActivity) getActivity())::fadeOut);

        transitionTrigger = new PostponedTransitionTrigger((ViewerActivity) getActivity());

        binding.setImage(image);
        binding.setThumbnail(intent.getStringExtra("thumbnail"));
        binding.setListener(transitionTrigger);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        transitionTrigger.cancel();
    }

}
