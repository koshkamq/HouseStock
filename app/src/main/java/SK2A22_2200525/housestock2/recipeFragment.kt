package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentRecipeBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.realm.Realm

class recipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //floatボタン非表示
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)

        //コレで決まりを押すと
        binding.koredekimaributton.setOnClickListener {

            //チェックボックスで分岐する
            if(binding.omuraisucheckBox.isChecked == true){

                //オムライスの画面へ遷移
                findNavController().navigate(R.id.action_recipeFragment_to_omuraisuFragment)

            }else if(binding.kareraisucheckBox.isChecked == true){

                findNavController().navigate(R.id.action_recipeFragment_to_kareraisuFragment)

            }else if(binding.nikuzyagacheckBox.isChecked == true){

                findNavController().navigate(R.id.action_recipeFragment_to_nikuzyagaFragment)

            }else if(binding.mentaicheckBox.isChecked == true){

                findNavController().navigate(R.id.action_recipeFragment_to_mentaiFragment)

            }else if(binding.hoikorocheckBox.isChecked == true){

                findNavController().navigate(R.id.action_recipeFragment_to_hoikoroFragment)
            }
        }

        //チェックボックスが押されたら他のボタンはオフにする
        binding.omuraisucheckBox.setOnClickListener {
            binding.kareraisucheckBox.isChecked = false
            binding.nikuzyagacheckBox.isChecked = false
            binding.mentaicheckBox.isChecked = false
            binding.hoikorocheckBox.isChecked = false
        }

        binding.kareraisucheckBox.setOnClickListener {
            binding.omuraisucheckBox.isChecked = false
            binding.nikuzyagacheckBox.isChecked = false
            binding.mentaicheckBox.isChecked = false
            binding.hoikorocheckBox.isChecked = false
        }

        binding.nikuzyagacheckBox.setOnClickListener {
            binding.omuraisucheckBox.isChecked = false
            binding.kareraisucheckBox.isChecked = false
            binding.mentaicheckBox.isChecked = false
            binding.hoikorocheckBox.isChecked = false
        }

        binding.mentaicheckBox.setOnClickListener {
            binding.omuraisucheckBox.isChecked = false
            binding.kareraisucheckBox.isChecked = false
            binding.nikuzyagacheckBox.isChecked = false
            binding.hoikorocheckBox.isChecked = false
        }

        binding.hoikorocheckBox.setOnClickListener {
            binding.omuraisucheckBox.isChecked = false
            binding.kareraisucheckBox.isChecked = false
            binding.nikuzyagacheckBox.isChecked = false
            binding.mentaicheckBox.isChecked = false
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()
    }
}