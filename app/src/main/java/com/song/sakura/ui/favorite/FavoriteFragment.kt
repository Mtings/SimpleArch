package com.song.sakura.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : IBaseFragment<IBaseViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flashGroup.setString("汉字WZ^89")
        flashGroup.startAnim()

        button.setOnClickListener {
            flashGroup.startAnim()
        }
    }
}