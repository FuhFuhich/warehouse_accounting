package com.example.warehouse_accounting.ui.help

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    private val helpItems = listOf(
        "Профиль" to "Здесь у вас находится информация о вас, которую вы можете изменить",
        "Товары" to "Здесь находятся ваши товары, которые присутствуют на том или ином складе. Вы можете удалять при зажатии кнопки, при нажатии вы сможете редактировать товар, при нажатии на кнопку справа снизу вы можете создать товар, который будет присутствовать на том или ином складе",
        "Поставщики" to "Здесь вы можете добавлять краткую информацию о ваших поставщиках. Вы можете удалять при зажатии кнопки, при нажатии вы сможете редактировать поставщика, при нажатии на кнопку справа снизу вы можете создать поставщика",
        "Покупатели" to "Здесь вы можете добавлять краткую информацию о ваших покупателях. Вы можете удалять при зажатии кнопки, при нажатии вы сможете редактировать покупателя, при нажатии на кнопку справа снизу вы можете создать покупателя",
        "Склады" to "Дает возможность создавать склады для последующего добавления товара. Вы можете удалять при зажатии кнопки, при нажатии вы сможете редактировать склад, при нажатии на кнопку справа снизу вы можете создать склад",
        "Нужна помощь?" to "Вы можете прислать сообщение разработчику на почту в случае возникновении ошибки или же предложить идеи по улучшению приложения. Чтобы прислать, нужно выбрать почту и отправить сообщение"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        helpItems.forEach { (title, description) ->
            val textView = LayoutInflater.from(requireContext())
                .inflate(android.R.layout.simple_list_item_1, binding.helpContainer, false) as TextView

            textView.text = title
            textView.textSize = 18f
            textView.setPadding(24, 24, 24, 24)
            textView.setTextColor(Color.WHITE)
            textView.setOnClickListener {
                showDescriptionDialog(title, description)
            }

            binding.helpContainer.addView(textView)
        }

        return root
    }

    private fun showDescriptionDialog(title: String, description: String) {
        val messageTextView = TextView(requireContext()).apply {
            text = description
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(40, 32, 40, 32)
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(title)
            .setView(messageTextView)
            .setPositiveButton("OK", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#222222")))
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
