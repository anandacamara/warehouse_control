package org.jw.warehousecontrol.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.ActivityWarehouseControlBinding
import org.jw.warehousecontrol.framework.createModule
import org.jw.warehousecontrol.presentation.model.delegate.BaseDelegate
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

/**
 * @author Ananda Camara
 */
class WarehouseControlActivity : AppCompatActivity(), BaseDelegate {
    private lateinit var view: ActivityWarehouseControlBinding

    private val navHost by lazy { findNavController(R.id.container) }

    init {
        startKoin {
            allowOverride(true)
            loadKoinModules(module = createModule(this@WarehouseControlActivity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityWarehouseControlBinding.inflate(layoutInflater)

        setContentView(view.root)
        setupToolbar()
    }

    private fun setupToolbar() = with(view.toolbar) {
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        setSupportActionBar(this)
        setupWithNavController(navHost, appBarConfiguration)
    }

    override fun showLoad() {
        view.loadBackground.visibility = View.VISIBLE
        view.loadIndicator.visibility = View.VISIBLE
        view.root.isClickable = false
    }

    override fun hideLoad() {
        view.loadBackground.visibility = View.GONE
        view.loadIndicator.visibility = View.GONE
        view.root.isClickable = true
    }
}