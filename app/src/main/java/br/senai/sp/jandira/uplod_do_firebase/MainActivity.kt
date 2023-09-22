package br.senai.sp.jandira.uplod_do_firebase

import android.app.ActivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import br.senai.sp.jandira.uplod_do_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

//Delaração do atributos

//Manipulacao dos elementos graficos dp material design
private lateinit var binding: ActivityMainBinding

//Permite a manipulacao do clound storage
private lateinit var storageRef: StorageReference

//Permite a manipulacao do banco de dados nosql
private lateinit var firebaseStore: FirebaseFirestore

//Uri - Permite a manipulacao de arquivos atraves do seu endereco
private var imageUri: Uri? = null


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiVars()
        registerclickEvents()

    }

    //FUNÇÃO DE INICIALIZAÇÃO DOS RECURSOS DO FIREBASE
    private fun initiVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("images")

        firebaseStore = FirebaseFirestore.getInstance()
    }

    //FUNÇÃO PARA O LANÇADOR DE RECUPERAÇÃO DE IMAGENS DA GALERIA
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {

        imageUri = it
        binding.imageView.setImageURI(it)

    }

    //FUNÇÃO DE TRATAMENTO DE CLICK
    private fun registerclickEvents() {

        //TRATA O EVENTO DE CLICK DO COMONENTE IMAGEVIEW
        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }

        binding.uploadBtn.setOnClickListener{
            uplodImage( )
        }


    }




    //FUNÇÃO DE UPLOD
    private fun uplodImage() {

        binding.progressBar.visibility = View.VISIBLE

        //DEFINA UM NOME UNICO PARA IMAGEM COM USO DE UM VALOR TIMESTAMP
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        //EXECUTA O PROCESSO DE UPLOAD DA IMAGEM
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener{
                task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "UPLOAD CONCLUIDO", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "ERRO AO REALIZAR O UPLOAD", Toast.LENGTH_LONG).show()
                        }
            }
        }

        binding.progressBar.visibility = View.GONE
    }

}