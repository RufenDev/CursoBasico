package com.example.cursobasico.animeapp

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R
import com.example.cursobasico.databinding.ActivityAnimeBinding
import com.example.cursobasico.animeapp.adapter.AnimeAdapter
import com.example.cursobasico.animeapp.adapter.AnimeGridViewHolder
import com.example.cursobasico.animeapp.adapter.AnimeLinearViewHolder
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class AnimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimeBinding
    private lateinit var adapter: AnimeAdapter
    private lateinit var photoDialog: Dialog
    private var animationDuration: Long = 0
    private var toolbarTitle: String = ""
    private var toolbarSubtitle: String = ""

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if(activityResult.resultCode == RESULT_OK){

                val edited:Boolean? = activityResult.data?.extras?.getBoolean(EDITED_CHARACTER)
                edited?.let { state ->
                    if(state){
                        val index:Int? = activityResult.data?.extras?.getInt(EDITED_INDEX)
                        index?.let { position ->
                            adapter.notifyItemChanged(position)
                        }

                    } else {
                        adapter.notifyItemInserted(CharacterList.list.lastIndex)
                    }
                }

            } else if (activityResult.resultCode == RESULT_CANCELED) {
                val unknownCharacter: Boolean =
                    activityResult.data?.extras?.getBoolean(UNKNOWN_CHARACTER) ?: false
                if (unknownCharacter) unknownDialog()
            }

            if (closeDialog && ::photoDialog.isInitialized) {
                closeDialog = false
                photoDialog.hide()
            }

            emptyActivity()
             adapter.notifyDataSetChanged()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initListeners()
        initUI()
    }

    private fun initUI() {
        animationDuration = resources.getInteger(R.integer.animation_duration).toLong()

        toolbarTitle = getString(R.string.anime_title)
        toolbarSubtitle = getString(R.string.anime_subtitle)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = toolbarTitle
        supportActionBar?.subtitle = toolbarSubtitle

        binding.rbGridDisplay.isChecked = layoutDisplay == TYPE_GRID

        binding.collapsingToolbar.isTitleEnabled = true
        binding.collapsingToolbar.title = toolbarTitle
        setSelectedState(CharacterList.list.filter { it.isSelected }.size)
    }

    private fun initRecyclerView() {
        adapter = AnimeAdapter(charactersList = CharacterList.list,
            onPhotoClickListener = { position, image ->
                openPhoto(
                    position, image
                )
            },
            onItemSelected = { position, state ->
                onItemSelected(position, state)
            },
            moreInformation = { moreInformation(it) })
        binding.rcvAnimeProtanist.layoutManager =
            if (layoutDisplay == TYPE_LINEAR) LinearLayoutManager(this)
            else GridLayoutManager(this, 3)
        binding.rcvAnimeProtanist.adapter = adapter
    }

    private fun initListeners() {
        binding.rbLinearDisplay.setOnClickListener {
            if(layoutDisplay != TYPE_LINEAR){
                changeDisplayLayout(true)
            }
        }
        binding.rbGridDisplay.setOnClickListener {
            if(layoutDisplay != TYPE_GRID){
                changeDisplayLayout(false)
            }
        }

        val layoutParams = binding.toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        var showCollapseTitle = true
        var scrollRange = -1
        binding.appbar.addOnOffsetChangedListener { _, verticalOffset ->

            if (scrollRange == -1) {
                scrollRange = binding.appbar.totalScrollRange
            }

            val isAppbarTotalCollapsed = scrollRange + verticalOffset == 0

            if (isAppbarTotalCollapsed) {
                // Cuando el CollapsingToolbarLayout está completamente colapsado
                binding.collapsingToolbar.isTitleEnabled = false

                layoutParams.gravity = Gravity.BOTTOM // el valor deseado
                binding.toolbar.layoutParams = layoutParams

                showCollapseTitle = true

            } else if (showCollapseTitle) {
                // Cuando se expande el CollapsingToolbarLayout
                binding.collapsingToolbar.isTitleEnabled = true

                layoutParams.gravity = Gravity.TOP // el valor deseado
                binding.toolbar.layoutParams = layoutParams

                showCollapseTitle = false
            }
        }

        binding.bottomSelectedMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btnMenuEdit -> {
                    navigateTo<AddCharacterActivity>()
                    true
                }
                R.id.btnMenuDelete -> {
                    confirmDelete()
                    true
                }
                R.id.btnMenuInfo -> {
                    navigateTo<CharacterDetailsActivity>()
                    true
                }
                else -> false
            }
        }

        binding.fabAddCharacter.setOnClickListener {
            val intent = Intent(this, AddCharacterActivity::class.java)
            activityResult.launch(intent)
        }

    }

    private inline fun <reified T : Activity> navigateTo(position: Int? = null) {
        if (position != null) {
            val intent = Intent(this, T::class.java)
            intent.putExtra(SEND_CHARACTER, position)
            activityResult.launch(intent)

        } else {
            val selectedCharacter: List<AnimeCharacter> =
                CharacterList.list.filter { it.isSelected }
            if (selectedCharacter.size == 1) {

                val intent = Intent(this, T::class.java)
                intent.putExtra(SEND_CHARACTER, CharacterList.list.indexOf(selectedCharacter[0]))
                activityResult.launch(intent)

            } else {
                Toast.makeText(
                    this, getString(R.string.error_unselected), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeDisplayLayout(linearDisplay: Boolean) {
        isSelectedState = false
        toggleMenu()

        CharacterList.list.forEach { it.isSelected = false }
        layoutDisplay = if (linearDisplay) {
            binding.rcvAnimeProtanist.layoutManager = LinearLayoutManager(this)
            TYPE_LINEAR

        } else {
            binding.rcvAnimeProtanist.layoutManager = GridLayoutManager(this, 3)
            TYPE_GRID
        }
    }

    private fun confirmDelete(position: Int? = null) {
        if (position != null) {

            val msj = getString(R.string.confirm_delete_one)
            val builder = AlertDialog.Builder(this)
            builder.setMessage(msj)

            builder.setPositiveButton(getString(R.string.acept)) { _, _ ->
                deleteCharacter(listOf(position))
                isDialogOpen = false
                closeDialog = true
                photoDialog.hide()
            }
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        } else {
            val indexList: List<Int> = CharacterList.list
                .mapNotNull {
                if (it.isSelected) CharacterList.list.indexOf(it)
                else null
            }.reversed()

            Log.i("Remove_characters", "IndexList = ${indexList.joinToString(" | ")}")

            if (indexList.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_unselected), Toast.LENGTH_SHORT)
                    .show()

            } else {
                val msj = if (indexList.size == 1) {
                    getString(R.string.confirm_delete_one)
                } else {
                    getString(R.string.confirm_delete_many, indexList.size.toString())
                }

                val builder = AlertDialog.Builder(this)
                builder.setMessage(msj)
                builder.setPositiveButton(getString(R.string.acept)) { _, _ ->
                    deleteCharacter(indexList)
                }
                builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    Toast.makeText(this, "Cancelar", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun deleteCharacter(indexList: List<Int>) {
        indexList.forEach {
            Log.i("Remove_characters", "RemovedAt and NotifyAt = $it")
            CharacterList.list.removeAt(it)
            adapter.notifyItemRemoved(it)
        }

        emptyActivity()

        val msj: String = getString(
            if (indexList.size == 1) {
                R.string.character_removed
            } else {
                R.string.characters_removed
            }
        )
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()

    }

    private fun openPhoto(position: Int, photo: Bitmap) {
        if (!isDialogOpen) {
            isDialogOpen = true

            photoDialog = Dialog(this)
            photoDialog.setContentView(R.layout.dialog_character_photo)

            val tvName: AppCompatTextView = photoDialog.findViewById(R.id.tvCharacterPhotoName)
            tvName.text = CharacterList.list[position].name

            val imgPhoto: AppCompatImageView = photoDialog.findViewById(R.id.imgCharacterPhoto)
            imgPhoto.setImageBitmap(photo)

            val bottomMenu: BottomNavigationView =
                photoDialog.findViewById(R.id.bottomCharacterMenu)
            bottomMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.btnDialogBack -> {
                        isDialogOpen = false
                        closeDialog = true
                        photoDialog.hide()
                        true
                    }
                    R.id.btnDialogEdit -> {
                        navigateTo<AddCharacterActivity>(position)
                        closeDialog = true
                        isDialogOpen = false
                        true
                    }
                    R.id.btnDialogDelete -> {
                        confirmDelete(position)
                        true
                    }
                    R.id.btnDialogInfo -> {
                        navigateTo<CharacterDetailsActivity>(position)
                        closeDialog = true
                        isDialogOpen = false
                        true
                    }
                    else -> false
                }
            }

            photoDialog.setOnDismissListener {
                photoDialog.hide()
                isDialogOpen = false
            }
            photoDialog.show()
        }
    }

    private fun moreInformation(position: Int) {
        navigateTo<CharacterDetailsActivity>(position)
    }

    private fun onItemSelected(position: Int, state: Boolean) {
        CharacterList.list[position].isSelected = state
        setSelectedState(CharacterList.list.count { it.isSelected })
    }

    private fun setSelectedState(selectedCount: Int) {
        isSelectedState = selectedCount > 0
        if (isSelectedState) {
            supportActionBar?.title = selectedCount.toString()
            supportActionBar?.subtitle = "Seleccionado(s)"
            binding.collapsingToolbar.title = "$selectedCount Seleccionado(s)"

            binding.bottomSelectedMenu.menu.findItem(R.id.btnMenuEdit).isVisible =
                selectedCount == 1

            binding.bottomSelectedMenu.menu.findItem(R.id.btnMenuInfo).isVisible =
                selectedCount == 1
        }

        toggleMenu()
    }

    private fun toggleMenu() {
        val showMenu = binding.bottomSelectedMenu.visibility == View.INVISIBLE

        if (isSelectedState && showMenu) {

            binding.bottomSelectedMenu.visibility = View.VISIBLE
            val menuHeight = binding.bottomSelectedMenu.height.toFloat()

            val show = AnimationUtils.loadAnimation(this, R.anim.show_menu)
            show.setAnimationListener(object : AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    val scrollHeight: Int = binding.nestedScroll.height - menuHeight.toInt()
                    binding.nestedScroll.layoutParams.height = scrollHeight
                }

                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
            })
            binding.bottomSelectedMenu.startAnimation(show)

            binding.fabAddCharacter.animate().translationY(-menuHeight)
                .setDuration(animationDuration).start()

        } else if (!isSelectedState && !showMenu) {

            val hideMenu = AnimationUtils.loadAnimation(this, R.anim.hide_menu)
            hideMenu.setAnimationListener(object : AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    binding.bottomSelectedMenu.visibility = View.INVISIBLE
                    binding.nestedScroll.layoutParams.height = LayoutParams.MATCH_PARENT
                }

                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
            })

            binding.bottomSelectedMenu.startAnimation(hideMenu)

            binding.fabAddCharacter.animate().translationY(0f).setDuration(animationDuration)
                .start()

            supportActionBar?.title = toolbarTitle
            supportActionBar?.subtitle = toolbarSubtitle
            binding.collapsingToolbar.title = toolbarTitle
        }
    }

    private fun unknownDialog() {
        val msj = getString(R.string.unknown_character_msg)
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msj)

        builder.setPositiveButton(getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun emptyActivity() {
        isSelectedState = false
        toggleMenu()

        CharacterList.list.forEach {
            if (it.isSelected) {
                val index: Int = CharacterList.list.indexOf(it)
                val child: View = binding.rcvAnimeProtanist.getChildAt(index)
                val viewHolder: RecyclerView.ViewHolder =
                    binding.rcvAnimeProtanist.getChildViewHolder(child)

                if (viewHolder is AnimeGridViewHolder) {
                    viewHolder.binding.viewGridShadow.visibility = View.GONE

                } else if (viewHolder is AnimeLinearViewHolder) {
                    viewHolder.selectedBg(false)
                }

                it.isSelected = false
            }
        }
    }

    override fun onBackPressed() {
        if (isSelectedState) {
            emptyActivity()

        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            emptyActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TYPE_LINEAR = 1
        const val TYPE_GRID = 2
        const val SEND_CHARACTER = "SEND_CHARACTER"
        const val EDITED_CHARACTER = "EDITED_CHARACTER"
        const val EDITED_INDEX = "EDITED_INDEX"
        const val UNKNOWN_CHARACTER = "UNKNOWN"

        var layoutDisplay: Int = TYPE_LINEAR
        var isDialogOpen: Boolean = false
        var closeDialog: Boolean = false
        var isSelectedState: Boolean = false
    }
}