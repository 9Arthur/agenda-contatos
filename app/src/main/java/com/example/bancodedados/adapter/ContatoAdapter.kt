package com.example.bancodedados.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bancodedados.AppDatabase
import com.example.bancodedados.AtualizarUsuario
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.databinding.ContatoItemBinding
import com.example.bancodedados.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContatoAdapter(
    private val context: Context,
    private val listaUsuarios: MutableList<Usuario>
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val usuario = listaUsuarios[position]

        holder.txtNome.text = usuario.nome
        holder.txtSobrenome.text = usuario.sobrenome
        holder.txtIdade.text = "Idade: ${usuario.idade}"
        holder.txtCelular.text = "Tel: ${formatCelular(usuario.celular)}"

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuario::class.java).apply {
                putExtra("nome", usuario.nome)
                putExtra("sobrenome", usuario.sobrenome)
                putExtra("idade", usuario.idade)
                putExtra("celular", usuario.celular)
                putExtra("uid", usuario.uid)
            }
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val usuarioDao: UsuarioDao = AppDatabase.getInstance(context).usuarioDao()
                usuarioDao.deletar(usuario.uid)
                listaUsuarios.remove(usuario)

                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = listaUsuarios.size

    inner class ContatoViewHolder(binding: ContatoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtSobrenome = binding.txtSobrenome
        val txtIdade = binding.txtIdade
        val txtCelular = binding.txtTelefone
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btdeletar
    }

    // Função para formatar o celular: 11987654321 → (11) 98765-4321
    private fun formatCelular(numero: String): String {
        return if (numero.length == 11) {
            val ddd = numero.substring(0, 2)
            val prefixo = numero.substring(2, 7)
            val sufixo = numero.substring(7)
            "($ddd) $prefixo-$sufixo"
        } else {
            numero
        }
    }
}
