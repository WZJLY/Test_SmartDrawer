package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import com.example.smartcabinet.util.SC_Const
import kotlinx.android.synthetic.main.fragment_edit_template.*

/**
 * A simple [Fragment] subclass.
 */
class EditTemplateFragment : Fragment() {
    var activityCallback: EditTemplateFragment.editTemplateListen? = null
    private var reagentTemplate: ReagentTemplate?=null
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    interface editTemplateListen {
        fun editTemplateButtonClick(text: String)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.title = "试剂模板"
        return inflater!!.inflate(R.layout.fragment_edit_template, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        scApp = context.applicationContext as SCApp
        dbManager = DBManager(context)
        val userAccount =  scApp?.userInfo
        when(userAccount?.getUserPower()){
            SC_Const.NORMAL -> {
                btn_clean.visibility = View.GONE
                btn_import.visibility = View.GONE
            }
        }
        val arrListReagentTemplate = dbManager?.reagentTemplate
        val sum = arrListReagentTemplate!!.size
        if(sum>0)
        {
            for (i in 1..sum) {
                reagentTemplate = arrListReagentTemplate?.get(i - 1)
                val fragment = childFragmentManager.beginTransaction()
                val templateLineFragment = TemplateLineFragment()
                val args = Bundle()
                args.putInt("order",i-1)
                templateLineFragment.arguments = args
                fragment.add(R.id.Linear_editTemplate,templateLineFragment)
                fragment.commit()
            }
        }
        else
            Toast.makeText(context,"没有试剂模板",Toast.LENGTH_SHORT).show()
        btn_import.setOnClickListener{

            editTemplateClicked("btn_import")
        }
        btn_clean.setOnClickListener{
            editTemplateClicked("btn_clean")

        }
        btn_single.setOnClickListener{
            editTemplateClicked("btn_single")
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as editTemplateListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }

    private fun editTemplateClicked(text: String) {
        activityCallback?.editTemplateButtonClick(text)
    }

}// Required empty public constructor
