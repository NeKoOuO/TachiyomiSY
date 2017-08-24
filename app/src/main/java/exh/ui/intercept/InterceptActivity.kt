package exh.ui.intercept

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.ui.base.activity.BaseActivity
import exh.GalleryAdder
import timber.log.Timber
import kotlin.concurrent.thread

//TODO :(
class InterceptActivity : BaseActivity() {

    private val galleryAdder = GalleryAdder()

    var finished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eh_activity_intercept)

        if(savedInstanceState == null)
            thread { setup() }
    }

    fun setup() {
        try {
            processLink()
        } catch(t: Throwable) {
            Timber.e(t, "Could not intercept link!")
            if(!finished)
                runOnUiThread {
                    MaterialDialog.Builder(this)
                            .title("Error")
                            .content("Could not load this gallery!")
                            .cancelable(true)
                            .canceledOnTouchOutside(true)
                            .cancelListener { onBackPressed() }
                            .positiveText("Ok")
                            .onPositive { _, _ -> onBackPressed() }
                            .dismissListener { onBackPressed() }
                            .show()
                }
        }
    }

    fun processLink() {
        if(Intent.ACTION_VIEW == intent.action) {
            val manga = galleryAdder.addGallery(intent.dataString)

            //TODO
//            if(!finished)
//                startActivity(MangaActivity.newIntent(this, manga, true))
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBackPressed() {
        if(!finished)
            runOnUiThread {
                super.onBackPressed()
            }
    }

    override fun onStop() {
        super.onStop()
        finished = true
    }
}
