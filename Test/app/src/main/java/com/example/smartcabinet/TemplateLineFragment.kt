package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartcabinet.util.DBManager
import com.example.smartcabinet.util.ReagentTemplate
import kotlinx.android.synthetic.main.fragment_template_line.*

/**
 * A simple [Fragment] subclass.
 */
class TemplateLineFragment : Fragment() {
    private var reagentTemplate: ReagentTemplate?=null
    private var dbManager: DBManager? = null

    var activityCallback: TemplateLineFragment.templateLineListen? = null
    interface  templateLineListen {
        fun templateLineButtonClick(text: String)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_template_line, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dbManager= DBManager(context.applicationContext)
        if(arguments!=null)
        {
            addTemplateLine()
        }
        templateLine_del.setOnClickListener {
            //单条删除试剂
            var unit:String? = null
            var purity:String? = null
            var type=2
            if(templateLine_state.text.toString()=="固体") {
                unit = "g"
                type = 1
            }
            else
                unit="ml"
            purity = templateLine_purity.text.toString().substring(0,templateLine_purity.text.toString().length-1)
           dbManager?.deletReagentTemplateByInfo("",templateLine_name.text.toString(),templateLine_anotherName.text.toString(),"",
           "",type, purity,templateLine_volume.text.toString(),templateLine__manufator.text.toString() ,
                   templateLine__code.text.toString(),unit,templateLine_density.text.toString())
            templateLineClicked("delet")
        }
    }

    fun addTemplateLine(){
        val order = arguments.getInt("order")
        val arrListReagentTemplate = dbManager?.reagentTemplate
        reagentTemplate = arrListReagentTemplate?.get(order)
        templateLine_name.text=reagentTemplate?.reagentName
        templateLine_anotherName.text=reagentTemplate?.reagentAlias
        templateLine_volume.text=reagentTemplate?.reagentSize
        templateLine__code.text=reagentTemplate?.reagentGoodsID
        templateLine__manufator.text=reagentTemplate?.reagentCreater
        templateLine_purity.text=reagentTemplate?.reagentPurity+"%"
        templateLine_density.text=reagentTemplate?.reagentDensity
        if(reagentTemplate?.reagentUnit=="g")
            templateLine_state.text="固体"
        else
            templateLine_state.text="液体"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as TemplateLineFragment.templateLineListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement AdminFragmentListener")
        }
    }

    private fun templateLineClicked(text: String) {
        activityCallback?.templateLineButtonClick(text)
    }
}// Required empty public constructor
