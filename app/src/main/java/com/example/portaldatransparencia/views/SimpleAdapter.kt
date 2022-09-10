package com.example.portaldatransparencia.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.DadoDespesas
import java.text.DecimalFormat

class SimpleAdapterView(private val context: Context, private val note: DadoDespesas) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount() = 1

    override fun getItem(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false)

        val textIdDoc = view!!.findViewById<TextView>(R.id.text_id_doc)
        val textDateDoc = view.findViewById<TextView>(R.id.text_date_doc)
        val textValor = view.findViewById<TextView>(R.id.text_valor_doc)
        val textFornecedor = view.findViewById<TextView>(R.id.text_fornecedor_doc)
        val textCnpj = view.findViewById<TextView>(R.id.text_cnpj_doc)
        val imageDown = view.findViewById<ImageView>(R.id.image_download)
        val imageShare = view.findViewById<ImageView>(R.id.image_share)
        val imageDoc = view.findViewById<ImageView>(R.id.image_note)

        imageDown?.setOnClickListener {  }
        imageShare?.setOnClickListener {  }

        val dateDoc = note.dataDocumento.split("-")
        (dateDoc[2]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { textDateDoc.text = it }
        textIdDoc?.text = note.codDocumento.toString()
        val format = DecimalFormat("#.00")
        val formatTotal = format.format(note.valorDocumento)
        "R$ $formatTotal".also { textValor.text = it }
        textFornecedor.text = note.nomeFornecedor
        textCnpj.text = note.cnpjCpfFornecedor

        Glide.with(context)
            .load(note.urlDocumento)
            .into(imageDoc)

        return view
    }
}
