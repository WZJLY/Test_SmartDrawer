package com.example.smartcabinet


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_reagent.*


/**
 * A simple [Fragment] subclass.
 */
class UserReagentFragment : Fragment() {

    var actionCallback: UserReagentFragment.userReagentListen ?= null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            actionCallback = context as userReagentListen
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString()
                    + " must implement userReagentListen")
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_reagent, container, false)
    }

    interface userReagentListen{
        fun userReagentButtonClick(text: String)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_user_take.setOnClickListener {
            buttonClicked("userTake")
        }

        btn_user_return.setOnClickListener {
            buttonClicked("userReturn")
        }

        btn_user_back.setOnClickListener {
            buttonClicked("userBack")
        }
    }

    private fun buttonClicked(text: String) {
        actionCallback?.userReagentButtonClick(text)
    }
}// Required empty public constructor
