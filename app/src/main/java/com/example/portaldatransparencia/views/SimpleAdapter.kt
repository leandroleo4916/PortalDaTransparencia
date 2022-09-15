package com.example.portaldatransparencia.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.dataclass.DadoDespesas
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.util.*

class SimpleAdapterView(private val context: Context, private val note: DadoDespesas): BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount() = 1

    override fun getItem(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false)

        val textIdDoc = view!!.findViewById<TextView>(R.id.text_id_doc)
        val textDateDoc = view.findViewById<TextView>(R.id.text_date_doc)
        val textValor = view.findViewById<TextView>(R.id.text_valor_doc)
        val textFornecedor = view.findViewById<TextView>(R.id.text_fornecedor_doc)
        val textCnpj = view.findViewById<TextView>(R.id.text_cnpj_doc)
        val textProof = view.findViewById<TextView>(R.id.text_proof_doc)
        val imageDown = view.findViewById<ImageView>(R.id.image_download)
        val imageShare = view.findViewById<ImageView>(R.id.image_share)
        val imageNote = view.findViewById<WebView>(R.id.image_note)

        imageDown?.setOnClickListener { it.id }
        imageShare?.setOnClickListener { it.id }

        val dateDoc = note.dataDocumento.split("-")
        (dateDoc[2]+"/"+dateDoc[1]+"/"+dateDoc[0]).also { textDateDoc.text = it }
        textIdDoc?.text = "Nota nº: "+note.codDocumento.toString()
        val format = DecimalFormat("#.00")
        val formatTotal = format.format(note.valorDocumento)
        "R$ $formatTotal".also { textValor.text = it }
        textFornecedor.text = note.nomeFornecedor
        textCnpj.text = "CNPJ: "+note.cnpjCpfFornecedor
        if (note.urlDocumento.isEmpty()) {
            textProof.text = "Comprovante não enviado"
            textProof.resources.getColor(R.color.red)
        }

        imageNote.loadUrl(note.urlDocumento)

        return view
    }

    private fun converterStringByBitmap(image: String): ByteArray {
        val base = Base64.getDecoder()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val ret = downPdf(note.urlDocumento)
                val to1 = ret.split("stream")
                val to2 = "stream"+to1[1]
                val image = converterStringByBitmap(to2)
            }
        }
        return base.decode(image)
    }

    private fun downPdf(urlString: String): String {

        val url = URL(urlString)
        val response = StringBuilder()
        val con = url.openConnection() as HttpURLConnection
        con.setRequestProperty("User-Agent", "Mozilla/5.0")

        val input = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        while (input.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        input.close()
        return response.toString()
    }
}
