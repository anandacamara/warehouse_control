package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jw.warehousecontrol.databinding.FragmentVolunteerRegisterBinding

/**
 * @author Ananda Camara
 */
internal class VolunteerRegisterFragment : Fragment() {
    private lateinit var view: FragmentVolunteerRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentVolunteerRegisterBinding.inflate(layoutInflater, container, false)
        return view.root
    }
}